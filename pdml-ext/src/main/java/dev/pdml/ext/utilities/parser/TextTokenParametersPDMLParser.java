package dev.pdml.ext.utilities.parser;

import dev.pdml.core.reader.PDMLReader;
import dev.pdml.ext.utilities.parser.eventhandlers.AppendToTextTokenParameters_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class TextTokenParametersPDMLParser {

    // parse

    public static @Nullable TextTokenParameters parse (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable TextToken startToken,
        boolean hasRootNode ) throws IOException, TextErrorException {

        if ( hasRootNode ) {
            return parseAndAdd ( reader, textResource, new TextTokenParameters ( startToken ) );
        } else {
            return parseWithoutRootAndAdd ( reader, textResource, "root", new TextTokenParameters ( startToken ) );
        }
    }

    public static @Nullable TextTokenParameters parseFile (
        @NotNull Path filePath,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = TextFileIO.getUTF8FileReader ( filePath ) ) {
            return parse ( reader, new File_TextResource ( filePath ), null, hasRootNode );
        }
    }

    public static @Nullable TextTokenParameters parseString (
        @NotNull String string,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = new StringReader ( string ) ) {
            return parse ( reader, new String_TextResource ( string ), null, hasRootNode );
        }
    }


    // parseAndAdd

    public static void parseAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull TextTokenParameters parameters,
        boolean hasRootNode ) throws IOException, TextErrorException {

        if ( hasRootNode ) {
            parseAndAdd ( reader, textResource, parameters );
        } else {
            parseWithoutRootAndAdd ( reader, textResource, "root", parameters );
        }
    }

    public static void parseFileAndAdd (
        @NotNull Path filePath,
        @NotNull TextTokenParameters parameters,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = TextFileIO.getUTF8FileReader ( filePath ) ) {
            parseAndAdd ( reader, new File_TextResource ( filePath ), parameters, hasRootNode );
        }
    }

    public static void parseStringAndAdd (
        @NotNull String string,
        @NotNull TextTokenParameters parameters,
        boolean hasRootNode ) throws IOException, TextErrorException {

        try ( Reader reader = new StringReader ( string ) ) {
            parseAndAdd ( reader, new String_TextResource ( string ), parameters, hasRootNode );
        }
    }

/*
    public static @Nullable TextTokenParameters parseAndAdd (
        @NotNull CharReaderWithInserts charReader,
        @NotNull TextTokenParameters textTokenParameters,
        boolean ignoreTextAfterEndOfRootNode ) throws IOException, InvalidTextException {

        AppendToTextTokenParameters_ParserEventHandler eventHandler =
            new AppendToTextTokenParameters_ParserEventHandler ( textTokenParameters );

        new PDMLParserBuilder<> ( eventHandler )
            .setCharReader ( charReader )
            .ignoreTextAfterEndOfRootNode ( ignoreTextAfterEndOfRootNode )
            .build()
            .parse();

        return eventHandler.getResult();
    }
*/

    public static @Nullable TextTokenParameters parseAndAdd (
        @NotNull PDMLReader PDMLReader,
        @NotNull TextTokenParameters textTokenParameters,
        boolean ignoreTextAfterEndOfRootNode ) throws IOException, TextErrorException {

        AppendToTextTokenParameters_ParserEventHandler eventHandler =
            new AppendToTextTokenParameters_ParserEventHandler ( textTokenParameters );

        new PDMLParserBuilder<> ( eventHandler )
            .ignoreTextAfterEndOfRootNode ( ignoreTextAfterEndOfRootNode )
            .parsePDMLReader ( PDMLReader );

        return eventHandler.getResult();
    }


    // private

    private static @Nullable TextTokenParameters parseAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull TextTokenParameters textTokenParameters ) throws IOException, TextErrorException {

        AppendToTextTokenParameters_ParserEventHandler eventHandler =
            new AppendToTextTokenParameters_ParserEventHandler ( textTokenParameters );

        new PDMLParserBuilder<> ( eventHandler )
            .parseReader ( reader, textResource );

        return eventHandler.getResult();
    }

    private static @Nullable TextTokenParameters parseWithoutRootAndAdd (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull String virtualRootName,
        @NotNull TextTokenParameters textTokenParameters ) throws IOException, TextErrorException {

        AppendToTextTokenParameters_ParserEventHandler eventHandler =
            new AppendToTextTokenParameters_ParserEventHandler ( textTokenParameters );

        CharReaderWithInserts charReader = PDMLParserUtils.createVirtualRootReader (
            virtualRootName, reader, textResource );

        new PDMLParserBuilder<> ( eventHandler )
            .parseCharReader ( charReader );

        return eventHandler.getResult();
    }
}
