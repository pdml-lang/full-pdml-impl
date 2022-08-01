package dev.pdml.ext.utilities.parser;

import dev.pp.text.reader.CharReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class PDMLParserUtilsTest {

    @Test
    void createVirtualRootReader() throws IOException  {

        CharReader reader = PDMLParserUtils.createVirtualRootReader (
            "config", new StringReader ( "[n1 v1]" ), null );

        assertEquals ( "[config [n1 v1]]", reader.readRemaining() );
    }
}