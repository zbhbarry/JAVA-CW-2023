package edu.uob;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DBTable {
    // Represents a table within a database, holding both the structure (columns) and data (rows).
    private String tableName;
    private ArrayList<DBColumn> columns;
    private ArrayList<DBRow> rows;

    public DBTable(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<DBRow> getRows() {
        return rows;
    }

    public ArrayList<DBColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<DBColumn> columns) {
        this.columns = columns;
    }

    // Converts the list of columns to a string representation, separated by tabs.
    public String columnsToString(){
        StringBuilder stringColumns = new StringBuilder("id");
        for(DBColumn column : this.columns){
            stringColumns.append("\t");
            stringColumns.append(column.getColumnName());
        }
        return stringColumns.toString();
    }

    // Converts the entire table (columns and rows) to a string representation.
    public String toString(){
        StringBuilder result = new StringBuilder(columnsToString());
        for(DBRow row: rows){
            result.append("\n");
            result.append(row.toString());
        }
        return result.toString();
    }

    // Converts specified columns and rows to a string representation, based on provided indices.
    public String toString(ArrayList<Integer> columnsIndex, ArrayList<Integer> rowsIndex){
        if (rowsIndex == null){
            StringBuilder result = new StringBuilder();
            ArrayList<String> line = new ArrayList<>();
            for (Integer index : columnsIndex) {
                if (index == -1){
                    line.add("id");
                }
                if(index >= 0 && index < columns.size()) {
                    line.add(columns.get(index).getColumnName());
                }
            }
            result.append(String.join("\t",line));
            for (DBRow row : rows) {
                result.append("\n");
                line = new ArrayList<>();
                for (Integer colIndex : columnsIndex) {
                    if (colIndex == -1){
                        line.add(String.valueOf(row.getId()));
                    }
                    if(colIndex >= 0 && colIndex < row.getDataValues().size()) {
                        line.add(row.getDataValues().get(colIndex));
                    }
                }
                result.append(String.join("\t",line));
            }
            return result.toString();
        }else {
            StringBuilder result = new StringBuilder();
            ArrayList<String> line = new ArrayList<>();
            for (Integer index : columnsIndex) {
                if (index == -1){
                    line.add("id");
                }
                if(index >= 0 && index < columns.size()) {
                    line.add(columns.get(index).getColumnName());
                }
            }
            result.append(String.join("\t",line));
            // 添加行数据
            for (Integer rowIndex : rowsIndex) {
                if(rowIndex >= 0 && rowIndex < rows.size()) {
                    result.append("\n");
                    line = new ArrayList<>();
                    DBRow row = rows.get(rowIndex);
                    for (Integer colIndex : columnsIndex) {
                        if (colIndex == -1){
                            line.add(String.valueOf(row.getId()));
                        }
                        if(colIndex >= 0 && colIndex < row.getDataValues().size()) {
                            line.add(row.getDataValues().get(colIndex));
                        }
                    }
                    result.append(String.join("\t",line));
                }
            }
            return result.toString();
        }
    }


    public void addRow(DBRow dbRow){
        this.rows.add(dbRow);
    }

    public void removeRow(int rowIndex){
        if (rowIndex >= 0 && rowIndex < this.rows.size()) {
            this.rows.remove(rowIndex);
        } else {
            throw new RuntimeException("Index out of the range.");
        }
    }

    public void removeRow(ArrayList<Integer> rowsIndex){
        rowsIndex.sort((a, b) -> b - a);
        for (int rowIndex : rowsIndex){
            removeRow(rowIndex);
        }
    }

    public void updateValue(int rowIndex, int columnIndex, String value){
        DBRow row = this.rows.get(rowIndex);
        row.getDataValues().set(columnIndex, value);
        this.rows.set(rowIndex, row);
    }

    public void updateValue(ArrayList<Integer> columnsIndex, ArrayList<Integer> rowsIndex, String value){
        for (int rowIndex: rowsIndex){
            for (int columnIndex: columnsIndex){
                updateValue(rowIndex,columnIndex, value);
            }
        }
    }

    public void dropColumn(String attr) {
        if (attr.equals("id")){
            throw new RuntimeException("Id is the primary key and cannot be deleted.");
        }
        int delIndex = -1;
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            if (attr.equalsIgnoreCase(columns.get(columnIndex).getColumnName())){
                delIndex = columnIndex;
                break;
            }
        }
        if (delIndex == -1){
            throw new RuntimeException("The column to be deleted does not exist.");
        }
        columns.remove(delIndex);
        for(DBRow row : rows){
            row.delDataValue(delIndex);
        }
    }

    public void addColumn(String attr) {
        if (attr.equals("id")){
            throw new RuntimeException("Id is the primary key and cannot be added.");
        }
        for (DBColumn column : columns) {
            if (attr.equalsIgnoreCase(column.getColumnName())) {
                throw new RuntimeException("Cannot create column with the same name.");
            }
        }
        columns.add(new DBColumn(attr));
        for(DBRow row : rows){
            row.addNull();
        }
    }

    // Generates the next ID for a new row based on existing rows.
    public int getNextId() {
        if (rows.isEmpty()){
            return 1;
        }else
        {
            return rows.get(rows.size()-1).getId()+1;
        }
    }
}
