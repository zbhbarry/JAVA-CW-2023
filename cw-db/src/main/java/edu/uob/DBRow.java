package edu.uob;

import java.util.ArrayList;

public class DBRow {
    // Represents a row in a database table.
    private int id;
    private ArrayList<String> dataValues;

    public DBRow(int id, ArrayList<String> dataValues) {
        this.id = id;
        this.dataValues = dataValues;
    }
    public void delDataValue(int index){
        dataValues.remove(index);
    }

    public void addNull(){
        dataValues.add("");
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
