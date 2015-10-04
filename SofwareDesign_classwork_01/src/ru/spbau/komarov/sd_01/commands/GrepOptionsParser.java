package ru.spbau.komarov.sd_01.commands;

import org.apache.commons.cli.ParseException;

public interface GrepOptionsParser {
    Grep.GrepParameters getParameters(String[] args) throws ParseException;
}
