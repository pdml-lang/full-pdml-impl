package dev.pdml.utils.parser;

import dev.pdml.utils.parser.eventhandlers.Logger_ParserEventHandler;
import dev.pp.basics.utilities.string.StringTruncator;
import dev.pp.text.inspection.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;


class PdmlParserTest {

    private StringWriter writer = new StringWriter();
    private Logger_ParserEventHandler eventHandler = new Logger_ParserEventHandler ( writer );

    @Test
    public void testNameSeparator() throws IOException, TextErrorException {

        checkLogContains ( "[empty]", "Root start, [1, 2], <empty>" );
        checkLogContains ( "[empty ]", "Root start, [1, 2], <empty>" );
        checkLogNotContains ( "[empty ]", "Text" );
        checkLogContainsAll ( "[empty  ]", new String[]{"Root start, [1, 2], <empty>", "Text, [1, 8], < >"} );
    }

    private void checkLog ( String code, String expectedLog ) throws IOException, TextErrorException {

        initHandler();

        parse ( code );

        assertEquals ( expectedLog, writer.toString() );
    }

    private void checkLogStart ( String code, String expectedLogStart ) throws IOException, TextErrorException {

        initHandler();

        parse ( code );

        String logStart = StringTruncator.truncate ( writer.toString(), expectedLogStart.length () );
        assertEquals ( expectedLogStart, logStart );
    }

    private void checkLogContains ( String code, String logSubstring ) throws IOException, TextErrorException {

        initHandler();

        parse ( code );

        String logResult = writer.toString();

        assertTrue ( logResult.contains ( logSubstring ) );
    }

    private void checkLogNotContains ( String code, String logSubstring ) throws IOException, TextErrorException {

        initHandler();

        parse ( code );

        String logResult = writer.toString();

        assertFalse ( logResult.contains ( logSubstring ) );
    }

    private void checkLogContainsAll ( String code, String[] logSubstrings ) throws IOException, TextErrorException {

        initHandler();

        parse ( code );

        String logResult = writer.toString();

        for ( String logSubstring : logSubstrings ) {
            assertTrue ( logResult.contains ( logSubstring ) );
        }
    }

    private void parse ( String code ) throws IOException, TextErrorException {

        new PdmlParserBuilder<> ( eventHandler )
            .allowAlternativeAttributesStartSyntax ( true )
            .parseString ( code, null, null );
    }

    private void initHandler() {

        writer = new StringWriter ();
        eventHandler = new Logger_ParserEventHandler ( writer );
        eventHandler.separator = ", ";
    }
}
