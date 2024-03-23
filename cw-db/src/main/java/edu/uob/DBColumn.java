package edu.uob;

public class DBColumn {
    // Represents a column in a database table.
    private String columnName; // The name of the column.

    public DBColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
