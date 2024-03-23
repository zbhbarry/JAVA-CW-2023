package edu.uob;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataStructuresTests {

    @Test
    public void testRow(){
        ArrayList<String> values = new ArrayList<>();
        values.add("people");
        values.add("lion");
        values.add("cat");
        DBRow dbRow = new DBRow(0, values);
        assertEquals(dbRow.toString(),"0\tpeople\tlion\tcat");
    }

    @Test
    public void testColumn(){
        DBColumn column = new DBColumn("name");
        assertEquals("name",column.getColumnName());
    }

    @Test
    public void testTable(){
        DBTable table = new DBTable("markbook");
        ArrayList<DBColumn> columns = new ArrayList<>();
        columns.add(new DBColumn("name"));
        columns.add(new DBColumn("mark"));
        columns.add(new DBColumn("pass"));
        table.setColumns(columns);
        assertEquals(table.columnsToString(),"id\tname\tmark\tpass");
        ArrayList<String> dataValues = new ArrayList<>(List.of("Simon", "65", "TRUE"));
        DBRow row = new DBRow(table.getRows().size()+1, dataValues);
        table.addRow(row);
        assertEquals("1\tSimon\t65\tTRUE", table.getRows().get(0).toString());
        table.updateValue(0,1,"75");
        assertEquals("1\tSimon\t75\tTRUE", table.getRows().get(0).toString());
        table.removeRow(0);
        assertEquals(table.getRows().size(),0);
    }
}
