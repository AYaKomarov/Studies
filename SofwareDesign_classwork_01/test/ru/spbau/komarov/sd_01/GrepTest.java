package ru.spbau.komarov.sd_01;

import com.beust.jcommander.ParameterException;
import org.apache.commons.cli.ParseException;
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
    private final GrepCommand jGrep = new GrepCommand(new GrepOptionsParserJCommander());

    private String getGrepResult(String[] args) throws IOException, ParseException {
        return Utils.getResult(args, in, jGrep);
    }

    private String getGrepResult(String[] args, InputStream in) throws IOException, ParseException {
        return Utils.getResult(args, in, jGrep);
    }

    @Test
    public void testGrepSimple() throws Exception {
        String[] args = {"seven"};
        String result = getGrepResult(args);
        assertEquals(result, lines[0]+"\n"+lines[5]+"\n");
    }

    @Test
    public void testGrepIgnoreCase() throws Exception {
        String[] args = {"-i", "TEst"};
        String result = getGrepResult(args);
        assertEquals(result, lines[1]+"\n");
    }

    @Test
    public void testGrepStartWord() throws Exception {
        String[] args = {"^start"};
        String result = getGrepResult(args);
        assertEquals(result, lines[0]+"\n");
    }

    @Test
    public void testGrepEndWord() throws Exception {
        String[] args = {"end$"};
        String result = getGrepResult(args);
        assertEquals(result, lines[1]+"\n");
    }

    @Test
    public void testGrepAloneWord() throws Exception {
        String[] args = {"-w", "seven"};
        String result = getGrepResult(args);
        assertEquals(result, lines[0]+"\n");
    }

    @Test
    public void testGrepAfterLines() throws Exception {
        String[] args = {"-A", "3", "next3lines"};
        String result = getGrepResult(args);
        assertEquals(result, lines[2]+"\n" + lines[3]+"\n" + lines[4]+"\n");
    }

    @Test(expected= IOException.class)
    public void testNotCorrectFilePath() throws Exception{
        String[] args = {"123", "notCorrectFilePath"};
        getGrepResult(args, null);
    }

    @Test(expected = ParameterException.class)
    public void testNotCorrectOptionsKey() throws Exception{
        String[] args = {"-BadKey", "a123"};
        getGrepResult(args);
    }

    @Test(expected = ParameterException.class)
    public void testNotCorrectOptionsInt() throws Exception{
        String[] args = {"-A", "seven", "a123"};
        getGrepResult(args);
    }

    @Test(expected = ParameterException.class)
    public void testNotCorrectOptionsDoubleA() throws Exception{
        String[] args = {"-A", "3", "-A", "2", "next3lines"};
        getGrepResult(args);
    }

    @Test(expected = ParameterException.class)
    public void testNotCorrectOptionsNegativeInt() throws Exception{
        String[] args = {"-A", "-3", "next3lines"};
        getGrepResult(args);
    }
}
