package dev.pp.text.reader;

import dev.pp.text.utilities.character.CharChecks;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CharReaderTest {

    private static CharReader getDefaultReader ( String s ) {

        try {
            return new DefaultCharReader ( s );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }

    // iteration

    @Test
    public void testIteration() throws IOException {
        testIteration_ ( CharReaderTest::getDefaultReader );
    }

    public static void testIteration_ ( Function<String, CharReader> iteratorGetter ) throws IOException {

        CharReader r = iteratorGetter.apply ( "abc" );

        assertTrue ( r.hasChar () );
        assertEquals ( 'a', r.currentChar () );
        assertTrue ( r.isAtChar ( 'a' ) );
        assertEquals ( 1, r.currentLineNumber () );
        assertEquals ( 1, r.currentColumnNumber () );

        // assertEquals ( 'a', r.advance () );
        r.advance();
        assertTrue ( r.hasChar () );
        assertTrue ( r.isAtChar ( 'b' ) );
        assertEquals ( 1, r.currentLineNumber () );
        assertEquals ( 2, r.currentColumnNumber () );

        // assertEquals ( 'b', r.advance () );
        r.advance();
        assertTrue ( r.hasChar () );
        assertTrue ( r.isAtChar ( 'c' ) );
        assertEquals ( 1, r.currentLineNumber () );
        assertEquals ( 3, r.currentColumnNumber () );

        // assertEquals ( 'c', r.advance () );
        r.advance();
        assertFalse ( r.hasChar () );
        // assertTrue ( r.isNext ( 0 );
        assertEquals ( 1, r.currentLineNumber () );
        assertEquals ( 4, r.currentColumnNumber () );

        // must be commented if assertions are disabled
        assertThrows ( NoSuchElementException.class, r::advance );


        r = iteratorGetter.apply ( "abc" );
        StringBuilder sb = new StringBuilder();
        while ( r.hasChar () ) {
            sb.append ( r.currentChar() );
            r.advance();
        }
        assertEquals ( "abc", sb.toString() );


        r = iteratorGetter.apply ( "" );
        assertFalse ( r.hasChar () );
        assertEquals ( 1, r.currentLineNumber () );
        assertEquals ( 1, r.currentColumnNumber () );

        // must be commented if assertions are disabled
        // assertThrows ( NoSuchElementException.class, r::advance );
        assertThrows ( Throwable.class, r::advance );


        r = iteratorGetter.apply ( "" );
        sb = new StringBuilder();
        while ( r.hasChar () ) {
            sb.append ( r.currentChar() );
            r.advance();
        }
        assertEquals ( "", sb.toString() );
    }


    // location

    @Test
    public void testLocation() throws IOException {
        testLocation_ ( CharReaderTest::getDefaultReader );
    }

    public static void testLocation_ ( Function<String, CharReader> readerGetter ) throws IOException {

        CharReader r = readerGetter.apply ( "12\n45\r\n89\n\nAB" );

        assertEquals ( '1', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );
        assertEquals ( 1, r.currentLocation().getLineNumber() );
        assertEquals ( 1, r.currentLocation().getColumnNumber() );

        r.advance();
        assertEquals ( '2', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '\n', r.currentChar() );
        assertEquals ( '\n', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 3, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '4', r.currentChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '5', r.currentChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '\r', r.currentChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 3, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '\n', r.currentChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 4, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '8', r.currentChar() );
        assertEquals ( 3, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '9', r.currentChar() );
        assertEquals ( 3, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '\n', r.currentChar() );
        assertEquals ( 3, r.currentLineNumber() );
        assertEquals ( 3, r.currentColumnNumber() );

        r.advance();
        assertEquals ( '\n', r.currentChar() );
        assertEquals ( 4, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );

        r.advance();
        assertEquals ( 'A', r.currentChar() );
        assertEquals ( 5, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );

        r.advance();
        assertEquals ( 'B', r.currentChar() );
        assertEquals ( 5, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );

        r.advance();
        assertFalse ( r.hasChar() );
        assertEquals ( 5, r.currentLineNumber() );
        assertEquals ( 3, r.currentColumnNumber() );
        assertEquals ( 5, r.currentLocation().getLineNumber() );
        assertEquals ( 3, r.currentLocation().getColumnNumber() );


        r = readerGetter.apply ( "\n2" );

        assertTrue ( r.hasChar() );
        assertEquals ( '\n', r.currentChar() );
        assertEquals ( '2', r.peekNextChar () );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );
        assertEquals ( 1, r.currentLocation().getLineNumber() );
        assertEquals ( 1, r.currentLocation().getColumnNumber() );

        r.advance();
        assertTrue ( r.hasChar() );
        assertEquals ( '2', r.currentChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );
        assertEquals ( 2, r.currentLocation().getLineNumber() );
        assertEquals ( 1, r.currentLocation().getColumnNumber() );

        r.advance();
        assertFalse ( r.hasChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );

        r = readerGetter.apply ( "" );

        assertFalse ( r.hasChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );
        assertEquals ( 1, r.currentLocation().getLineNumber() );
        assertEquals ( 1, r.currentLocation().getColumnNumber() );

        r = readerGetter.apply ( "1" );

        assertTrue ( r.hasChar() );
        r.advance();
        assertFalse ( r.hasChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );
        assertEquals ( 1, r.currentLocation().getLineNumber() );
        assertEquals ( 2, r.currentLocation().getColumnNumber() );
    }


    // isAt

    @Test
    public void testisAtFunctions() throws IOException {
        testisAtFunctions_ ( CharReaderTest::getDefaultReader );
    }

    public static void testisAtFunctions_ ( Function<String, CharReader> readerGetter ) throws IOException {

        CharReader r = readerGetter.apply ( "1234" );

        assertTrue ( r.isAtChar ( '1' ) );
        assertFalse ( r.isAtChar ( '2' ) );

        // assertTrue ( r.isNextChar ( '2' ) );
        // assertFalse ( r.isNextChar ( '1' ) );

        assertTrue ( r.isAtString ( "1" ) );
        assertTrue ( r.isAtString ( "12" ) );
        assertTrue ( r.isAtString ( "123" ) );
        assertTrue ( r.isAtString ( "1234" ) );

        assertFalse ( r.isAtString ( "2" ) );
        assertFalse ( r.isAtString ( "22" ) );
        assertFalse ( r.isAtString ( "13" ) );
        assertFalse ( r.isAtString ( "1235" ) );
        assertFalse ( r.isAtString ( "12345" ) );
        assertFalse ( r.isAtString ( "123456" ) );

        r.advance();
        r.advance();
        r.advance();

        assertTrue ( r.isAtChar ( '4' ) );
        assertFalse ( r.isAtChar ( '2' ) );

        // assertTrue ( r.isNextChar ( (char) 0 ) );
        // assertFalse ( r.isNextChar ( '4' ) );

        assertTrue ( r.isAtString ( "4" ) );
        assertFalse ( r.isAtString ( "2" ) );
        assertFalse ( r.isAtString ( "44" ) );


        r = readerGetter.apply ( "1 \t\r\n" );

        assertFalse ( r.isAt ( CharChecks::isSpaceOrTab ) );
        assertFalse ( r.isAt ( CharChecks::isNewLine ) );
        assertFalse ( r.isAt ( CharChecks::isSpaceOrTabOrNewLine ) );

        r.advance();
        assertTrue ( r.isAt ( CharChecks::isSpaceOrTab ) );
        assertFalse ( r.isAt ( CharChecks::isNewLine ) );
        assertTrue ( r.isAt ( CharChecks::isSpaceOrTabOrNewLine ) );

        r.advance();
        assertTrue ( r.isAt ( CharChecks::isSpaceOrTab ) );
        assertFalse ( r.isAt ( CharChecks::isNewLine ) );
        assertTrue ( r.isAt ( CharChecks::isSpaceOrTabOrNewLine ) );

        r.advance();
        assertFalse ( r.isAt ( CharChecks::isSpaceOrTab ) );
        assertTrue ( r.isAt ( CharChecks::isNewLine ) );
        assertTrue ( r.isAt ( CharChecks::isSpaceOrTabOrNewLine ) );

        r.advance();
        assertFalse ( r.isAt ( CharChecks::isSpaceOrTab ) );
        assertTrue ( r.isAt ( CharChecks::isNewLine ) );
        assertTrue ( r.isAt ( CharChecks::isSpaceOrTabOrNewLine ) );
    }


    // skip

    @Test
    public void testSkipFunctions() throws IOException {
        testSkipFunctions_ ( CharReaderTest::getDefaultReader );
    }

    public static void testSkipFunctions_ ( Function<String, CharReader> readerGetter ) throws IOException {
/*
        final CharReader r = readerGetter.apply ( "12 \t\r\n \t\n3 \t \t4\r\n\n\r\n5 \t\r\n\t 6789123456789" );

        assertEquals ( '1', r.advance() );
        assertFalse ( r.skipChar ( '2' ) );
        assertFalse ( r.skipOneSpaceOrTab() );
        assertFalse ( r.skipOneNewLine() );
        assertFalse ( r.skipOneSpaceOrTabOrNewLine() );
        assertFalse ( r.skipSpacesAndTabs() );
        assertFalse ( r.skipNewLines() );
        assertFalse ( r.skipSpacesAndTabsAndNewLines() );
        assertFalse ( r.skipString ( "2" ) );

        assertTrue ( r.skipChar ( '1' ) );
        assertTrue ( r.skipString ( "2" ) );
        assertEquals ( ' ', r.currentChar() );

        assertTrue ( r.skipOneSpaceOrTab() );
        assertTrue ( r.skipOneSpaceOrTab() );
        assertTrue ( r.skipOneNewLine() );
        assertTrue ( r.skipOneNewLine() );
        assertTrue ( r.skipOneSpaceOrTabOrNewLine() );
        assertTrue ( r.skipOneSpaceOrTabOrNewLine() );
        assertTrue ( r.skipOneSpaceOrTabOrNewLine() );
        assertEquals ( '3', r.currentChar() );
        r.advance();

        assertTrue ( r.skipSpacesAndTabs() );
        assertEquals ( '4', r.currentChar() );
        r.advance();

        assertTrue ( r.skipNewLines() );
        assertEquals ( '5', r.currentChar() );
        r.advance();

        assertTrue ( r.skipSpacesAndTabsAndNewLines() );
        assertEquals ( '6', r.currentChar() );

        assertTrue ( r.skipString ( "6789" ) );
        assertEquals ( '1', r.currentChar() );

        r.skipNChars ( 0 );
        assertEquals ( '1', r.currentChar() );

        r.skipNChars ( 1 );
        assertEquals ( '2', r.currentChar() );

        r.skipNChars ( 3 );
        assertEquals ( '5', r.currentChar() );

        assertThrows ( IllegalArgumentException.class, () -> r.skipNChars ( -1 ) );

        r.skipNChars ( 4 );
        assertEquals ( '9', r.currentChar() );
        assertFalse ( r.hasNext() );
        assertThrows ( IllegalArgumentException.class, () -> r.skipNChars ( 1 ) );

        CharReader r2 = readerGetter.apply ( " " );
        r2.next();
        assertFalse ( r2.hasNext() );
        assertThrows ( IllegalCallerException.class, () -> r2.skipChar ( ' ' ) );
        assertThrows ( IllegalCallerException.class, r2::skipOneSpaceOrTabOrNewLine );
        assertThrows ( IllegalCallerException.class, r2::skipSpacesAndTabsAndNewLines );
*/
    }

    // read-ahead

    @Test
    public void testMark() throws IOException {
        testMark_( CharReaderTest::getDefaultReader );
    }

    public static void testMark_ ( Function<String, CharReader> readerGetter ) throws IOException {

        CharReader r = readerGetter.apply ( "123\n45" );

        assertEquals ( '1', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );

        r.setMark ( 1 );
        r.advance();
        assertEquals ( '2', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );
        r.goBackToMark ();
        assertEquals ( '1', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );

        r.setMark ( 10 );
        r.advance();
        r.advance();
        r.advance();
        r.advance();
        r.advance();
        assertEquals ( '5', r.currentChar() );
        assertEquals ( 2, r.currentLineNumber() );
        assertEquals ( 2, r.currentColumnNumber() );
        r.goBackToMark ();
        assertEquals ( '1', r.currentChar() );
        assertEquals ( 1, r.currentLineNumber() );
        assertEquals ( 1, r.currentColumnNumber() );
    }

/*
    @Test
    public void testPeekCurrentString() throws IOException {
        testPeekCurrentString_( CharReaderTest::getDefaultReader );
    }

    public static void testPeekCurrentString_ ( Function<String, CharReader> readerGetter ) throws IOException {

        CharReader r = readerGetter.apply ( "1234" );

        r.advance();

        assertEquals ( "1", r.peekCurrentString( 1 ) );
        assertEquals ( "12", r.peekCurrentString( 2 ) );
        assertEquals ( "1234", r.peekCurrentString( 4 ) );
        assertEquals ( "1234", r.peekCurrentString( 5 ) );
        assertEquals ( "1234", r.peekCurrentString( 500 ) );
        assertEquals ( '1', r.currentChar() );

        r.advance();
        assertEquals ( "2", r.peekCurrentString( 1 ) );
        assertEquals ( "23", r.peekCurrentString( 2 ) );
        assertEquals ( "234", r.peekCurrentString( 3 ) );
        assertEquals ( "234", r.peekCurrentString( 4 ) );
        assertEquals ( '2', r.currentChar() );

        r.advance();
        r.advance();
        assertEquals ( "4", r.peekCurrentString( 1 ) );
        assertEquals ( "4", r.peekCurrentString( 2 ) );
        assertEquals ( "4", r.peekCurrentString( 3 ) );
        assertEquals ( '4', r.currentChar() );
    }
*/

    @Test
    public void testPeekCharAfter() throws IOException {
        testPeekCharAfter_ ( CharReaderTest::getDefaultReader );
    }

    public static void testPeekCharAfter_ ( Function<String, CharReader> readerGetter ) throws IOException {

        CharReader r = readerGetter.apply ( "1 2  3   4 " );

        assertEquals ( '1', r.currentChar() );
        assertNull ( r.peekCharAfterRequired ( CharChecks::isSpaceOrTabOrNewLine, 10) );

        r.advance();
        assertEquals ( ' ', r.currentChar() );
        assertEquals ( '2', r.peekCharAfterRequired ( CharChecks::isSpaceOrTabOrNewLine,10) );

        r.advance();
        assertEquals ( '2', r.currentChar() );
        assertNull ( r.peekCharAfterRequired ( CharChecks::isSpaceOrTabOrNewLine, 10) );

        r.advance();
        assertEquals ( ' ', r.currentChar() );
        assertEquals ( '3', r.peekCharAfterRequired ( CharChecks::isSpaceOrTabOrNewLine,10) );

        r.advance();
        r.advance();
        assertEquals ( '3', r.currentChar() );
        r.advance();
        assertEquals ( '4', r.peekCharAfterRequired ( CharChecks::isSpaceOrTabOrNewLine,10) );

        r.advance();
        r.advance();
        r.advance();
        assertEquals ( '4', r.currentChar() );
        r.advance();
        assertEquals ( ' ', r.currentChar() );
        assertNull ( r.peekCharAfterRequired ( CharChecks::isSpaceOrTabOrNewLine, 10) );
    }
}
