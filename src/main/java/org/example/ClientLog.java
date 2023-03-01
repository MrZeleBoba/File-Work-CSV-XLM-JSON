package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ClientLog {
    String log = "productNum,amount\n";

    public void log(int productNum, int amount) {
        log += String.format("%d,%d\n", productNum, amount);
    }
    public void exportAsCSV (File txtFile) throws IOException {
       try(var writer = new FileWriter(txtFile);) {
           writer.write(log);
       }catch (IOException e) {
           System.out.println(Arrays.toString(e.getStackTrace()));
       }


    }
}