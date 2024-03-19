package edu.uob;

public class DBColumn {
    private String columnName;
    private Class<?> dataType;

    public DBColumn(String columnName) {
        this.columnName = columnName;
        this.dataType = null;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }
}
