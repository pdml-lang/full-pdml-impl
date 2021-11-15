package dev.pp.text.utilities.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringBuilderUtilitiesTest {

    @Test
    public void testRemoveOptionalNewLineAtEnd() {

        StringBuilder sb = new StringBuilder ( "abc\r\n\r\n" );
        assertEquals ( "abc\r\n", StringBuilderUtils.removeOptionalNewLineAtEnd ( sb ).toString() );

        sb = new StringBuilder ( "abc\n\n" );
        assertEquals ( "abc\n", StringBuilderUtils.removeOptionalNewLineAtEnd ( sb ).toString() );

        sb = new StringBuilder ( "abc\r\n" );
        assertEquals ( "abc", StringBuilderUtils.removeOptionalNewLineAtEnd ( sb ).toString() );

        sb = new StringBuilder ( "abc\n" );
        assertEquals ( "abc", StringBuilderUtils.removeOptionalNewLineAtEnd ( sb ).toString() );

        sb = new StringBuilder ( "abc" );
        assertEquals ( "abc", StringBuilderUtils.removeOptionalNewLineAtEnd ( sb ).toString() );
    }
}