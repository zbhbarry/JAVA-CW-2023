package edu.uob;

import java.io.*;
import java.util.Arrays;

public class DBFile {
    private String root = "databases";
    private String extension = ".tab";
    public String readTable(String tableName){
        try{
            String tablePath = root + File.separator + tableName.toLowerCase()+extension;
            File tableFileOpen = new File(tablePath);
            BufferedReader tableReader = new BufferedReader(new FileReader(tableFileOpen));

            String currentLine;
            while ((currentLine = tableReader.readLine()) != null) {
                System.out.println(currentLine);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) {
//        DBFile dbFile = new DBFile();
//        dbFile.readTable("people");
//        String header = "id Name Age Email";
//        String[] parts = header.split("[\t ]");
//        DBColumn[] columns = new DBColumn[parts.length - 1];
//        int index = 0;
//        for (String part : parts) {
//            if (!part.equalsIgnoreCase("id")) {
//                columns[index++] = new DBColumn(part);
//            }
//        }
//
//        // 输出结果，验证是否正确创建了Column对象数组
//        for (DBColumn column : columns) {
//            System.out.println(column.getColumnName());
//        }
//        File documentFolder = new File("databases");
//        File[] documents = documentFolder.listFiles();
//        System.out.println(Arrays.toString(documents));
//
//        System.out.println(documentFolder.isDirectory());
//
//        String name = "src" + File.separator + "main";
//        File fileToOpen = new File(name);
//        if(fileToOpen.exists()){
//            File[] documents2 = fileToOpen.listFiles();
//            System.out.println(Arrays.toString(documents2));
//        }
//
//        String name2 = "test.tab";
//        File fileToOpen2 = new File(name2);
//        if(!fileToOpen2.exists()){
//            System.out.println(fileToOpen2.createNewFile());
//        }
//
//        String dirName = "email";
//        File emailFolder = new File(dirName);
//        if(!emailFolder.exists()){
//            System.out.println(emailFolder.mkdir());
//        }
//
//        String fileName = dirName + File.separator + "cv.txt";
//        File cvFile =new File(fileName);
//        if(!cvFile.exists()){
//            FileWriter writer = new FileWriter(cvFile);
//            writer.write("Hello\n");
//            writer.write('a');
//            writer.flush();
//            writer.close();
//        }
//
//        String readFile = dirName + File.separator + "cv.txt";
//        File readFileOpen = new File(readFile);
//        FileReader reader = new FileReader(readFileOpen);
//        char[] buffer = new char[10];
//        reader.read(buffer, 0, buffer.length);
//        System.out.println(buffer);
//        reader.close();
//
//        FileReader reader2 = new FileReader(readFileOpen);
//        BufferedReader buffReader = new BufferedReader(reader2);
//        String firstLine = buffReader.readLine();
//        System.out.println(firstLine);
//        String secondLine = buffReader.readLine();
//        System.out.println(secondLine);
//        buffReader.close();

//        try{
//            String fileName = "databases" + File.separator + "people.tab";
//            File file = new File(fileName);
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//            //读取读取文件内容并打印
//            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
//            }
//            bufferedReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
