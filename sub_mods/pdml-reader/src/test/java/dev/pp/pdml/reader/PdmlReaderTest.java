package dev.pp.pdml.reader;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class PdmlReaderTest {

    @Test
    void isAtNodeStart() throws IOException, PdmlException {

        PdmlReader reader = createReader ( "[" );
        assertTrue ( reader.isAtNodeStart() );

        reader = createReader ( "[tag" );
        assertTrue ( reader.isAtNodeStart() );

        reader = createReader ( "foo" );
        assertFalse ( reader.isAtNodeStart() );
    }

    @Test
    void readMultilineComment() throws IOException, PdmlException {

        PdmlReader reader = createReader ( "^/* comment */" );
        assertEquals ("^/* comment */", reader.readMultilineComment() );

        reader = createReader ( "^/*comment*/" );
        assertEquals ("^/*comment*/", reader.readMultilineComment() );

        reader = createReader ( "^/**comment**/" );
        assertEquals ("^/**comment**/", reader.readMultilineComment() );

        reader = createReader ( "^/** */ **/" );
        assertEquals ("^/** */ **/", reader.readMultilineComment() );

        reader = createReader ( "^/* ^/* nested */ */" );
        assertEquals ("^/* ^/* nested */ */", reader.readMultilineComment() );

        reader = createReader ( "^/** ^/* nested */ */ **/" );
        assertEquals ("^/** ^/* nested */ */ **/", reader.readMultilineComment() );

        reader = createReader ( "^/* ^/* nested 1 ^/* nested 2 */ */ end */" );
        assertEquals ("^/* ^/* nested 1 ^/* nested 2 */ */ end */", reader.readMultilineComment() );

        // Invalid

        reader = createReader ( "^/* comment ]" );
        assertThrows ( PdmlException.class, reader::readMultilineComment );
    }

/*
    @Test
    public void readAttributeValue() throws Exception {

        PdmlReader reader = createReader ( "a ab \"abc\"" );
        assertEquals ("a", reader.readAttributeValue() );
        assertNull ( reader.readAttributeValue() );
        reader.advanceChar ();
        assertEquals ("ab", reader.readAttributeValue() );
        reader.advanceChar ();
        assertEquals ("abc", reader.readAttributeValue() );
        // assertNull ( r.readAttributeValue() );
        assertTrue ( reader.isAtEnd() );
    }
 */

    @Test
    void testSetMark() throws IOException, PdmlException {

        String code = "[root    ^\"a\"]";
        try ( StringReader sr = new StringReader ( code ) ) {
            PdmlReader pr = new PdmlReader ( sr );
            assert pr.readNodeStart();
            pr.readTag ();
            assertEquals ( " ", pr.readSeparator() );
        }
    }

    private @NotNull PdmlReader createReader ( @NotNull String code ) throws IOException {
        StringReader stringReader = new StringReader ( code );
        return new PdmlReader ( stringReader );
    }
}
