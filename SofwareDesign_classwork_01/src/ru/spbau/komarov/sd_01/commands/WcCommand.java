package ru.spbau.komarov.sd_01.commands;

import java.io.*;

public class WcCommand implements Command {

    private String info = "Print newline, word, and byte counts for file";

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void execute(String[] args, InputStream in, PrintStream out) {

        if(in == null && args.length == 0) {
            System.out.println("Too few arguments");
            return;
        }

        int bytes = 0;
        int lines = 0;
        int words = 0;

        try {
            try (BufferedReader reader = new BufferedReader(in == null ?
                    new FileReader(args[0]) :
                    new InputStreamReader(in, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines++;
                    bytes += line.getBytes().length;
                    words += line.split("\\s+").length;
                }

            } catch (FileNotFoundException e) {
                System.out.println("No such file or directory");
            }
            out.format("%d %d %d\n", lines, words, bytes);
        } catch (IOException e) {
            System.err.print(e.getMessage());
            return;
        }
    }
}
