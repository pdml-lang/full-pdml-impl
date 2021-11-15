package dev.pdml.ext.utilities;

import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.EventStreamParserBuilder;
import dev.pdml.core.reader.parser.eventHandler.impls.WriteStandalone_ParserEventHandler;
import dev.pp.text.annotations.NotNull;
import dev.pdml.ext.extensions.DefaultPXMLExtensionsHandler;

import java.io.File;
import java.io.IOException;

public class StandaloneUtils {

    public static void createStandalone (
        @NotNull File inputFile, @NotNull File outputFile ) throws IOException, TextReaderException {

        new EventStreamParserBuilder<> ( new WriteStandalone_ParserEventHandler ( outputFile ) )
            .setExtensionsHandler ( new DefaultPXMLExtensionsHandler() )
            .parseFile ( inputFile );
    }
}
