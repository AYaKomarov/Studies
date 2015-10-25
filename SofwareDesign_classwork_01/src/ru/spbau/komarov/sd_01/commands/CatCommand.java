package ru.spbau.komarov.sd_01.commands;

import java.io.*;

public class CatCommand implements Command {

    @Override
    public String getInfo() {
        return "Print file on the standard output";
    }

    @Override
    public void execute(String[] args, InputStream in, PrintStream out) throws IOException {

        if (in == null && args.length == 0) {
            System.out.println("Too few arguments");
            return;
        }
        String filename = in == null ? args[0] : null;

        LineReaderWithHandler lineReaderWithHandler =
                new LineReaderWithHandler(in, filename, line -> out.print(line + "\n"));

        lineReaderWithHandler.readAndHandleLines();
    }
}
