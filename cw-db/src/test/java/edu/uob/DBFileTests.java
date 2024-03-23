package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DBFileTests {
    private DBFile dbFile;

    @BeforeEach
    public void init() {
        dbFile = new DBFile();
    }

    @Test
    public void testConstructor() {
        assertEquals(dbFile.getDatabaseFolder(), "");
    }

    @Test
    public void testGetDatabasePath() {
        assertEquals(dbFile.getDatabasePath(""), "databases");
        assertEquals(dbFile.getDatabasePath("markbook"), "databases" + File.separator + "markbook");
    }

    @Test
    public void testGetTablePath() {
        dbFile.setDatabaseFolder("markbook");
        String tableName = "mark";
        assertEquals(dbFile.getTablePath(tableName),
                "databases" + File.separator + "markbook" + File.separator + "mark.tab");
    }

    @Test
    public void testIsDatabaseFolder(){
        assertFalse(dbFile.isDatabaseFolder("markbook"));
        dbFile.createDatabaseFolder("markbook");
        assertTrue(dbFile.isDatabaseFolder("markbook"));
        dbFile.deleteDatabaseFolder("markbook");
    }

    @Test
    public void testCreateAndDeleteDatabase(){
        dbFile.createDatabaseFolder("markbook");
        dbFile.deleteDatabaseFolder("markbook");
        assertEquals(dbFile.getDatabaseFolder(),"");
    }

    @Test
    public void testSaveReadAndDeleteTable(){
        dbFile.createDatabaseFolder("markbook1");
        dbFile.setDatabaseFolder("markbook1");
        DBTable table = new DBTable("mark1");
        ArrayList<DBColumn> columns = new ArrayList<>();
        columns.add(new DBColumn("name"));
        columns.add(new DBColumn("mark"));
        table.setColumns(columns);
        ArrayList<String> rowDataValues = new ArrayList<>();
        rowDataValues.add("Tom");
        rowDataValues.add("65");
        DBRow row = new DBRow(table.getRows().size()+1, rowDataValues);
        table.addRow(row);
        System.out.println(table.toString());
        dbFile.saveTableToFile(table);
//        DBTable newTable = dbFile.readTableFromFile("mark");
//        assertEquals(newTable.getTableName(), "mark");
//        assertEquals(newTable.columnsToString(), "id\tname\tmark");
//        dbFile.deleteTableFile("mark");
//        dbFile.deleteDatabaseFolder("markbook");
    }
}
