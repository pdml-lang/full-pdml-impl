package dev.pdml.reader;

import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.text.inspection.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class PdmlReaderTest {

    private @NotNull PdmlReader getReader ( @NotNull String code ) throws IOException {
        StringReader stringReader = new StringReader ( code );
        return new PdmlReaderImpl ( stringReader );
    }

    @Test
    public void testNodeNames() throws IOException {

        PdmlReader r = getReader ( "div item_12 _ab1-c.3 a A _ 1a AB* xm xml XML1" );

        assertEquals ( "div", r.readName() );
        assertTrue ( r.isAtChar ( ' ' ) );
        r.skipOptionalSpacesAndTabsAndNewLines ();
        assertTrue ( r.isAtChar ( 'i' ) );

        assertEquals ( "item_12", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "_ab1-c.3", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "a", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "A", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "_", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertNull ( r.readName() ); // 1a is invalid
        assertTrue ( r.skipChar ( '1' ) );
        assertTrue ( r.skipChar ( 'a' ) );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "AB", r.readName() ); // "*" is invalid
        assertTrue ( r.skipChar ( '*' ) );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        /*
        PXMLReaderErrorHandler errorHandler = r.getConfig().getErrorHandler();
        assertEquals ( "a", r.readName() );
        assertEquals ( 1, errorHandler.errorCount() );
        r.skipWhiteSpace();
        assertTrue ( r.isAtChar ( 'A' ) );

        assertEquals ( "AB", r.readName() );
        assertEquals ( 2, errorHandler.errorCount() );
        r.skipWhiteSpace();
        */

        /*
        assertThrows ( PXMLDataException.class, t::readName );
        t.skipWhiteSpace();

        Exception exception = assertThrows ( PXMLDataException.class, t::readName );
        // System.out.println ( exception.toString() );
        assertTrue ( exception.getMessage().contains ( "cannot start with \"xml\"" ) );
        */

        assertEquals ( "xm", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "xml", r.readName() );
        r.skipOptionalSpacesAndTabsAndNewLines ();

        assertEquals ( "XML1", r.readName() );
    }

    @Test
    public void testText() throws Exception {

        PdmlReader r = getReader ( "text text 123" );
        assertEquals ("text text 123", r.readText() );

        // text limiters
        r = getReader ( "text 123 [qwe][" );
        assertEquals ("text 123 ", r.readText());
        assertTrue ( r.skipChar ( '[' ) );
        assertEquals ("qwe", r.readText() );
        assertNull ( r.readText() );
    }

    @Test
    public void testTextEscapes() throws Exception {

        PdmlReader r = getReader ( "\\t" );
        assertEquals ("\t", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\u0048" );
        assertEquals ("H", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\U00000048" );
        assertEquals ("H", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "1\\u0048\\r\\n2" );
        assertEquals ("1H\r\n2", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "1\\U00000048\\r\\n2" );
        assertEquals ("1H\r\n2", r.readText() );
        assertFalse ( r.hasChar () );

        // 111\[\]222\t333\r\n444\\555
        r = getReader ( "111\\[\\]222\\t333\\r\\n444\\\\555" );
        assertEquals ("111[]222\t333\r\n444\\555", r.readText() );
        assertFalse ( r.hasChar () );

        // Unicode escape sequences
        r = getReader ( "\\u0048\\u0065ll\\u006f" );
        assertEquals ("Hello", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\U00000048\\u0065ll\\U0000006f" );
        assertEquals ("Hello", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\U0001F600" ); // smiley U+1F600
        assertEquals ("\uD83D\uDE00", r.readText() ); // 16 bits encoding takes two chars
        // assertEquals ("😃", r.readText() );
        assertFalse ( r.hasChar () );

        // invalid escapes

        r = getReader ( "4\\5" );
        assertEquals ( "4", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\" );
        Exception exception = assertThrows ( PdmlDocumentSyntaxException.class, r::readText );
        System.err.println ( exception.toString() );

        r = getReader ( "\\u00az" ); // z is invalid
        assertEquals ( "\u00a0", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\U000000az" );
        assertEquals ( "\u00a0", r.readText() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\\u00a" ); // only 3 hex digits
        exception = assertThrows ( PdmlDocumentSyntaxException.class, r::readText );
        System.err.println ( exception.toString() );

        r = getReader ( "\\U000000a" ); // only 7 hex digits
        exception = assertThrows ( PdmlDocumentSyntaxException.class, r::readText );
        System.err.println ( exception.toString() );

        r = getReader ( "\\u" );
        exception = assertThrows ( PdmlDocumentSyntaxException.class, r::readText );
        System.err.println ( exception.toString() );

        r = getReader ( "\\U" );
        exception = assertThrows ( PdmlDocumentSyntaxException.class, r::readText );
        System.err.println ( exception.toString() );
    }

    @Test
    public void testUnquotedAttributeValue() throws IOException, TextErrorException {

        PdmlReader r = getReader ( "[abc" );
        assertNull ( r.readUnquotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readUnquotedAttributeValue() );
        assertNull ( r.readUnquotedAttributeValue() );
        assertFalse ( r.hasChar () );

        r = getReader ( "a abc C:\\foo\\bar.txt" );
        assertEquals ("a", r.readUnquotedAttributeValue() );
        assertNull ( r.readUnquotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readUnquotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("C:\\foo\\bar.txt", r.readUnquotedAttributeValue() );
        assertNull ( r.readUnquotedAttributeValue() );
        assertFalse ( r.hasChar () );
    }

    @Test
    public void testQuotedAttributeValue() throws IOException, TextErrorException {

        PdmlReader r = getReader ( " \"abc\"" );
        assertNull ( r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readQuotedAttributeValue() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        r = getReader ( "\"a\" \"abc\" \"a b\" \"a b \\\" ' \\u0048\\u0065ll\\U0000006f\"" );
        assertEquals ("a", r.readQuotedAttributeValue() );
        assertNull ( r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("a b", r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("a b \" ' Hello", r.readQuotedAttributeValue() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // unclosed
        r = getReader ( "\"a" );
        Exception e = assertThrows ( PdmlDocumentSyntaxException.class, r::readQuotedAttributeValue );
        System.err.println ( e.toString() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // invalid escape \y
        r = getReader ( "\"ab\\ycd\"" );
        assertEquals ("abcd", r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // quoted with '
        r = getReader ( "'a' 'abc' 'a b \" \\' c '" );
        assertEquals ("a", r.readQuotedAttributeValue() );
        assertNull ( r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("a b \" ' c ", r.readQuotedAttributeValue() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // unclosed
        r = getReader ( "'a" );
        e = assertThrows ( PdmlDocumentSyntaxException.class, r::readQuotedAttributeValue );
        System.err.println ( e.toString() );
    }


    @Test
    public void testAttributeValue() throws IOException, TextErrorException {

        PdmlReader r = getReader ( "a 'ab' \"abc\"" );
        assertEquals ("a", r.readAttributeValue() );
        assertNull ( r.readAttributeValue() );
        r.advanceChar ();
        assertEquals ("ab", r.readAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readAttributeValue() );
        // assertNull ( r.readAttributeValue() );
        assertFalse ( r.hasChar () );
    }

    @Test
    public void testComments() throws IOException, PdmlDocumentSyntaxException {

        PdmlReader r = getReader ("[- comment -]" );
        assertEquals("[- comment -]", r.readComment());

        r = getReader ("[- [name value-\\]] -]" );
        assertEquals("[- [name value-\\]] -]", r.readComment());

        r = getReader ("   [-a-]qwe" );
        r.skipOptionalSpacesAndTabsAndNewLines ();
        assertEquals("[-a-]", r.readComment());

        r = getReader ("[--]qwe" );
        assertEquals("[--]", r.readComment());

        // nested
        r = getReader ("[- comment [- nested -] -]" );
        assertEquals("[- comment [- nested -] -]", r.readComment());

        r = getReader ("[-[--]-]" );
        assertEquals("[-[--]-]", r.readComment());
    }
}
