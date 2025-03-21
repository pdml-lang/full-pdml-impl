package dev.pp.pdml.utils.parser;

import dev.pp.core.text.reader.CharReader;
import dev.pp.pdml.utils.parser.VirtualRootUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VirtualRootHelperTest {

    @Test
    void createVirtualRootReader() throws IOException  {

        CharReader reader = VirtualRootUtil.createVirtualRootReader (
            "config", new StringReader ( "[n1 v1]" ), null, null, null );

        assertEquals ( "[config [n1 v1]]", reader.readRemaining() );
    }
}
