package dev.pp.text.utilities.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringAlignerTest {

    @Test
    public void testAlign() {

        assertEquals ( "1  ", StringAligner.align ( "1", 3, HTextAlign.LEFT ) );
        assertEquals ( " 1 ", StringAligner.align ( "1", 3, HTextAlign.CENTER ) );
        assertEquals ( "  1", StringAligner.align ( "1", 3, HTextAlign.RIGHT ) );

        assertEquals ( "123   ", StringAligner.align ( "123", 6, HTextAlign.LEFT ) );
        assertEquals ( " 123  ", StringAligner.align ( "123", 6, HTextAlign.CENTER ) );
        assertEquals ( "   123", StringAligner.align ( "123", 6, HTextAlign.RIGHT ) );

        assertEquals ( "123 ", StringAligner.align ( "123", 4, HTextAlign.LEFT ) );
        assertEquals ( "123 ", StringAligner.align ( "123", 4, HTextAlign.CENTER ) );
        assertEquals ( " 123", StringAligner.align ( "123", 4, HTextAlign.RIGHT ) );

        assertEquals ( "123", StringAligner.align ( "123", 3, HTextAlign.LEFT ) );
        assertEquals ( "123", StringAligner.align ( "123", 3, HTextAlign.CENTER ) );
        assertEquals ( "123", StringAligner.align ( "123", 3, HTextAlign.RIGHT ) );

        assertEquals ( "   ", StringAligner.align ( "", 3, HTextAlign.LEFT ) );
        assertEquals ( "   ", StringAligner.align ( "", 3, HTextAlign.CENTER ) );
        assertEquals ( "   ", StringAligner.align ( "", 3, HTextAlign.RIGHT ) );

        assertEquals ( "   ", StringAligner.align ( null, 3, HTextAlign.LEFT ) );
        assertEquals ( "   ", StringAligner.align ( null, 3, HTextAlign.CENTER ) );
        assertEquals ( "   ", StringAligner.align ( null, 3, HTextAlign.RIGHT ) );
    }

    @Test
    public void testAlignOrTruncateWithEllipses() {

        assertEquals ( "123   ", StringAligner.alignOrTruncateWithEllipses ( "123", 6, HTextAlign.LEFT ) );
        assertEquals ( "123456", StringAligner.alignOrTruncateWithEllipses ( "123456", 6, HTextAlign.LEFT ) );
        assertEquals ( "12 ...", StringAligner.alignOrTruncateWithEllipses ( "1234567", 6, HTextAlign.LEFT ) );
    }


}