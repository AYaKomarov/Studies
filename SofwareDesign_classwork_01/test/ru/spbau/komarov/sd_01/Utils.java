package ru.spbau.komarov.sd_01;

import org.apache.commons.cli.ParseException;
import ru.spbau.komarov.sd_01.commands.Command;

import java.io.*;

public class Utils {
    public static String getResult(String[] args, InputStream in, Command command) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PrintStream out = new PrintStream(baos, true, "utf-8");
            try {
                command.execute(args, in, out);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }
}
