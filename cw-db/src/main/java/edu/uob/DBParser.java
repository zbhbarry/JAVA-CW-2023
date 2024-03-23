package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBParser {
    private final ArrayList<String> keywords = new ArrayList<>(List.of("USE", "CREATE", "DATABASE", "TABLE",
            "DROP", "ALTER", "INSERT", "INTO", "VALUES", "SELECT", "FROM", "WHERE", "UPDATE", "SET", "DELETE", "JOIN",
            "AND", "ON", "ADD", "TRUE", "FALSE", "NULL", "OR", "LIKE"));
    private ArrayList<String> tokens;
    private DBFile dbFile;
    private String message;
    private int currentTokenIndex;

    public String getMessage() {
        return this.message;
    }

    public DBParser(DBFile dbFile, ArrayList<String> tokens) {
        this.dbFile = dbFile;
        this.tokens = tokens;
        this.message = "";
        this.currentTokenIndex = 0;
        String currentToken = getCurrentToken();
        switch (currentToken.toUpperCase()) {
            case "USE" -> useCommand();
            case "CREATE" -> createCommand();
            case "DROP" -> dropCommand();
            case "ALTER" -> alterCommand();
            case "INSERT" -> insertCommand();
            case "SELECT" -> selectCommand();
            case "UPDATE" -> updateCommand();
            case "DELETE" -> deleteCommand();
            case "JOIN" -> joinCommand();
            default -> throw new IllegalArgumentException("Expected Command.");
        };
    }

    private String getCurrentToken(){
        if (currentTokenIndex < tokens.size()){
            currentTokenIndex++;
            return tokens.get(currentTokenIndex-1);
        } else {
            throw new IllegalArgumentException("Expected Command.");
        }
    };

    private void useCommand() {

    }

    private void createCommand() {
        String currentToken = getCurrentToken();
        switch (currentToken.toUpperCase()) {
            case "DATABASE" -> createDatabaseCommand();
            case "TABLE" -> createTableCommand();
            default -> throw new IllegalArgumentException("Expected Command.");
        }
    }

    private void createTableCommand() {

    }

    private void attributeListCommand() {
    }

    private void createDatabaseCommand() {

    }

    private void dropCommand() {

    }

    private void dropTableCommand() {

    }

    private void dropDatabaseCommand() {

    }

    private void alterCommand() {

    }

    private void insertCommand() {

    }

    private void selectCommand() {

    }

    private void updateCommand() {

    }

    private void deleteCommand() {

    }

    private void joinCommand() {

    }

    private void isKeyWord(String token){
        if (keywords.contains(token.toUpperCase())){
            throw new IllegalArgumentException("Keyword error.");
        }
    }

    private Boolean endCommand(){
        String currentToken = tokens.get(currentTokenIndex) ;
        if (Objects.equals(currentToken, ";")){
            if (currentTokenIndex == tokens.size()-1){
                return true;
            } else {
                throw new IllegalArgumentException("Expected Command.");
            }
        }else {
            return false;
        }
    }
}
