package ru.spbau.komarov.sd_01.commands;

import org.apache.commons.cli.ParseException;

public interface GrepOptionsParser {
    GrepCommand.GrepParameters getParameters(String[] args) throws ParseException;
}
