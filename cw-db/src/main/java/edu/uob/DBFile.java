package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DBFile {
    private final String extension = ".tab";
    private String root;
    private String databaseFolder;

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

    public Boolean isDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        return databaseFolderOpen.exists();
    }

    public void isUseDatabase(){
        if (Objects.equals(databaseFolder, "")){
            throw new IllegalArgumentException("Database must be specified before using table.");
        }
    }

    public Boolean isTableFile(String tableName) {
        String tablePath = getTablePath(tableName);
        System.out.println(tablePath);
        File tableFileOpen = new File(tablePath);
        System.out.println(tableFileOpen.exists());
        return tableFileOpen.exists();
    }

    public void createDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        if (!databaseFolderOpen.mkdir()){
            throw new RuntimeException("Database cannot be created.");
        }
    }

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

    public void deleteTableFile(String tableName){
        String tablePath = getTablePath(tableName);
        File tableFileOpen = new File(tablePath);
        if (!tableFileOpen.delete()){
            throw new RuntimeException("Table cannot be deleted.");
        }
    }

    public DBTable readTableFromFile(String tableName){
        String tablePath = getTablePath(tableName);
        DBTable table = new DBTable(tableName);
        File tableFileOpen = new File(tablePath);
        try(BufferedReader tableReader = new BufferedReader(new FileReader(tableFileOpen))){
            String firstLine = tableReader.readLine();
            String[] rawColumnNames = firstLine.split("[\t ]");
            ArrayList<DBColumn> columns = new ArrayList<>();
            for (String columnName : rawColumnNames) {
                if (!columnName.equalsIgnoreCase("id")) {
                    columns.add(new DBColumn(columnName));
                }
            }
            table.setColumns(columns);
            String currentLine;
            while ((currentLine = tableReader.readLine()) != null) {
                String[] rowValues = currentLine.split("[\t ]");
                ArrayList<String> rowDataValues = new ArrayList<>(Arrays.asList(rowValues).subList(1, rowValues.length));
                DBRow row = new DBRow(Integer.parseInt(rowValues[0]), rowDataValues);
                table.addRow(row);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return table;
    }

    public Boolean saveTableToFile(DBTable table){
        String tablePath = getTablePath(table.getTableName());
        File tableFileOpen = new File(tablePath);
        try{
            if(tableFileOpen.createNewFile()){
                FileWriter tableFileWriter = new FileWriter(tableFileOpen);
                tableFileWriter.write(table.columnsToString());
                for (DBRow row : table.getRows()){
                    tableFileWriter.write("\n" + row.toString());
                }
                tableFileWriter.flush();
                tableFileWriter.close();
                return true;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return false;
    }
}
