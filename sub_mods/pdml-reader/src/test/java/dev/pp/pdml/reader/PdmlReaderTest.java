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

/* TODO? move tests to parser

    @Test
    void readStringLiteral() throws IOException, PdmlException {

        readStringLiteral ( "unquoted", "unquoted" );
        readStringLiteral ( "unquoted ", "unquoted" );
        readStringLiteral ( "\"quoted\"", "quoted" );
        readStringLiteral ( "\"quoted\" ", "quoted" );
        readStringLiteral ( "~|raw|~", "raw" );
        readStringLiteral ( "~|raw~|~", "raw~" );
        readStringLiteral ( "~|raw||~", "raw|" );
        readStringLiteral ( "~~|raw|~|~~", "raw|~" );
        readStringLiteral ( """
            \"\"\"
            multi
            line
            \"\"\"
            """, "multi\nline" );

        // Empty Value
        readStringLiteral ( " ", null );
        readStringLiteral ( "\"\"", "" );
        readStringLiteral ( "~||~", "" );
        readStringLiteral ( """
            \"\"\"
            \"\"\"
            """, "" );

        // Escape Sequences
        readStringLiteral ( "unquoted\\\\\\[\\]\\s\\n\\^", "unquoted\\[] \n^" );
        readStringLiteral ( "\"quoted\\\\[] \\n*\"", "quoted\\[] \n*" );
        readStringLiteral ( "~|raw\\[] \n*|~", "raw\\[] \n*" );
        readStringLiteral ( """
            \"\"\"
            multi
            line\\[] *
            \"\"\"
            """, "multi\nline\\[] *" );

        // Unicode Escape Sequences
        readStringLiteral ( "unquoted\\u0041\\u{42}\\u{43 44}", "unquotedABCD" );
        readStringLiteral ( "\"quoted\\u0041\\u{42}\\u{43 44}\"", "quotedABCD" );
        readStringLiteral ( "~|raw\\u0041\\u{42}\\u{43 44}|~", "raw\\u0041\\u{42}\\u{43 44}" );
        readStringLiteral ( """
            \"\"\"
            multi
            line\\u0041\\u{42}\\u{43 44}
            \"\"\"
            """, "multi\nline\\u0041\\u{42}\\u{43 44}" );

        // Utility Nodes
        // cannot be tested here, because reader has no extension handler
        // readStringLiteral ( "unquoted[u:exp 1 + 1]3", "unquoted23" );
    }

    private void readStringLiteral ( String pdmlCode, String expected ) throws IOException, PdmlException {

        PdmlReader reader = createReader ( pdmlCode );
        if ( expected != null ) {
            assertEquals ( expected, reader.readEmptyableStringLiteral ( PdmlReader.ExtensionInitiatorKind.STRING_LITERAL ) );
        } else {
            assertNull ( reader.readEmptyableStringLiteral ( PdmlReader.ExtensionInitiatorKind.STRING_LITERAL ) );
        }
    }

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
