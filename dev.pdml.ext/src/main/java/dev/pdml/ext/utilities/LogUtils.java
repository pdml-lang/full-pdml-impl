package dev.pdml.ext.utilities;

import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.EventStreamParserBuilder;
import dev.pdml.core.reader.parser.eventHandler.impls.Logger_ParserEventHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.ext.extensions.DefaultPXMLExtensionsHandler;

import java.io.File;
import java.io.IOException;

public class LogUtils {

    public static void logParserEvents (
        @NotNull File inputFile, @Nullable File logFile ) throws IOException, TextReaderException {

        Logger_ParserEventHandler eventHandler =
            logFile != null
            ? new Logger_ParserEventHandler ( logFile )
            : new Logger_ParserEventHandler();

        new EventStreamParserBuilder<> ( eventHandler )
            .setExtensionsHandler ( new DefaultPXMLExtensionsHandler () )
            .parseFile ( inputFile );
    }
}
