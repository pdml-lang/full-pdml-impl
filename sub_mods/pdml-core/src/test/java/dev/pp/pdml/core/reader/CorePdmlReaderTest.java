package dev.pp.pdml.core.reader;

import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class CorePdmlReaderTest {

    @Test
    void generalTest() throws IOException, PdmlException {

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
    void readTag() throws IOException, PdmlException {

        expectTag ( "tag1 ", "tag1" );
        expectTag ( "tag_2] ", "tag_2" );
        expectTag ( "2025-01-07] ", "2025-01-07" );
        expectTag ( "tag_.-] ", "tag_.-" );
        expectTag ( "_]", "_" );
        expectTag ( "คุณภาพ]", "คุณภาพ" );


        // Escape sequences
        expectTag ( "tag_3\\]] ", "tag_3]" );
        expectTag ( "tag\\s4] ", "tag 4" );
        expectTag (
            "\\[\\]\\s\\t\\n\\r\\f\\^\\(\\)\\=\\\"\\~\\|\\:\\,\\`\\!\\$\\\\]",
            "[] \t\n\r\f^()=\"~|:,`!$\\" );

        CorePdmlReader reader = createReader ( "]" );
        assertNull ( reader.readTag() );

        // Invalid
        expectInvalidTag ( "tag|" ); // invalid char |
        expectInvalidTag ( "tag\\m]" ); // invalid escape char \m

        // Invalid Unicode control code points
        expectInvalidTag ( "tag\u0000]" );
        expectInvalidTag ( "tag\u001F]" );
        expectInvalidTag ( "tag\u0080]" );
        expectInvalidTag ( "tag\u009F]" );

        // Invalid Unicode surrogate code points
        // expectInvalidTag ( "tag\uD800]" );
        // expectInvalidTag ( "tag\uDFFF]" );
    }

    @Test
    void readSeparator() throws IOException, PdmlException {

        expectSeparator ( " ", " " );
        expectSeparator ( "\t", "\t" );
        expectSeparator ( "\n", "\n" );
        expectSeparator ( "\r\n", "\r\n" );

        expectSeparator ( "  ", " " );
        expectSeparator ( "\r\n\n", "\r\n" );

        expectSeparator ( "a", null );
        expectSeparator ( "\\s", null );
    }

    @Test
    void readText() throws IOException, PdmlException {

        expectText ( "text1[", "text1" );
        expectText ( "text2]", "text2" );
        expectText ( "123 _.- คุณภาพ]", "123 _.- คุณภาพ" );

        expectText (
            "\\[\\] \t\n\r\f\\^()=\"~|:,`!$\\\\]",
            "[] \t\n\r\f^()=\"~|:,`!$\\" );

        // Escape sequences
        expectText ( "text_3\\]] ", "text_3]" );
        expectText ( "text\\s4] ", "text 4" );
        expectText (
            "\\[\\]\\s\\t\\n\\r\\f\\^\\(\\)\\=\\\"\\~\\|\\:\\,\\`\\!\\$\\\\]",
            "[] \t\n\r\f^()=\"~|:,`!$\\" );

        CorePdmlReader reader = createReader ( "]" );
        assertNull ( reader.readText() );

        // Invalid
        expectInvalidText ( "text\\m]" ); // invalid escape char \m

        // Invalid Unicode control code points
        expectInvalidText ( "text\u0000]" );
        expectInvalidText ( "text\u001F]" );
        expectInvalidText ( "text\u0080]" );
        expectInvalidText ( "text\u009F]" );

        // Invalid Unicode surrogate code points
        // expectInvalidText ( "text\uD800]" );
        // expectInvalidText ( "text\uDFFF]" );
    }

    // Helpers

    private void expectTag ( String code, String expectedTag ) throws IOException, PdmlException {

        CorePdmlReader reader = createReader ( code );
        assertEquals ( expectedTag, reader.readTag() );
    }

    private void expectSeparator ( String code, String expectedResult ) throws IOException, PdmlException {

        CorePdmlReader reader = createReader ( code );
        assertEquals ( expectedResult, reader.readSeparator() );
    }

    private void expectText ( String code, String expectedText ) throws IOException, PdmlException {

        CorePdmlReader reader = createReader ( code );
        assertEquals ( expectedText, reader.readText() );
    }

    private void expectInvalidTag ( String code ) throws IOException {

        CorePdmlReader reader = createReader ( code );
        assertThrows ( MalformedPdmlException.class, reader::readTag );
    }

    private void expectInvalidText ( String code ) throws IOException {

        CorePdmlReader reader = createReader ( code );
        assertThrows ( MalformedPdmlException.class, reader::readText );
    }

    private @NotNull CorePdmlReader createReader ( @NotNull String code ) throws IOException {
        StringReader stringReader = new StringReader ( code );
        return new CorePdmlReader ( stringReader );
    }
}
