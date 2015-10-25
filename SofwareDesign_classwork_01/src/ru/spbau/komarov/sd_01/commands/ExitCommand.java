package ru.spbau.komarov.sd_01.commands;

import java.io.InputStream;
import java.io.PrintStream;

public class ExitCommand implements Command {

    @Override
    public String getInfo() {
        return "Cause normal process termination";
    }

    @Override
    public void execute(String arg[], InputStream in, PrintStream out) {
        System.exit(0);
    }
}
