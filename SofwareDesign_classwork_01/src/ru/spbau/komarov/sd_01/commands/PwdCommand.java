package ru.spbau.komarov.sd_01.commands;

import java.io.InputStream;
import java.io.PrintStream;

public class PwdCommand implements Command {

    private String info = "Print name of current/working directory";

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void execute(String[] arg, InputStream in, PrintStream out) {
        out.println(System.getProperty("user.dir"));
    }
}
