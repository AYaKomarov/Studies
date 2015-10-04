package ru.spbau.komarov.sd_01.commands;

import java.io.InputStream;
import java.io.PrintStream;

public class ExitCommand implements Command {
    private String info = "Cause normal process termination";

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void execute(String arg[], InputStream in, PrintStream out) {
        System.exit(0);
    }
}
