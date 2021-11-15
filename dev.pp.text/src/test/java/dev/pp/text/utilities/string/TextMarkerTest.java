package dev.pp.text.utilities.string;

import dev.pp.text.utilities.text.TextMarker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextMarkerTest {

    @Test
    public void testInsertInlineMarker() {

        assertEquals ( " M 12345", TextMarker.insertInlineMarker ( "12345", 1, " M " ) );
        assertEquals ( "1 M 2345", TextMarker.insertInlineMarker ( "12345", 2, " M " ) );
        assertEquals ( "1234 M 5", TextMarker.insertInlineMarker ( "12345", 5, " M " ) );
        assertEquals ( "12345 M ", TextMarker.insertInlineMarker ( "12345", 6, " M " ) );
        assertEquals ( " M ", TextMarker.insertInlineMarker ( "", 1, " M " ) );
    }

    @Test
    public void testCreateMarkerLineForTextLine() {

        assertEquals ( " ^^", TextMarker.createMarkerLineForTextLine ( "12345", 1, '^', 2 ) );

        assertEquals ( "^",    TextMarker.createMarkerLineForTextLine ( "123", 0, '^', 1 ) );
        assertEquals ( "^^",   TextMarker.createMarkerLineForTextLine ( "123", 0, '^', 2 ) );
        assertEquals ( "^^^",  TextMarker.createMarkerLineForTextLine ( "123", 0, '^', 3 ) );
        assertEquals ( "^^^^", TextMarker.createMarkerLineForTextLine ( "123", 0, '^', 4 ) );
        assertEquals ( "^^^^", TextMarker.createMarkerLineForTextLine ( "123", 0, '^', 5 ) );

        assertEquals ( "   ^", TextMarker.createMarkerLineForTextLine ( "123", 3, '^', 1 ) );
        assertEquals ( "   ^", TextMarker.createMarkerLineForTextLine ( "123", 3, '^', 3 ) );
        assertThrows ( AssertionError.class, () -> TextMarker.createMarkerLineForTextLine ( "123", 4, '^', 2 ) );
    }

    @Test
    public void testAppendMarkerLineToTextLine() {

        String expected = "12345" + StringConstants.OS_NEW_LINE +
                          " ^^";
        assertEquals ( expected, TextMarker.appendMarkerLineToTextLine ( "12345", 1, '^', 2 ) );
    }

    @Test
    public void testBreakSingleTextLineAndInsertMarkerLine() {

        // single line

        String expected = "012345" + StringConstants.OS_NEW_LINE +
                          "  ^^^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012345", 2, '^', 3, false, null ) );

        expected = "012345" + StringConstants.OS_NEW_LINE +
                   "^^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012345", 0, '^', 2, false, null ) );

        expected = "012345" + StringConstants.OS_NEW_LINE +
                   "     ^^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012345", 5, '^', 3, false, null ) );

        expected = "012345" + StringConstants.OS_NEW_LINE +
                   "      ^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012345", 6, '^', 3, false, null ) );


        // remove indent

        expected = "345" + StringConstants.OS_NEW_LINE +
                   "^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "   345", 3, '^', 1, true, null ) );

        expected = "123" + StringConstants.OS_NEW_LINE +
                   "^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            " 123", 1, '^', 1, true, null ) );

        expected = "012" + StringConstants.OS_NEW_LINE +
                   " ^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012", 1, '^', 1, true, null ) );

        expected = "345" + StringConstants.OS_NEW_LINE +
                   "   ^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "   345", 6, '^', 1, true, null ) );

        expected = "   " + StringConstants.OS_NEW_LINE +
                   " ^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "   ", 1, '^', 1, true, null ) );

        expected = "   345" + StringConstants.OS_NEW_LINE +
                   " ^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "   345", 1, '^', 1, true, null ) );


        // multiple lines

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "^^" + StringConstants.OS_NEW_LINE +
                   "456";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 0, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   " ^^" + StringConstants.OS_NEW_LINE +
                   "456";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 1, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "  ^^" + StringConstants.OS_NEW_LINE +
                   "456";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 2, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "   ^^" + StringConstants.OS_NEW_LINE +
                   "456";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 3, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "456" + StringConstants.OS_NEW_LINE +
                   "^^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 4, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "456" + StringConstants.OS_NEW_LINE +
                   " ^^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 5, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "456" + StringConstants.OS_NEW_LINE +
                   "  ^^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 6, '^', 2, true, 4 ) );

        expected = "012 " + StringConstants.OS_NEW_LINE +
                   "456" + StringConstants.OS_NEW_LINE +
                   "   ^";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            "012 456", 7, '^', 2, true, 4 ) );

        String textLine = "012 456 89012 45 789012345";
        expected = "012 456" + StringConstants.OS_NEW_LINE +
                   " 89012 " + StringConstants.OS_NEW_LINE +
                   "45 " + StringConstants.OS_NEW_LINE +
                   "7890123" + StringConstants.OS_NEW_LINE +
                   "    ^^" + StringConstants.OS_NEW_LINE +
                   "45";
        assertEquals ( expected, TextMarker.breakSingleTextLineAndInsertMarkerLine (
            textLine, 21, '^', 2, true, 7 ) );
    }
}