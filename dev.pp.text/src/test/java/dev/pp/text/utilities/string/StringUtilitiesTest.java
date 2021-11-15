package dev.pp.text.utilities.string;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilitiesTest {

    @Test
    public void testReplaceQuoteWith2Quotes() {

        assertEquals ( "1", StringUtils.replaceQuoteWith2Quotes ( "1" ) );
        assertEquals ( "1\"\"3", StringUtils.replaceQuoteWith2Quotes ( "1\"3" ) );
        assertEquals ( "1\"\"3\"\"5", StringUtils.replaceQuoteWith2Quotes ( "1\"3\"5" ) );
        assertEquals ( "\"\"\"\"\"\"", StringUtils.replaceQuoteWith2Quotes ( "\"\"\"" ) );
    }
}