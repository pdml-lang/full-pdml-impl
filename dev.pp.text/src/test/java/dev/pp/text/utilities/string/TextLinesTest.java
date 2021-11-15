package dev.pp.text.utilities.string;

import dev.pp.text.utilities.text.TextLines;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextLinesTest {

    @Test
    public void testTextToTextWithMaxLineLength() {

        // without new line in input

        assertEquals ( "123 56 89", TextLines.textToTextWithMaxLineLength ( "123 56 89", 10 ) );
        assertEquals ( "123 56 89", TextLines.textToTextWithMaxLineLength ( "123 56 89", 9 ) );

        String expected = String.join ( StringConstants.OS_NEW_LINE,
            "123 56 ",
            "89" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 8 ) );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 7 ) );

        expected = String.join ( StringConstants.OS_NEW_LINE,
            "123 56",
            " 89" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 6 ) );

        expected = String.join ( StringConstants.OS_NEW_LINE,
            "123 ",
            "56 89" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 5 ) );
        expected = String.join ( StringConstants.OS_NEW_LINE,
            "123 ",
            "56 ",
            "89" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 4 ) );

        expected = String.join ( StringConstants.OS_NEW_LINE,
            "123",
            " 56",
            " 89" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 3 ) );

        expected = String.join ( StringConstants.OS_NEW_LINE,
            "12",
            "3 ",
            "56",
            " 8",
            "9" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 2 ) );

        expected = String.join ( StringConstants.OS_NEW_LINE,
            "1",
            "2",
            "3",
            " ",
            "5",
            "6",
            " ",
            "8",
            "9" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( "123 56 89", 1 ) );

        // with new line in input

        String input = String.join ( StringConstants.OS_NEW_LINE,
            "123 56 89",
            "line 2",
            "line3",
            "line44444",
            "A long line (too long)" );
        expected = String.join ( StringConstants.OS_NEW_LINE,
            "123 56",
            " 89",
            "line 2",
            "line3",
            "line44",
            "444",
            "A long",
            " line ",
            "(too ",
            "long)" );
        assertEquals ( expected, TextLines.textToTextWithMaxLineLength ( input, 6 ) );
    }
}
