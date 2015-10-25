package ru.spbau.komarov.sd_01;

import org.junit.Test;
import ru.spbau.komarov.sd_01.commands.WcCommand;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class WcTest {
    private String exampleText =
                    "hello\n" +
                    "the end\n";
    private final InputStream in = new ByteArrayInputStream(exampleText.getBytes(StandardCharsets.UTF_8));
    private final WcCommand wcCommand = new WcCommand();

    private String getWcResult(String[] args) {
        return Utils.getResult(args, in, wcCommand);
    }

    @Test
    public void testWc() {
        String[] args = {};
        String result = getWcResult(args);
        assertEquals(result, "2 3 12\n");
    }
}
