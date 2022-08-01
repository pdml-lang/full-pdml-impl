package dev.pdml.ext.utilities;

import dev.pdml.ext.utilities.parser.PDMLParserBuilder;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.error.TextErrorException;
import dev.pdml.ext.utilities.parser.eventhandlers.Logger_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class LogUtils {

    public static void logParserEvents (
        @NotNull Path inputFile, @Nullable Path logFile ) throws IOException, TextErrorException {

        try ( Writer writer = TextFileIO.getUTF8FileOrStdoutWriter ( logFile ) ) {
            new PDMLParserBuilder<> ( new Logger_ParserEventHandler ( writer ) )
                .parseFile ( inputFile );
        }
    }
}
