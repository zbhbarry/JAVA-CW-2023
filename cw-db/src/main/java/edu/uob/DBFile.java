package edu.uob;

import java.io.*;
import java.util.Arrays;

public class DBFile {
    public static void main(String[] args) throws IOException {
        File documentFolder = new File("databases");
        File[] documents = documentFolder.listFiles();
        System.out.println(Arrays.toString(documents));

        System.out.println(documentFolder.isDirectory());

        String name = "src" + File.separator + "main";
        File fileToOpen = new File(name);
        if(fileToOpen.exists()){
            File[] documents2 = fileToOpen.listFiles();
            System.out.println(Arrays.toString(documents2));
        }

        String name2 = "test.tab";
        File fileToOpen2 = new File(name2);
        if(!fileToOpen2.exists()){
            System.out.println(fileToOpen2.createNewFile());
        }

        String dirName = "email";
        File emailFolder = new File(dirName);
        if(!emailFolder.exists()){
            System.out.println(emailFolder.mkdir());
        }

        String fileName = dirName + File.separator + "cv.txt";
        File cvFile =new File(fileName);
        if(!cvFile.exists()){
            FileWriter writer = new FileWriter(cvFile);
            writer.write("Hello\n");
            writer.write('a');
            writer.flush();
            writer.close();
        }

        String readFile = dirName + File.separator + "cv.txt";
        File readFileOpen = new File(readFile);
        FileReader reader = new FileReader(readFileOpen);
        char[] buffer = new char[10];
        reader.read(buffer, 0, buffer.length);
        System.out.println(buffer);
        reader.close();

        FileReader reader2 = new FileReader(readFileOpen);
        BufferedReader buffReader = new BufferedReader(reader2);
        String firstLine = buffReader.readLine();
        System.out.println(firstLine);
        String secondLine = buffReader.readLine();
        System.out.println(secondLine);
        buffReader.close();

    }
}
