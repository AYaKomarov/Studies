package ru.spbau.komarov.sd_01;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import ru.spbau.komarov.sd_01.commands.CatCommand;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class CatTest {
    private String exampleText =
                    "Text\n" +
                    "One line\n" +
                    "Two line\n" +
                    "The End\n";
    private final InputStream in = new ByteArrayInputStream(exampleText.getBytes(StandardCharsets.UTF_8));
    private final CatCommand catCommand = new CatCommand();

    private String getCatResult(String[] args) throws IOException, ParseException {
        return Utils.getResult(args, in, catCommand);
    }

    @Test
    public void testCat() throws Exception{
        String[] args = {};
        String result = getCatResult(args);
        assertEquals(result, exampleText);
    }
}
