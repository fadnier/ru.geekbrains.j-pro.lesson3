package client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javafx.scene.control.TextArea;
import org.apache.commons.io.input.ReversedLinesFileReader;


public class HistoryChat {
    private static File filesave;

    public static void save(String userLogin,String msg) {
        filesave = new File("history_"+userLogin+".txt");
        try {
            FileWriter writer = new FileWriter(filesave, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(msg.trim());
            bufferWriter.newLine();
            bufferWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void load(String userLogin, TextArea textArea) {
        filesave = new File("history_"+userLogin+".txt");
        if(filesave.exists()) {
            List lines = readLastLine(filesave, 100);
            reverse(lines).forEach(x ->  textArea.appendText(String.valueOf(x)+"\n"));
        }
    }

    public static List readLastLine(File file, int numLastLineToRead) {

        List result = new ArrayList<>();

        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8)) {

            String line = "";
            while ((line = reader.readLine()) != null && result.size() < numLastLineToRead) {
                result.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static List reverse(List list) {
        List reverseList = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            reverseList.add(list.get(list.size()-i-1));
        }

        return reverseList;
    }
}
