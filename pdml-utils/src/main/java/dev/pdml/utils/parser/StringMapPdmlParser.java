package dev.pdml.utils.parser;

import dev.pdml.utils.parser.eventhandlers.AppendToStringMap_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StringMapPdmlParser {

    public static void parseAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode,
        @NotNull Map<String, String> map ) throws IOException, TextErrorException {

        AppendToStringMap_ParserEventHandler eventHandler = new AppendToStringMap_ParserEventHandler ( map );

        CharReaderWithInserts charReader = hasRootNode ?
            CharReaderWithInsertsImpl.createAndAdvance ( reader, textResource, lineOffset, columnOffset ) :
            PdmlParserUtils.createVirtualRootReader ("root", reader, textResource, lineOffset, columnOffset );

        new PdmlParserBuilder<> ( eventHandler )
            .allowAlternativeAttributesStartSyntax ( true )
            .parseCharReader ( charReader );
    }

    public static @Nullable Map<String, String> parse (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode ) throws IOException, TextErrorException {

        Map<String, String> map = new HashMap<>();
        parseAndAdd ( reader, textResource, lineOffset, columnOffset, hasRootNode, map );

        return map.isEmpty() ? null : map;
    }

    public static @Nullable Map<String, String> parseFile (
        @NotNull Path filePath,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( FileReader reader = TextFileIO.getUTF8FileReader ( filePath ) ) {
            return parse (
                reader,
                new File_TextResource ( filePath ),
                null,
                null,
                hasRootNode );
        }
    }


    public static @Nullable Map<String, String> parseString (
        @NotNull String string,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( StringReader reader = new StringReader ( string ) ) {
            return parse (
                reader,
                textResource,
                lineOffset,
                columnOffset,
                hasRootNode );
        }
    }

    public static @Nullable Map<String, String> parseString (
        @NotNull String string,
        boolean hasRootNode ) throws IOException, TextErrorException {

        return parseString ( string, new String_TextResource ( string ), null, null, hasRootNode );
    }
}
