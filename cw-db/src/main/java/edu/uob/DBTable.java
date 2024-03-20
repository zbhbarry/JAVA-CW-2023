package edu.uob;

import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBTable {
    private String tableName;
    private List<DBColumn> columns;
    private List<DBRow> rows;
    private int count;

    public DBTable(String tableName, List<DBColumn> columns, List<DBRow> rows) {
        this.tableName = tableName;
        this.columns = columns;
        this.rows = rows;
        this.count = 0;
    }

    public void addRow(DBRow dbRow){
        this.rows.add(dbRow);
        this.count++;
    }

    public Boolean removeRow(int rowIndex){
        if (rowIndex >= 0 && rowIndex < this.rows.size()) {
            this.rows.remove(rowIndex);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public String rowToString(DBRow row){
        return "";
    }

    public String columnToString(){
        return "";
    }

    public void saveTable(){
        String root = "databases";
        String extension = ".tab";
        try {
            String tablePath = root + File.separator + tableName.toLowerCase()+extension;
            File tableFileOpen = new File(tablePath);
            if(!tableFileOpen.exists()){
                FileWriter writer = new FileWriter(tableFileOpen);
                writer.write(columnToString());
                for(DBRow row: this.rows){
                    writer.write(this.rowToString(row));
                }
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static DBTable readTable(String tableName){
        String root = "databases";
        String extension = ".tab";
        try{
            String tablePath = root + File.separator + tableName.toLowerCase()+extension;
            File tableFileOpen = new File(tablePath);
            BufferedReader tableReader = new BufferedReader(new FileReader(tableFileOpen));
            String firstLine = tableReader.readLine();
            String[] columnNames = firstLine.split("[\t ]");
            List<DBColumn> columns = new ArrayList<>();
            for (String columnName : columnNames) {
                if (!columnName.equalsIgnoreCase("id")) {
                    columns.add(new DBColumn(columnName));
                }
            }
            List<DBRow> dbRows = new ArrayList<>();
            DBTable dbTable = new DBTable(tableName, columns, dbRows);
            String currentLine;
            while ((currentLine = tableReader.readLine()) != null) {
                String[] rowValues = currentLine.split("[\t ]");
                List<String> rowDataValues = new ArrayList<>(Arrays.asList(rowValues).subList(1, rowValues.length));
                DBRow dbRow = new DBRow(Integer.parseInt(rowValues[0]), rowDataValues);
                dbTable.addRow(dbRow);
            }
            return dbTable;
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }
}
