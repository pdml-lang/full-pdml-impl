package dev.pdml.utils.parser;

import dev.pdml.utils.parser.eventhandlers.AppendToStringParameters_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.parameters.parameters.MutableParameters;
import dev.pp.parameters.parameters.Parameters;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class StringParametersPdmlParser {

    // parse

    public static @Nullable Parameters<String> parse (
        @NotNull CharReaderWithInserts charReader,
        boolean ignoreTextAfterEndOfRootNode ) throws IOException, TextErrorException {

        MutableParameters<String> result = new MutableParameters<> (
            new TextToken ( charReader.currentChar(), charReader.currentLocation() ) );
        parseAndAdd ( charReader, ignoreTextAfterEndOfRootNode, result );
        return result.makeImmutableOrNull();
    }

    public static @Nullable Parameters<String> parseReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean ignoreTextAfterEndOfRootNode,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode ) throws IOException, TextErrorException {

        CharReaderWithInserts charReader = charReaderWithInserts (
            reader, textResource, lineOffset, columnOffset, hasRootNode );

        return parse ( charReader, ignoreTextAfterEndOfRootNode );
    }

    public static @Nullable Parameters<String> parseReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean hasRootNode ) throws IOException, TextErrorException {

        return parseReader (
            reader, textResource, false, null, null, hasRootNode );
    }

    public static @Nullable Parameters<String> parseFile (
        @NotNull Path filePath,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = TextFileIO.getUTF8FileReader ( filePath ) ) {
            return parseReader ( reader, new File_TextResource ( filePath ), hasRootNode );
        }
    }

    public static @Nullable Parameters<String> parseString (
        @NotNull String string,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = new StringReader ( string ) ) {
            return parseReader ( reader, new String_TextResource ( string ), hasRootNode );
        }
    }


    // parseAndAdd

    public static void parseAndAdd (
        @NotNull CharReaderWithInserts charReader,
        boolean ignoreTextAfterEndOfRootNode,
        @NotNull MutableParameters<String> stringParameters ) throws IOException, TextErrorException {

        AppendToStringParameters_ParserEventHandler eventHandler =
            new AppendToStringParameters_ParserEventHandler ( stringParameters );

        new PdmlParserBuilder<> ( eventHandler )
            .ignoreTextAfterEndOfRootNode ( ignoreTextAfterEndOfRootNode )
            .allowAlternativeAttributesStartSyntax ( true )
            .parseCharReader ( charReader );
    }

    public static void parseReaderAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean ignoreTextAfterEndOfRootNode,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode,
        @NotNull MutableParameters<String> stringParameters ) throws IOException, TextErrorException {

        CharReaderWithInserts charReader = charReaderWithInserts (
            reader, textResource, lineOffset, columnOffset, hasRootNode );

        parseAndAdd ( charReader, ignoreTextAfterEndOfRootNode, stringParameters );;
    }

    public static void parseReaderAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean hasRootNode,
        @NotNull MutableParameters<String> stringParameters ) throws IOException, TextErrorException {

        parseReaderAndAdd (
            reader, textResource, false, null, null, hasRootNode, stringParameters );
    }

    public static void parseFileAndAdd (
        @NotNull Path filePath,
        @NotNull MutableParameters<String> stringParameters,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = TextFileIO.getUTF8FileReader ( filePath ) ) {
            parseReaderAndAdd ( reader, new File_TextResource ( filePath ), hasRootNode, stringParameters );
        }
    }

    public static void parseStringAndAdd (
        @NotNull String string,
        @NotNull MutableParameters<String> stringParameters,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = new StringReader ( string ) ) {
            parseReaderAndAdd ( reader, new String_TextResource ( string ), hasRootNode, stringParameters );
        }
    }


    // Private

    private static CharReaderWithInserts charReaderWithInserts (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode ) throws IOException {

        return hasRootNode
            ? CharReaderWithInsertsImpl.createAndAdvance ( reader, textResource, lineOffset, columnOffset )
            : PdmlParserUtils.createVirtualRootReader ( reader, textResource, lineOffset, columnOffset );
    }
}
