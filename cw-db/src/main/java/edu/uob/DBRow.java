package edu.uob;

import java.util.List;

public class DBRow {
    private int id;
    private List<String> dataValues;

    public DBRow(int id, List<String> dataValues) {
        this.id = id;
        this.dataValues = dataValues;
    }

    public int getId() {
        return id;
    }

    public List<String> getDataValues() {
        return dataValues;
    }

    public void setDataValues(List<String> dataValues) {
        this.dataValues = dataValues;
    }
}
