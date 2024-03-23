package edu.uob;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DBFile {
    // Handles file operations for databases and tables.
    private final String extension = ".tab"; // Default file extension for table files.
    private String root = "databases";
    private String databaseFolder; // The current database in use.

    public void setRoot(String root) {
        this.root = root;
    }

    public DBFile() {
        this.databaseFolder = "";
    }

    public String getDatabaseFolder() {
        return databaseFolder;
    }

    public void setDatabaseFolder(String databaseFolder) {
        this.databaseFolder = databaseFolder;
    }

    public String getDatabasePath(String databaseName){
        if(Objects.equals(databaseName, "")){
            return root;
        }else {
            return root + File.separator + databaseName.toLowerCase();
        }
    }

    // Checks if a database folder exists.
    public Boolean isDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        return databaseFolderOpen.exists();
    }

    // Ensures a database has been specified before using a table.
    public void isUseDatabase(){
        if (Objects.equals(databaseFolder, "")){
            throw new IllegalArgumentException("Database must be specified before using table.");
        }
    }

    // Checks if a table file exists.
    public Boolean isTableFile(String tableName) {
        String tablePath = getTablePath(tableName);
        File tableFileOpen = new File(tablePath);
        return tableFileOpen.exists();
    }

    // Creates a database folder.
    public void createDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        if (!databaseFolderOpen.mkdir()){
            throw new RuntimeException("Database cannot be created.");
        }
    }

    // Deletes a database folder.
    public void deleteDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        if (!databaseFolderOpen.delete()){
            throw new RuntimeException("Database cannot be deleted.");
        }
        databaseFolder = "";
    }


    public String getTablePath(String tableName){
        return getDatabasePath(databaseFolder) + File.separator + tableName.toLowerCase()+extension;
    }

    // Creates a new table file and initializes it with column names.
    public void createTableFile(String tableName, ArrayList<String> attributeList){
        String tablePath = getTablePath(tableName);
        File tableFileOpen = new File(tablePath);
        try{
            if(tableFileOpen.createNewFile()){
                FileWriter tableFileWriter = new FileWriter(tableFileOpen);
                tableFileWriter.write("id\t"+String.join("\t", attributeList));
                tableFileWriter.flush();
                tableFileWriter.close();
            }
        }catch (IOException ioe){
            throw new RuntimeException("The table file cannot be created, please check the permissions.");
        }
    }

    // Deletes a table file.
    public void deleteTableFile(String tableName){
        String tablePath = getTablePath(tableName);
        File tableFileOpen = new File(tablePath);
        if (!tableFileOpen.delete()){
            throw new RuntimeException("Table cannot be deleted.");
        }
    }

    // Reads a table from its file and constructs a DBTable object.
    public DBTable readTableFromFile(String tableName){
        String tablePath = getTablePath(tableName);
        DBTable table = new DBTable(tableName);
        File tableFileOpen = new File(tablePath);
        try(BufferedReader tableReader = new BufferedReader(new FileReader(tableFileOpen))){
            String firstLine = tableReader.readLine();
            String[] rawColumnNames = firstLine.split("\t", -1);
            ArrayList<DBColumn> columns = new ArrayList<>();
            for (String columnName : rawColumnNames) {
                if (!columnName.equalsIgnoreCase("id")) {
                    columns.add(new DBColumn(columnName));
                }
            }
            table.setColumns(columns);
            String currentLine;
            while ((currentLine = tableReader.readLine()) != null) {
                String[] rowValues = currentLine.split("\t" , -1);
                ArrayList<String> rowDataValues = new ArrayList<>(Arrays.asList(rowValues).subList(1, rowValues.length));
                DBRow row = new DBRow(Integer.parseInt(rowValues[0]), rowDataValues);
                table.addRow(row);
            }
        }catch(IOException ioe){
            throw new RuntimeException("Table cannot be opened.");
        }
        return table;
    }

    // Saves a DBTable object to its corresponding file.
    public void saveTableToFile(DBTable table){
        String tablePath = getTablePath(table.getTableName());
        try {
            Files.write(Paths.get(tablePath), table.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Table cannot be saved.");
        }
    }
}
