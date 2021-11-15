package dev.pdml.core.reader.reader;

import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.exception.PXMLResourceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PXMLReaderTest {

    @Test
    public void testNodeNames() throws PXMLResourceException {

        PXMLReader r = new DefaultPXMLReader ( "div item_12 _ab1-c.3 a A _ 1a AB* xm xml XML1" );

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

        PXMLReader r = new DefaultPXMLReader( "text text 123" );
        assertEquals ("text text 123", r.readText() );

        // text limiters
        r = new DefaultPXMLReader( "text 123 [qwe][" );
        assertEquals ("text 123 ", r.readText());
        assertTrue ( r.skipChar ( '[' ) );
        assertEquals ("qwe", r.readText() );
        assertNull ( r.readText() );
    }

    @Test
    public void testTextEscapes() throws Exception {

        PXMLReader r = new DefaultPXMLReader( "\\t" );
        assertEquals ("\t", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\u0048" );
        assertEquals ("H", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\U00000048" );
        assertEquals ("H", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "1\\u0048\\r\\n2" );
        assertEquals ("1H\r\n2", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "1\\U00000048\\r\\n2" );
        assertEquals ("1H\r\n2", r.readText() );
        assertFalse ( r.hasChar () );

        // 111\[\]222\t333\r\n444\\555
        r = new DefaultPXMLReader( "111\\[\\]222\\t333\\r\\n444\\\\555" );
        assertEquals ("111[]222\t333\r\n444\\555", r.readText() );
        assertFalse ( r.hasChar () );

        // Unicode escape sequences
        r = new DefaultPXMLReader( "\\u0048\\u0065ll\\u006f" );
        assertEquals ("Hello", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\U00000048\\u0065ll\\U0000006f" );
        assertEquals ("Hello", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\U0001F600" ); // smiley U+1F600
        assertEquals ("\uD83D\uDE00", r.readText() ); // 16 bits encoding takes two chars
        // assertEquals ("😃", r.readText() );
        assertFalse ( r.hasChar () );

        // invalid escapes

        r = new DefaultPXMLReader( "4\\5" );
        assertEquals ( "4", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\" );
        Exception exception = assertThrows ( MalformedPXMLDocumentException.class, r::readText );
        System.err.println ( exception.toString() );

        r = new DefaultPXMLReader( "\\u00az" ); // z is invalid
        assertEquals ( "\u00a0", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\U000000az" );
        assertEquals ( "\u00a0", r.readText() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\\u00a" ); // only 3 hex digits
        exception = assertThrows ( MalformedPXMLDocumentException.class, r::readText );
        System.err.println ( exception.toString() );

        r = new DefaultPXMLReader( "\\U000000a" ); // only 7 hex digits
        exception = assertThrows ( MalformedPXMLDocumentException.class, r::readText );
        System.err.println ( exception.toString() );

        r = new DefaultPXMLReader( "\\u" );
        exception = assertThrows ( MalformedPXMLDocumentException.class, r::readText );
        System.err.println ( exception.toString() );

        r = new DefaultPXMLReader( "\\U" );
        exception = assertThrows ( MalformedPXMLDocumentException.class, r::readText );
        System.err.println ( exception.toString() );
    }

    @Test
    public void testUnquotedAttributeValue() throws TextReaderException {

        PXMLReader r = new DefaultPXMLReader( "[abc" );
        assertNull ( r.readUnquotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readUnquotedAttributeValue() );
        assertNull ( r.readUnquotedAttributeValue() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "a abc C:\\foo\\bar.txt" );
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
    public void testQuotedAttributeValue() throws TextReaderException {

        PXMLReader r = new DefaultPXMLReader( " \"abc\"" );
        assertNull ( r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readQuotedAttributeValue() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        r = new DefaultPXMLReader( "\"a\" \"abc\" \"a b\" \"a b \\\" ' \\u0048\\u0065ll\\U0000006f\"" );
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
        r = new DefaultPXMLReader( "\"a" );
        Exception e = assertThrows ( MalformedPXMLDocumentException.class, r::readQuotedAttributeValue );
        System.err.println ( e.toString() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // invalid escape \y
        r = new DefaultPXMLReader( "\"ab\\ycd\"" );
        assertEquals ("abcd", r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // quoted with '
        r = new DefaultPXMLReader( "'a' 'abc' 'a b \" \\' c '" );
        assertEquals ("a", r.readQuotedAttributeValue() );
        assertNull ( r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("abc", r.readQuotedAttributeValue() );
        r.advanceChar ();
        assertEquals ("a b \" ' c ", r.readQuotedAttributeValue() );
        // assertNull ( r.readQuotedAttributeValue() );
        assertFalse ( r.hasChar () );

        // unclosed
        r = new DefaultPXMLReader( "'a" );
        e = assertThrows ( MalformedPXMLDocumentException.class, r::readQuotedAttributeValue );
        System.err.println ( e.toString() );
    }


    @Test
    public void testAttributeValue() throws TextReaderException {

        PXMLReader r = new DefaultPXMLReader( "a 'ab' \"abc\"" );
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
    public void testComments() throws PXMLResourceException, MalformedPXMLDocumentException {

        PXMLReader r = new DefaultPXMLReader("[- comment -]" );
        assertEquals("[- comment -]", r.readComment());

        r = new DefaultPXMLReader("[- [name value-\\]] -]" );
        assertEquals("[- [name value-\\]] -]", r.readComment());

        r = new DefaultPXMLReader("   [-a-]qwe" );
        r.skipOptionalSpacesAndTabsAndNewLines ();
        assertEquals("[-a-]", r.readComment());

        r = new DefaultPXMLReader("[--]qwe" );
        assertEquals("[--]", r.readComment());

        // nested
        r = new DefaultPXMLReader("[- comment [- nested -] -]" );
        assertEquals("[- comment [- nested -] -]", r.readComment());

        r = new DefaultPXMLReader("[-[--]-]" );
        assertEquals("[-[--]-]", r.readComment());
    }
}
