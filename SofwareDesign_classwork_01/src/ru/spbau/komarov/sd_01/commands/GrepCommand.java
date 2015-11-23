package ru.spbau.komarov.sd_01.commands;

import org.apache.commons.cli.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class GrepCommand implements Command {

    GrepOptionsParser grepOptionsParser;

    public GrepCommand(GrepOptionsParser grepOptionsParser) {
        this.grepOptionsParser = grepOptionsParser;
    }

    @Override
    public String getInfo() {
        return "grep - print lines matching a pattern";
    }

    @Override
    public void execute(String[] args, InputStream in, PrintStream out) throws ParseException, IOException {

        if(in == null && args.length < 2 || args.length == 0) {
            System.out.println("Too few arguments for grep");
            return;
        }

        LineReaderWithHandler lineReaderWithHandler = createGrepLineReader(args, in, out);
        lineReaderWithHandler.readAndHandleLines();
    }

    private LineReaderWithHandler createGrepLineReader(String[] args, InputStream in, PrintStream out) throws ParseException {
        boolean readFromFile = in == null;

        String filename = readFromFile ? args[args.length-1] : null;
        String[] options = readFromFile ? Arrays.copyOfRange(args, 0, args.length - 2) :
                Arrays.copyOfRange(args, 0, args.length - 1);
        final String query = readFromFile ? args[args.length-2] : args[args.length-1];
        final GrepParameters grepParameters = grepOptionsParser.getParameters(options);

        return new LineReaderWithHandler(in, filename, new LineReaderWithHandler.LineHandler() {
                    private int lines = 0;

                    @Override
                    public void handleLine(String line) {
                        if(lines > 0) {
                            out.print(line + "\n");
                            lines--;
                        }

                        Pattern pattern = grepParameters.isIgnoreCase() ?
                                Pattern.compile(query, Pattern.CASE_INSENSITIVE) : Pattern.compile(query);

                        if( grepParameters.isWordRegexp() ?
                                findWord(pattern, line, grepParameters) : pattern.matcher(line).find()) {
                            if(grepParameters.isAfterContext()) {
                                lines = grepParameters.getCountLinesAfterContext();
                            } else {
                                out.print(line + "\n");
                            }
                        }
                    }
                });
    }
    private boolean findWord(Pattern pattern, String line, GrepParameters grepParameters) {
        String patternString = pattern.pattern();
        boolean startLine = patternString.charAt(0) == '^';
        patternString = startLine ? patternString.substring(1) : patternString;
        boolean endLine = patternString.charAt(patternString.length()-1) == '$';
        patternString = endLine ? patternString.substring(0,patternString.length()-2) : patternString;

        String[] words = line.split("\\s+");
        for (String word : words) {
            boolean find = grepParameters.ignoreCase ? patternString.equalsIgnoreCase(word) :
                    patternString.equals(word);
            if (find) {
                if ((!startLine && !endLine) ||
                        (startLine && !endLine && line.startsWith(word)) ||
                        (!startLine && endLine && line.endsWith(word)) ||
                        (startLine && endLine && line.startsWith(word) && line.endsWith(word))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class GrepParameters {
        private boolean ignoreCase = false;
        private boolean wordRegexp = false;
        private boolean afterContext = false;
        private int countLinesAfterContext = 0;

        public void setParameters(boolean ignoreCase,
                                  boolean wordRegexp,
                                  boolean afterContext,
                                  int countLinesAfterContext) {
            this.ignoreCase = ignoreCase;
            this.wordRegexp = wordRegexp;
            this.afterContext = afterContext;
            this.countLinesAfterContext = countLinesAfterContext;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public boolean isWordRegexp() {
            return wordRegexp;
        }

        public boolean isAfterContext() {
            return afterContext;
        }

        public int getCountLinesAfterContext() {
            return countLinesAfterContext;
        }
    }
}
