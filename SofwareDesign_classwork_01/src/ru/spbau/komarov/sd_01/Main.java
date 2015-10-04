package ru.spbau.komarov.sd_01;

import ru.spbau.komarov.sd_01.commands.*;

public class Main {
    public static void main(String[] args) {
        Shell.builder().addCommand("cat", new CatCommand())
                .addCommand("exit", new ExitCommand())
                .addCommand("pwd", new PwdCommand())
                .addCommand("wc", new WcCommand())
                .addCommand("cliGrep", new Grep(new GrepOptionsParserCli()))
                .addCommand("jGrep", new Grep(new GrepOptionsParserJCommander()))
                .build().start();
    }
}
