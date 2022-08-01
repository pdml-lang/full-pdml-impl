package dev.pdml.ext.utilities.parser;

import dev.pdml.ext.utilities.parser.eventhandlers.AppendToStringMap_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Map;

public class StringMapPDMLParser {

    public static @Nullable Map<String, String> parse (
        @NotNull Reader reader,
        @Nullable TextResource textResource ) throws IOException, TextErrorException {

        AppendToStringMap_ParserEventHandler eventHandler = new AppendToStringMap_ParserEventHandler ();

        new PDMLParserBuilder<> ( eventHandler )
            .parseReader ( reader, textResource );

        return eventHandler.getResult();
    }

    public static @Nullable Map<String, String> parse (
        @NotNull Path filePath ) throws IOException, TextErrorException {

        return parse (
            TextFileIO.getUTF8FileReader ( filePath ),
            new File_TextResource ( filePath ) );
    }


    public static @Nullable Map<String, String> parse (
        @NotNull String string ) throws IOException, TextErrorException {

        return parse ( new StringReader ( string ), null );
    }
}
