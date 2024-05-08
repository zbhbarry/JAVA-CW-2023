package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

public final class GameServer {

    private static final char END_OF_TRANSMISSION = 4;
    private Players players;
    private GameMap map;
    private ActionsTable actionsTab;

    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    /**
     * Do not change the following method signature or we won't be able to mark your submission
     * Instanciates a new server instance, specifying a game with some configuration files
     *
     * @param entitiesFile The game configuration file containing all game entities to use in your game
     * @param actionsFile  The game configuration file containing all game actions to use in your game
     */
    public GameServer(File entitiesFile, File actionsFile) {
        try {
            Parser entitiesParser = new Parser();
            FileReader reader = new FileReader(entitiesFile);
            entitiesParser.parse(reader);
            Graph wholeDocument = entitiesParser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();
            this.map = new GameMap(sections);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionsFile);
            Element root = document.getDocumentElement();
            NodeList actions = root.getElementsByTagName("action");  // Get all action elements
            this.actionsTab = new ActionsTable(actions);

            players = new Players();

        } catch (ParseException | ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Do not change the following method signature or we won't be able to mark your submission
     * This method handles all incoming game commands and carries out the corresponding actions.</p>
     *
     * @param command The incoming command to be processed
     */
    public String handleCommand(String command) {
        String[] parts = command.split(":", 2); // Splitting at the first colon
        String username = parts[0].trim().toLowerCase();
        String actionWords = parts[1].trim().toLowerCase();

        // Check if the player exists or create a new one
        Player player = players.getPlayer(username);
        if (player == null) {
            player = new Player(username, "A new adventurer " + username + ".", this.map.getStartLocation());
            players.addPlayer(username, player);
        }

        // Handle the actual command
        try {
            ActionWords playerAction = this.actionsTab.interpret(actionWords, player, this.map.getEntriesAll());
            return player.act(playerAction, this.map);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Do not change the following method signature or we won't be able to mark your submission
     * Starts a *blocking* socket server listening for new connections.
     *
     * @param portNumber The port to listen on.
     * @throws IOException If any IO related operation fails.
     */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
     * Do not change the following method signature or we won't be able to mark your submission
     * Handles an incoming connection from the socket server.
     *
     * @param serverSocket The client socket to read/write from.
     * @throws IOException If any IO related operation fails.
     */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept(); BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream())); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if (incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
