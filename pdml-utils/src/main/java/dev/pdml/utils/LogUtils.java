package dev.pdml.utils;

import dev.pdml.utils.parser.PdmlParserBuilder;
import dev.pdml.utils.parser.eventhandlers.Logger_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.inspection.TextErrorException;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class LogUtils {

    public static void logParserEvents (
        @NotNull Path inputFile, @Nullable Path logFile ) throws IOException, TextErrorException {

        try ( Writer writer = TextFileIO.getUTF8FileOrStdoutWriter ( logFile ) ) {
            new PdmlParserBuilder<> ( new Logger_ParserEventHandler ( writer ) )
                .allowAlternativeAttributesStartSyntax ( true )
                .parseFile ( inputFile );
        }
    }
}
