package dev.pdml.utils.parser;

import dev.pp.text.reader.CharReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PdmlParserUtilsTest {

    @Test
    void createVirtualRootReader() throws IOException  {

        CharReader reader = PdmlParserUtils.createVirtualRootReader (
            "config", new StringReader ( "[n1 v1]" ), null, null, null );

        assertEquals ( "[config [n1 v1]]", reader.readRemaining() );
    }
}
