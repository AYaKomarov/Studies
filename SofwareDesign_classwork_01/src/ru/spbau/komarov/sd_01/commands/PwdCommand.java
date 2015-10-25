package ru.spbau.komarov.sd_01.commands;

import java.io.InputStream;
import java.io.PrintStream;

public class PwdCommand implements Command {

    @Override
    public String getInfo() {
        return "Print name of current/working directory";
    }

    @Override
    public void execute(String[] arg, InputStream in, PrintStream out) {
        out.println(System.getProperty("user.dir"));
    }
}
