package dev.pp.text.utilities.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringTruncatorTest {

    @Test
    public void testTruncate() {

        assertEquals ( "123", StringTruncator.truncate ( "1234", 3 ) );
        assertEquals ( "123", StringTruncator.truncate ( "123", 3 ) );
        assertEquals ( "12", StringTruncator.truncate ( "12", 3 ) );
    }

    @Test
    public void testRightPad() {

        assertEquals ( "1   ", StringTruncator.rightPadOrTruncate ( "1", 4 ) );
        assertEquals ( "123 ", StringTruncator.rightPadOrTruncate ( "123", 4 ) );
        assertEquals ( "1234", StringTruncator.rightPadOrTruncate ( "1234", 4 ) );
        assertEquals ( "1234", StringTruncator.rightPadOrTruncate ( "12345", 4 ) );
        assertEquals ( "1234", StringTruncator.rightPadOrTruncate ( "123456", 4 ) );
    }

    @Test
    public void testTruncateWithEllipses() {

        assertEquals ( "1", StringTruncator.truncateWithEllipses ( "1", 7 ) );
        assertEquals ( "123456", StringTruncator.truncateWithEllipses ( "123456", 7 ) );
        assertEquals ( "1234567", StringTruncator.truncateWithEllipses ( "1234567", 7 ) );
        assertEquals ( "123 ...", StringTruncator.truncateWithEllipses ( "12345678", 7 ) );
        assertEquals ( "123 ...", StringTruncator.truncateWithEllipses ( "1234567890", 7 ) );
    }
}