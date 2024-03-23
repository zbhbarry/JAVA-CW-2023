package edu.uob;

import java.util.ArrayList;

public class DBTable {
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

    public String columnsToString(){
        StringBuilder stringColumns = new StringBuilder("id");
        for(DBColumn column : this.columns){
            stringColumns.append("\t");
            stringColumns.append(column.getColumnName());
        }
        return stringColumns.toString();
    }

    public void addRow(DBRow dbRow){
        this.rows.add(dbRow);
    }

    public Boolean removeRow(int rowIndex){
        if (rowIndex >= 0 && rowIndex < this.rows.size()) {
            this.rows.remove(rowIndex);
            return true;
        } else {
            return false;
        }
    }

    public Boolean updateValue(int rowIndex, int columnIndex, String value){
        if (rowIndex >= 0 && rowIndex < this.rows.size() && columnIndex >= 0 && columnIndex < this.columns.size()) {
            DBRow row = this.rows.get(rowIndex);
            row.getDataValues().set(columnIndex, value);
            this.rows.set(rowIndex, row);
            return true;
        } else {
            return false;
        }
    }
}
