package main;

import actions.ProcessData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public class Test2 {
    public static void main(String[] args) throws IOException {
        InputLoader inputLoader = new InputLoader("test_db/test_files/large_test_no_8.json");
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer("result/test");
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        ProcessData data = new ProcessData(input);
        data.processActions(arrayResult, fileWriter);
        fileWriter.closeJSON(arrayResult);
    }
}
