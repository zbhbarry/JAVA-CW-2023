package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DBFile {
    private final String root = "databases";
    private final String extension = ".tab";
    private String databaseFolder;

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

    public Boolean createDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        return databaseFolderOpen.mkdir();
    }

    public Boolean deleteDatabaseFolder(String databaseName){
        String databasePath = getDatabasePath(databaseName);
        File databaseFolderOpen = new File(databasePath);
        Boolean deleteResult = databaseFolderOpen.delete();
        if(deleteResult){
            setDatabaseFolder("");
        }
        return deleteResult;
    }


    public String getTablePath(String tableName){
        return getDatabasePath(databaseFolder) + File.separator + tableName.toLowerCase()+extension;
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

    public Boolean deleteTableFile(DBTable table){
        String tablePath = getTablePath(table.getTableName());
        File tableFileOpen = new File(tablePath);
        return tableFileOpen.delete();
    }
}
