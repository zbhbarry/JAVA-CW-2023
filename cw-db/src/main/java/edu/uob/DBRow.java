package edu.uob;

import java.util.List;

public class DBRow {
    private int id;
    private List<Object> dataValues;

    public DBRow(int id, List<Object> dataValues) {
        this.id = id;
        this.dataValues = dataValues;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Object> getDataValues() {
        return dataValues;
    }

    public void setDataValues(List<Object> dataValues) {
        this.dataValues = dataValues;
    }
}
