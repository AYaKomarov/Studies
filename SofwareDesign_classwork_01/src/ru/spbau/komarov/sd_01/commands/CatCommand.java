package ru.spbau.komarov.sd_01.commands;

import java.io.*;

public class CatCommand implements Command {

    private String info = "Print file on the standard output";

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

        try {
            try (BufferedReader reader = new BufferedReader(in == null ?
                    new FileReader(args[0]) :
                    new InputStreamReader(in, "UTF-8"))) {

                StringBuilder stringBuilder = new StringBuilder();
                String ls = System.getProperty("line.separator");
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append( line );
                    stringBuilder.append( ls );
                }
                out.print(stringBuilder.toString());

            } catch (FileNotFoundException e) {
                System.out.println("No such file or directory");
            }
        } catch (IOException e) {
            System.err.print(e.getMessage());
            return;
        }
    }
}
