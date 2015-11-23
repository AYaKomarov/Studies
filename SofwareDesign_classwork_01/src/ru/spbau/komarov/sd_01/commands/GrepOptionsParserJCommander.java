package ru.spbau.komarov.sd_01.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.apache.commons.cli.ParseException;

public class GrepOptionsParserJCommander implements GrepOptionsParser {

    @Override
    public GrepCommand.GrepParameters getParameters(String[] options) throws ParseException {
        JArgs jct = new JArgs();
        new JCommander(jct, options);

        boolean i = jct.isIgnoreCase;
        boolean w = jct.isWordRegexp;

        boolean A = false;
        int cA = 0;
        if(jct.countLinesAfterContext != null) {
            cA = jct.countLinesAfterContext;
            if(cA < 0) {
                throw new ParameterException("Negative integer");
            }
            A = true;
        }

        GrepCommand.GrepParameters gps = new GrepCommand.GrepParameters();
        gps.setParameters(i,w,A,cA);
        return gps;
    }

    private static class JArgs {
        @Parameter(names = "-A", description = "After context")
        public Integer countLinesAfterContext = null;

        @Parameter(names = "-w", description = "Word regexp")
        public boolean isWordRegexp = false;

        @Parameter(names = "-i", description = "Ignore case flag")
        private boolean isIgnoreCase = false;
    }
}
