package dev.pdml.ext.utilities.parser;

import dev.pdml.ext.utilities.parser.eventhandlers.AppendToStringMap_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StringMapPDMLParser {

    public static void parseAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean hasRootNode,
        @NotNull Map<String, String> map ) throws IOException, TextErrorException {

        AppendToStringMap_ParserEventHandler eventHandler = new AppendToStringMap_ParserEventHandler ( map );

        CharReaderWithInserts charReader = hasRootNode ?
            new CharReaderWithInsertsImpl ( reader, textResource ) :
            PDMLParserUtils.createVirtualRootReader ("root", reader, textResource );

        new PDMLParserBuilder<> ( eventHandler )
            .parseCharReader ( charReader );
    }

    public static @Nullable Map<String, String> parse (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean hasRootNode ) throws IOException, TextErrorException {

        Map<String, String> map = new HashMap<>();
        parseAndAdd ( reader, textResource, hasRootNode, map );

        return map.isEmpty() ? null : map;
    }

    public static @Nullable Map<String, String> parseFile (
        @NotNull Path filePath,
        boolean hasRootNode ) throws IOException, TextErrorException {

        return parse (
            TextFileIO.getUTF8FileReader ( filePath ),
            new File_TextResource ( filePath ),
            hasRootNode );
    }


    public static @Nullable Map<String, String> parseString (
        @NotNull String string,
        boolean hasRootNode ) throws IOException, TextErrorException {

        return parse ( new StringReader ( string ), null, hasRootNode );
    }
}
