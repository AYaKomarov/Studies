package ru.spbau.komarov.sd_01.commands;

import org.apache.commons.cli.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Grep implements Command {

    GrepOptionsParser grepOptionsParser;

    public Grep(GrepOptionsParser grepOptionsParser) {
        this.grepOptionsParser = grepOptionsParser;
    }

    @Override
    public void execute(String[] args, InputStream in, PrintStream out) {

        if(in == null && args.length < 2 || args.length == 0) {
            System.out.println("Too few arguments for grep");
            return;
        }

        String query = in == null ? args[args.length-2] : args[args.length-1];

        GrepParameters grepParameters = null;
        try {
            String[] options = in == null ? Arrays.copyOfRange(args, 0, args.length - 2) :
                    Arrays.copyOfRange(args, 0, args.length - 1);
            grepParameters = grepOptionsParser.getParameters(options);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        List<String> resultsLines = new ArrayList<>();
        int lines = 0;

        try {
            try (BufferedReader reader = new BufferedReader(in == null ?
                    new FileReader(args[args.length-1]) :
                    new InputStreamReader(in, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if(lines > 0) {
                        resultsLines.add(line);
                        lines--;
                    }

                    Pattern pattern;
                    if (grepParameters.isIgnoreCase()) {
                        pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                    } else {
                        pattern = Pattern.compile(query);
                    }

                    if( (!grepParameters.isWordRegexp() && pattern.matcher(line).find()) ||
                            (grepParameters.isWordRegexp() && findWord(pattern, line, grepParameters))) {
                        if(grepParameters.isAfterContext()) {
                            lines = grepParameters.getCountLinesAfterContext();
                        } else {
                            resultsLines.add(line);
                        }
                    }

                }
            } catch (FileNotFoundException e) {
                System.out.println("No such file or directory");
            }
        } catch (IOException e) {
            System.err.print(e.getMessage());
            return;
        }

        for(String line : resultsLines) {
            out.println(line);
        }
    }


    private boolean findWord(Pattern pattern, String line, GrepParameters grepParameters) {
        String patternString = pattern.pattern();
        boolean startLine = patternString.charAt(0) == '^';
        patternString = startLine ? patternString.substring(1) : patternString;
        boolean endLine = patternString.charAt(patternString.length()-1) == '$';
        patternString = endLine ? patternString.substring(0,patternString.length()-2) : patternString;

        String[] words = line.split("\\s+");
        for(int i=0; i < words.length; i++) {
            boolean find = grepParameters.ignoreCase ? patternString.equalsIgnoreCase(words[i]) :
                    patternString.equals(words[i]);
            if (find) {
                if (    (!startLine && !endLine) ||
                        (startLine && !endLine && line.startsWith(words[i])) ||
                        (!startLine && endLine && line.endsWith(words[i])) ||
                        (startLine && endLine && line.startsWith(words[i]) && line.endsWith(words[i]))) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public String getInfo() {
        return "grep - print lines matching a pattern";
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
