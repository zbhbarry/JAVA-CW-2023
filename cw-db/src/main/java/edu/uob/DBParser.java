package edu.uob;

import javax.swing.text.TabableView;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;

public class DBParser {
    private final ArrayList<String> keywords = new ArrayList<>(List.of("USE", "CREATE", "DATABASE", "TABLE",
            "DROP", "ALTER", "INSERT", "INTO", "VALUES", "SELECT", "FROM", "WHERE", "UPDATE", "SET", "DELETE", "JOIN",
            "AND", "ON", "ADD", "TRUE", "FALSE", "NULL", "OR", "LIKE"));
    private ArrayList<String> tokens;
    private DBFile dbFile;
    private String message;
    private int currentTokenIndex;

    public String getMessage() {
        return this.message;
    }

    public DBParser(DBFile dbFile, ArrayList<String> tokens) {
        this.dbFile = dbFile;
        this.tokens = tokens;
        this.message = "";
        this.currentTokenIndex = 0;
        String currentToken = getCurrentToken();
        switch (currentToken.toUpperCase()) {
            case "USE" -> useCommand();
            case "CREATE" -> createCommand();
            case "DROP" -> dropCommand();
            case "ALTER" -> alterCommand();
            case "INSERT" -> insertCommand();
            case "SELECT" -> selectCommand();
            case "UPDATE" -> updateCommand();
            case "DELETE" -> deleteCommand();
            case "JOIN" -> joinCommand();
            default -> throw new IllegalArgumentException("Expected Command.");
        };
    }

    private String getCurrentToken(){
        if (currentTokenIndex < tokens.size()){
            currentTokenIndex++;
            return tokens.get(currentTokenIndex-1);
        } else {
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
    };

    private void useCommand() {
        String dbname = getCurrentToken();
        isKeyWord(dbname);
        if(!dbFile.isDatabaseFolder(dbname)){
            throw new IllegalArgumentException("Database doesn't exist.");
        }

        if (endCommand()){
            dbFile.setDatabaseFolder(dbname);
            this.message = "[OK]";
        } else {
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
    }

    private void createCommand() {
        String objType = getCurrentToken();
        switch (objType.toUpperCase()) {
            case "DATABASE" -> createDatabaseCommand();
            case "TABLE" -> createTableCommand();
            default -> throw new IllegalArgumentException("Expected Command.");
        }
    }

    private void createDatabaseCommand() {
        String dbName = getCurrentToken();
        isKeyWord(dbName);
        if(dbFile.isDatabaseFolder(dbName)){
            throw new IllegalArgumentException("Database already existed.");
        }
        if (endCommand()){
            dbFile.createDatabaseFolder(dbName);
            this.message = "[OK]";
        } else {
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
    }
    private void createTableCommand() {
        String tbName = getCurrentToken();
        isKeyWord(tbName);
        dbFile.isUseDatabase();
        if(dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table already existed.");
        }
        if (endCommand()){
            dbFile.createTableFile(tbName, null);
            this.message = "[OK]";
        } else {
            ArrayList<String> attrList = createAttributeListCommand();
            if(endCommand()){
                dbFile.createTableFile(tbName, attrList);
                this.message = "[OK]";
            }else {
                throw new IllegalArgumentException("Illegal Command length or format.");
            }
        }
    }
    private ArrayList<String> createAttributeListCommand() {
        ArrayList<String> attrList = new ArrayList<String>();
        boolean inList = true;
        if(!Objects.equals(getCurrentToken(), "(") || !Objects.equals(tokens.get(tokens.size() - 2), ")")){
            throw new IllegalArgumentException("Attributes list is not in () .");
        }else {
            String attr = getCurrentToken();
            while (!Objects.equals(attr, ")")){
                if(inList){
                    isKeyWord(attr);
                    attrList.add(attr);
                    inList = false;
                }else {
                    inList = true;
                    if(!Objects.equals(attr, ",")){
                        throw new IllegalArgumentException("Attributes should be parted by , .");
                    }
                }
                attr = getCurrentToken();
            }
        }
        return attrList;
    }

    private void dropCommand() {
        String objType = getCurrentToken();
        switch (objType.toUpperCase()) {
            case "DATABASE" -> dropDatabaseCommand();
            case "TABLE" -> dropTableCommand();
            default -> throw new IllegalArgumentException("Unexpected Command.");
        }
    }
    private void dropDatabaseCommand() {
        String dbName = getCurrentToken();
        if(!dbFile.isDatabaseFolder(dbName)){
            throw new IllegalArgumentException("Database doesn't existed.");
        }
        if (endCommand()){
            dbFile.deleteDatabaseFolder(dbName);
            this.message = "[OK]";
        } else {
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
    }
    private void dropTableCommand() {
        String tbName = getCurrentToken();
        dbFile.isUseDatabase();
        if(!dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table doesn't existed.");
        }
        if (endCommand()){
            dbFile.deleteTableFile(tbName);
            this.message = "[OK]";
        } else {
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
    }

    private void alterCommand() {
        dbFile.isUseDatabase();
        if(!getCurrentToken().equalsIgnoreCase("table")){
            throw new IllegalArgumentException("Illegal object.");
        }
        String tbName = getCurrentToken();
        if(!dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table doesn't existed.");
        }
        String type = getCurrentToken().toUpperCase();
        String attr = getCurrentToken();
        if(!endCommand()){
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
        switch (type){
            case "ADD" -> alterAddCommand(tbName, attr);
            case "DROP" -> alterDropCommand(tbName, attr);
            default -> throw new IllegalArgumentException("Unexpected Command.");
        }
    }

    private void alterDropCommand(String tbName, String attr) {
        DBTable table = dbFile.readTableFromFile(tbName);
        table.dropColumn(attr);
        dbFile.saveTableToFile(table);
    }

    private void alterAddCommand(String tbName, String attr) {
        DBTable table = dbFile.readTableFromFile(tbName);
        table.addColumn(attr);
        dbFile.saveTableToFile(table);
    }

    private void insertCommand() {
        dbFile.isUseDatabase();
        if(!getCurrentToken().equalsIgnoreCase("INTO")){
            throw new IllegalArgumentException("Illegal command.");
        }
        String tbName = getCurrentToken();
        if(!dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table doesn't existed.");
        }
        if(!getCurrentToken().equalsIgnoreCase("VALUES")){
            throw new IllegalArgumentException("Illegal command.");
        }

    }

    private void selectCommand() {

    }

    private void updateCommand() {

    }

    private void deleteCommand() {

    }

    private void joinCommand() {

    }

    private void isKeyWord(String token){
        if (keywords.contains(token.toUpperCase())){
            throw new IllegalArgumentException("Keyword error.");
        }
    }

    private Boolean endCommand(){
        String currentToken = tokens.get(currentTokenIndex) ;
        if (Objects.equals(currentToken, ";")){
            if (currentTokenIndex == tokens.size()-1){
                return true;
            } else {
                throw new IllegalArgumentException("Illegal Command length or format.");
            }
        }else {
            return false;
        }
    }
}
