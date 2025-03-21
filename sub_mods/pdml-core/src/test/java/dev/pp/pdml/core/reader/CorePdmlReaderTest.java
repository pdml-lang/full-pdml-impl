package dev.pp.pdml.core.reader;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class CorePdmlReaderTest {

    @Test
    void test() throws IOException, PdmlException {

        CorePdmlReader reader = createReader ( "[root [child\r\nfoo bar]]" );
        assertTrue ( reader.readNodeStart() );
        assertEquals ( "root", reader.readTag () );
        assertEquals ( " ", reader.readSeparator() );
        assertTrue ( reader.readNodeStart() );
        assertEquals ( "child", reader.readTag () );
        assertEquals ( "\r\n", reader.readSeparator() );
        assertEquals ( "foo bar", reader.readText() );
        assertTrue ( reader.readNodeEnd() );
        assertTrue ( reader.readNodeEnd() );
        assertTrue ( reader.isAtEnd() );
    }

    @Test
    void readName() throws IOException, PdmlException {

        CorePdmlReader reader = createReader ( "name1" );
        assertEquals ( "name1", reader.readTag () );

        reader = createReader ( "name_2[" );
        assertEquals ( "name_2", reader.readTag () );

        reader = createReader ( "name_2 " );
        assertEquals ( "name_2", reader.readTag () );

        reader = createReader ( "tag\\s3\\\\a\\[b\\]\\^]" );
        assertEquals ( "tag 3\\a[b]^", reader.readTag () );

        reader = createReader ( "[" );
        assertNull ( reader.readTag () );

        reader = createReader ( "" );
        assertNull ( reader.readTag () );

        reader = createReader ( "tag\\4" );
        assertThrows ( PdmlException.class, reader::readTag );
        // assertEquals ( "tag?4", reader.readNodeName() );
    }

    @Test
    void readText() throws IOException, PdmlException {

        CorePdmlReader reader = createReader ("text 1\r\n" );
        assertEquals ( "text 1\r\n", reader.readText() );

        reader = createReader ("text \\\\ \\[2\\]\\t\\n\\r]" );
        assertEquals ( "text \\ [2]\t\n\r", reader.readText() );

        reader = createReader ("[foo]" );
        assertNull ( reader.readText() );

        reader = createReader ("text \\4" );
        assertThrows ( PdmlException.class, reader::readText );
        // assertEquals ( "text ?4", reader.readText() );
    }

    private @NotNull CorePdmlReader createReader ( @NotNull String code ) throws IOException {
        StringReader stringReader = new StringReader ( code );
        return new CorePdmlReader ( stringReader );
    }
}
