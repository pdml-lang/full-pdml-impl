package dev.pdml.core.reader.parser;

import dev.pp.text.utilities.string.StringTruncator;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.eventHandler.impls.Logger_ParserEventHandler;
// import dev.pxml.ext.extensions.DefaultPXMLExtensionsHandler;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEventStreamParserTest {

    private StringWriter writer = new StringWriter();
    private Logger_ParserEventHandler eventHandler = new Logger_ParserEventHandler ( writer );

    @Test
    public void testNameSeparator() throws TextReaderException {

        checkLogContains ( "[empty]", "Root start, [1, 2], <empty>" );
        checkLogContains ( "[empty ]", "Root start, [1, 2], <empty>" );
        checkLogNotContains ( "[empty ]", "Text" );
        checkLogContainsAll ( "[empty  ]", new String[]{"Root start, [1, 2], <empty>", "Text, [1, 8], < >"} );
    }

    private void checkLog ( String code, String expectedLog ) throws TextReaderException {

        initHandler();
        /*
        eventHandler.logNone();
        eventHandler.logRootNodeStart = true;
        eventHandler.logNodeStart = true;
        eventHandler.logText = true;
        */

        parse ( code );

        assertEquals ( expectedLog, writer.toString() );
    }

    private void checkLogStart ( String code, String expectedLogStart ) throws TextReaderException {

        initHandler();
        /*
        eventHandler.logNone();
        eventHandler.logRootNodeStart = true;
        eventHandler.logNodeStart = true;
        eventHandler.logText = true;
        */

        parse ( code );

        String logStart = StringTruncator.truncate ( writer.toString(), expectedLogStart.length () );
        assertEquals ( expectedLogStart, logStart );
    }

    private void checkLogContains ( String code, String logSubstring ) throws TextReaderException {

        initHandler();
        /*
        eventHandler.logNone();
        eventHandler.logRootNodeStart = true;
        eventHandler.logNodeStart = true;
        eventHandler.logText = true;
        */

        parse ( code );

        String logResult = writer.toString();

        assertTrue ( logResult.contains ( logSubstring ) );
    }

    private void checkLogNotContains ( String code, String logSubstring ) throws TextReaderException {

        initHandler();
        /*
        eventHandler.logNone();
        eventHandler.logRootNodeStart = true;
        eventHandler.logNodeStart = true;
        eventHandler.logText = true;
        */

        parse ( code );

        String logResult = writer.toString();

        assertFalse ( logResult.contains ( logSubstring ) );
    }

    private void checkLogContainsAll ( String code, String[] logSubstrings ) throws TextReaderException {

        initHandler();
        /*
        eventHandler.logNone();
        eventHandler.logRootNodeStart = true;
        eventHandler.logNodeStart = true;
        eventHandler.logText = true;
        */

        parse ( code );

        String logResult = writer.toString();

        for ( String logSubstring : logSubstrings ) {
            assertTrue ( logResult.contains ( logSubstring ) );
        }
    }

    private void parse ( String code ) throws TextReaderException {

        new EventStreamParserBuilder<> ( eventHandler )
            // .setExtensionsHandler ( new DefaultPXMLExtensionsHandler () )
            .parseString ( code );
    }

    private void initHandler() {

        writer = new StringWriter ();
        eventHandler = new Logger_ParserEventHandler ( writer );
        eventHandler.separator = ", ";
    }
}
