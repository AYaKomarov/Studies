package ru.spbau.komarov.sd_01.commands;

import java.io.*;

public class WcCommand implements Command {

    @Override
    public String getInfo() {
        return "Print newline, word, and byte counts for file";
    }

    @Override
    public void execute(String[] args, InputStream in, PrintStream out) throws IOException {

        if(in == null && args.length == 0) {
            System.out.println("Too few arguments");
            return;
        }
        String filename = in == null ? args[0] : null;

        WcLineHandler wcLineHandler = new WcLineHandler();
        LineReaderWithHandler lineReaderWithHandler =
                new LineReaderWithHandler(in, filename, wcLineHandler);

        lineReaderWithHandler.readAndHandleLines();

        out.format("%d %d %d\n", wcLineHandler.lines, wcLineHandler.words, wcLineHandler.bytes);
    }

    private class WcLineHandler implements LineReaderWithHandler.LineHandler {
        private int bytes = 0;
        private int lines = 0;
        private int words = 0;

        @Override
        public void handleLine(String line) {
            lines++;
            bytes += line.getBytes().length;
            words += line.split("\\s+").length;
        }
    }
}
