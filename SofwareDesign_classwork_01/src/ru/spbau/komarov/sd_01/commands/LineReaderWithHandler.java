package ru.spbau.komarov.sd_01.commands;

import java.io.*;

public class LineReaderWithHandler {

    InputStream in;
    String filename;
    LineHandler lineHandler;

    public LineReaderWithHandler(InputStream in, String filename, LineHandler lineHandler) {
        this.in = in;
        this.filename = filename;
        this.lineHandler = lineHandler;
    }

    public void readAndHandleLines() throws IOException {
        try {
            try (BufferedReader reader = new BufferedReader(in == null ?
                    new FileReader(filename) :
                    new InputStreamReader(in, "UTF-8"))) {

                StringBuilder stringBuilder = new StringBuilder();
                String ls = System.getProperty("line.separator");
                String line;
                while ((line = reader.readLine()) != null) {
                    lineHandler.hadleLine(line);
                }
            } catch (FileNotFoundException e) {
                throw e;
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public static interface LineHandler {
        public void hadleLine(String line);
    }
}
