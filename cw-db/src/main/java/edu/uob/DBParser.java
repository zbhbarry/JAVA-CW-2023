package edu.uob;

import java.util.*;

public class DBParser {
    private final ArrayList<String> keywords = new ArrayList<>(List.of("USE", "CREATE", "DATABASE", "TABLE",
            "DROP", "ALTER", "INSERT", "INTO", "VALUES", "SELECT", "FROM", "WHERE", "UPDATE", "SET", "DELETE", "JOIN",
            "ON", "ADD", "NULL"));
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
            ArrayList<String> attrList = attributeOrValueListCommand();
            if(endCommand()){
                dbFile.createTableFile(tbName, attrList);
                this.message = "[OK]";
            }else {
                throw new IllegalArgumentException("Illegal Command length or format.");
            }
        }
    }

    private ArrayList<String> attributeOrValueListCommand() {
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
        this.message = "[OK]";
    }

    private void alterAddCommand(String tbName, String attr) {
        DBTable table = dbFile.readTableFromFile(tbName);
        table.addColumn(attr);
        dbFile.saveTableToFile(table);
        this.message = "[OK]";
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
        ArrayList<String> valueList = attributeOrValueListCommand();
        DBTable table = dbFile.readTableFromFile(tbName);
        if(endCommand()){
            DBRow row = new DBRow(table.getNextId(), valueList);
            table.addRow(row);
            dbFile.saveTableToFile(table);
            this.message = "[OK]";
        }else {
            throw new IllegalArgumentException("Illegal Command length or format.");
        }
    }
    private void selectCommand() {
        dbFile.isUseDatabase();
        ArrayList<String> columnName = columnListCommand();

        String tbName = getCurrentToken();
        if(!dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table doesn't existed.");
        }
        DBTable table = dbFile.readTableFromFile(tbName);
        ArrayList<Integer> columnsIndex, rowsIndex;
        columnsIndex = getColumnsIndex(table, columnName);
        if(endCommand()){
            this.message = "[OK]\n" + table.toString(columnsIndex, null);
        }else {
            if(!getCurrentToken().equalsIgnoreCase("WHERE")){
                throw new IllegalArgumentException("Illegal command.");
            }
            ArrayList<String> conditions = condCommand();
            rowsIndex = getRowsIndex(table, conditions);
            this.message = "[OK]\n" + table.toString(columnsIndex, rowsIndex);
        }
    }
    private ArrayList<String> columnListCommand() {
        ArrayList<String> attrList = new ArrayList<String>();
        boolean inList = true;
        String attr = getCurrentToken();
        while (!Objects.equals(attr, "FROM") && !Objects.equals(attr, ";")){
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
        if(attr.equals(";")){
            throw new IllegalArgumentException("Illegal command.");
        }
        return attrList;
    }

    public Set<String> operater = new HashSet<>
            (Arrays.asList("==" , ">" , "<" , ">=" , "<=" , "!=" , "LIKE"
            ));
    private ArrayList<String> condCommand() {
        ArrayList<String> conditions = new ArrayList<String>();
        String cond = getCurrentToken();
        while (!Objects.equals(cond, ";")){
            isKeyWord(cond);
//            if(cond.equals("(") || cond.equals(")")){
//                continue;
//            }
            conditions.add(cond);
            cond = getCurrentToken();
        }
        if(!cond.equals(";")){
            throw new IllegalArgumentException("Illegal command.");
        }
//        if(endCommand()){
//            throw new IllegalArgumentException("Illegal command.");
//        }
        if(conditions.size() == 3){
            if(!operater.contains(conditions.get(1)) ){
                throw new IllegalArgumentException("Wrong operator.");
            }
        }
        if(conditions.size() == 7){
            if(!operater.contains(conditions.get(1)) || !operater.contains(conditions.get(5)) ||
                    (!conditions.get(3).equalsIgnoreCase("AND")
                            && !conditions.get(3).equalsIgnoreCase("OR"))){
                throw new IllegalArgumentException("Wrong operator.");
            }
        }
        return conditions;
    }

    public ArrayList<Integer> getColumnsIndex(DBTable table, ArrayList<String> columnName) {
        ArrayList<Integer> columnsIndex = new ArrayList<Integer>();
        List<DBColumn> columns = table.getColumns();
        for(String colNeed : columnName){
            if(colNeed.equalsIgnoreCase("*")){
                columnsIndex.add(-1);
                for(int i =0 ; i < columns.size(); i++){
                    columnsIndex.add(i);
                }
                break;
            }
            if(colNeed.equalsIgnoreCase("id")){
                columnsIndex.add(-1);
                continue;
            }
            isKeyWord(colNeed);
            boolean isFind = false;
            for(int i =0 ; i < columns.size(); i++){
                DBColumn colExist = columns.get(i);
                if(colNeed.equalsIgnoreCase(colExist.getColumnName())){
                    columnsIndex.add(i);
                    isFind = true;
                }
            }
            if(!isFind){
                throw new IllegalArgumentException("Attribute:" + colNeed +"is not found!");
            }
        }
        return columnsIndex;
    }

    public ArrayList<Integer> getRowsIndex(DBTable table, ArrayList<String> conditions) {
        ArrayList<Integer> rowsIndex = new ArrayList<Integer>();
        List<DBColumn> columns = table.getColumns();
        int columnsIndex = -1;

        if(conditions.size() == 3){
            String colNeed = conditions.get(0);
            if(colNeed.equalsIgnoreCase("id")){
                for(int i = 0; i < table.getRows().size(); i++){
                    DBRow row = table.getRows().get(i);
                    int id2Check = row.getId();
                    int idNow = Integer.parseInt(conditions.get(2));
                    switch (conditions.get(1)){
                        case "==":
                            if(id2Check == idNow){
                                rowsIndex.add(i);
                            }
                            break;
                        case ">":
                            if(id2Check > idNow){
                                rowsIndex.add(i);
                            }
                            break;
                        case "<":
                            if(id2Check < idNow){
                                rowsIndex.add(i);
                            }
                            break;
                        case ">=":
                            if(id2Check >= idNow){
                                rowsIndex.add(i);
                            }
                            break;
                        case "<=":
                            if(id2Check <= idNow){
                                rowsIndex.add(i);
                            }
                            break;
                        case "!=":
                            if(id2Check != idNow){
                                rowsIndex.add(i);
                            }
                            break;
                        case "LIKE":
                            throw new IllegalArgumentException("Illegal operator for two number.");
                        default:
                            throw new IllegalArgumentException("Illegal command.");
                    }
                }
            }else{
                boolean isFind = false;
                for(int i =0 ; i < columns.size(); i++){
                    DBColumn colExist = columns.get(i);
                    if(colNeed.equalsIgnoreCase(colExist.getColumnName())){
                        columnsIndex = i;
                        isFind = true;
                    }
                }
                if(!isFind){
                    throw new IllegalArgumentException("Attribute:" + colNeed +"is not found!");
                }
                for(int i = 0; i < table.getRows().size(); i++){
                    DBRow row = table.getRows().get(i);
                    String val2Check = row.getDataValues().get(columnsIndex);
                    String valNow = conditions.get(2);
                    switch (conditions.get(1)){
                        case "==":
                            if(val2Check.compareToIgnoreCase(valNow) == 0){
                                rowsIndex.add(i);
                            }
                            break;
                        case ">":
                            if(val2Check.compareToIgnoreCase(valNow) > 0){
                                rowsIndex.add(i);
                            }
                            break;
                        case "<":
                            if(val2Check.compareToIgnoreCase(valNow) < 0){
                                rowsIndex.add(i);
                            }
                            break;
                        case ">=":
                            if(val2Check.compareToIgnoreCase(valNow) >= 0){
                                rowsIndex.add(i);
                            }
                            break;
                        case "<=":
                            if(val2Check.compareToIgnoreCase(valNow) <= 0){
                                rowsIndex.add(i);
                            }
                            break;
                        case "!=":
                            if(val2Check.compareToIgnoreCase(valNow) != 0){
                                rowsIndex.add(i);
                            }
                            break;
                        case "LIKE":
                            if(val2Check.contains(valNow)){
                                rowsIndex.add(i);
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Illegal command.");
                    }
                }
            }
        }else{
            throw new IllegalArgumentException("[OK]");
        }
        return rowsIndex;
    }

    private void updateCommand() {
        dbFile.isUseDatabase();

        String tbName = getCurrentToken();
        if(!dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table doesn't existed.");
        }
        DBTable table = dbFile.readTableFromFile(tbName);
        if(!getCurrentToken().equalsIgnoreCase("SET")){
            throw new IllegalArgumentException("Illegal command.");
        }
        String attr = getCurrentToken();
        ArrayList<String> attrList = new ArrayList<>();
        attrList.add(attr);
        ArrayList<Integer> columnsIndex = getColumnsIndex(table, attrList);

        if(!getCurrentToken().equalsIgnoreCase("=")){
            throw new IllegalArgumentException("Illegal command.");
        }
        String valNew = getCurrentToken();
        if(!getCurrentToken().equalsIgnoreCase("WHERE")){
            throw new IllegalArgumentException("Illegal command.");
        }
        ArrayList<String> conditions = condCommand();
        ArrayList<Integer> rowsIndex = getRowsIndex(table, conditions);
        table.updateValue(columnsIndex, rowsIndex, valNew);
        dbFile.saveTableToFile(table);
        this.message = "[OK]";
    }

    private void deleteCommand() {
        dbFile.isUseDatabase();
        if(!getCurrentToken().equalsIgnoreCase("FROM")){
            throw new IllegalArgumentException("Illegal command.");
        }
        String tbName = getCurrentToken();
        if(!dbFile.isTableFile(tbName)){
            throw new IllegalArgumentException("Table doesn't existed.");
        }
        DBTable table = dbFile.readTableFromFile(tbName);
        if(!getCurrentToken().equalsIgnoreCase("WHERE")){
            throw new IllegalArgumentException("Illegal command.");
        }
        ArrayList<String> conditions = condCommand();
        ArrayList<Integer> rowsIndex = getRowsIndex(table, conditions);
        table.removeRow(rowsIndex);
        dbFile.saveTableToFile(table);
        this.message = "[OK]";
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
