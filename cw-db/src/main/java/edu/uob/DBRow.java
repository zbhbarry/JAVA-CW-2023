package edu.uob;

import java.util.ArrayList;

public class DBRow {
    private int id;
    private ArrayList<String> dataValues;

    public DBRow(int id, ArrayList<String> dataValues) {
        this.id = id;
        this.dataValues = dataValues;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getDataValues() {
        return dataValues;
    }

    public void setDataValues(ArrayList<String> dataValues) {
        this.dataValues = dataValues;
    }

    public String toString(){
        return this.id + "\t" + String.join("\t", this.dataValues);
    }
}
