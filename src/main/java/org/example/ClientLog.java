package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ClientLog {
    StringBuilder log;

    public ClientLog() {
        log = new StringBuilder("productNum,amount\n");
    }

    public void log(int productNum, int amount) {
        log.append(String.format("%d,%d\n", productNum, amount));
    }

    public void exportAsCSV(File txtFile) throws IOException {
        try (var writer = new FileWriter(txtFile);) {
            writer.write(String.valueOf(log));
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }


    }
}