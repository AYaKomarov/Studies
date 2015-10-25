package ru.spbau.komarov.sd_01.commands;

import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public interface Command {
    public void execute(String[] args, InputStream in, PrintStream out) throws IOException, ParseException;
    public String getInfo();
}
