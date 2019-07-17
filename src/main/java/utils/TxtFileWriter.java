package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TxtFileWriter {
    private String fileName;
    private static final Logger loger = Logger.getLogger(Logger.class.getName());


    public TxtFileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(String line) {
        try (BufferedWriter bufferedWriter =
                     new BufferedWriter(
                             new FileWriter(fileName, true))) {
            bufferedWriter.write(line);
        } catch (IOException e) {
            loger.warning("Error!" + e.getMessage());
        }
    }

    public void customWrite(ArrayList<String> listOfAccounts, String productStr, int amountOfItmes) {

        try (BufferedWriter bufferedWriterAux = new BufferedWriter(
                new FileWriter(ApplicationConst.FILE_PRODUCTS_PATH_AUX))) {

            for (String line : listOfAccounts) {
                String[] args = line.split(" ");
                if (args.length != 3) continue;

                if (line.contains(productStr)) {
                    String updateCurrentQuantity = (Integer.parseInt(args[1]) - amountOfItmes) + "";
                    line = args[0] + " " + updateCurrentQuantity + " " + args[2] + " ";
                }

                //scriu in noul fisier toate liniile + cea modificata
                bufferedWriterAux.write(line);
                bufferedWriterAux.write("\n");
                bufferedWriterAux.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
