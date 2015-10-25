package ru.spbau.komarov.sd_01;

import org.junit.Test;
import ru.spbau.komarov.sd_01.commands.GrepCommand;
import ru.spbau.komarov.sd_01.commands.GrepOptionsParserJCommander;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;


public class GrepTest {
    private static String[] lines = {
            "start one two three seven pampam",
            "not start TeSt next3lines end",
            "1 11 111",
            "2 22 222",
            "3 33 three333",
            "TWO three seventeen end not"};

    private static String exampleText;

    static {
        StringBuilder builder = new StringBuilder();
        for(String line : lines) {
            builder.append(line);
            builder.append("\n");
        }
        exampleText = builder.toString();
    }

    private final InputStream in = new ByteArrayInputStream(exampleText.getBytes(StandardCharsets.UTF_8));
    private final GrepCommand grep = new GrepCommand(new GrepOptionsParserJCommander());

    private String getGrepResult(String[] args) {
        return Utils.getResult(args, in, grep);
    }

    @Test
    public void testGrepSimple() {
        String[] args = {"seven"};
        String result = getGrepResult(args);
        assertEquals(result, lines[0]+"\n"+lines[5]+"\n");
    }

    @Test
    public void testGrepIgnoreCase() {
        String[] args = {"-i", "TEst"};
        String result = getGrepResult(args);
        assertEquals(result, lines[1]+"\n");
    }

    @Test
    public void testGrepStartWord() {
        String[] args = {"^start"};
        String result = getGrepResult(args);
        assertEquals(result, lines[0]+"\n");
    }

    @Test
    public void testGrepEndWord() {
        String[] args = {"end$"};
        String result = getGrepResult(args);
        assertEquals(result, lines[1]+"\n");
    }

    @Test
    public void testGrepAloneWord() {
        String[] args = {"-w", "seven"};
        String result = getGrepResult(args);
        assertEquals(result, lines[0]+"\n");
    }

    @Test
    public void testGrepAfterLines() {
        String[] args = {"-A", "3", "next3lines"};
        String result = getGrepResult(args);
        assertEquals(result, lines[2]+"\n" + lines[3]+"\n" + lines[4]+"\n");
    }
}
