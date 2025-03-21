package dev.pp.pdml.utils.parser;

import dev.pp.pdml.data.exception.InvalidPdmlDataException;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.parser.PdmlParserConfigBuilder;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameters.ParametersCreator;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.reader.CharReader;
import dev.pp.core.text.reader.CharReaderImpl;
import dev.pp.core.text.resource.File_TextResource;
import dev.pp.core.text.resource.String_TextResource;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.utilities.file.TextFileReaderUtil;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class ParametersUtil {

    public static <V> @Nullable Parameters<V> parse (
        @NotNull CharReader charReader,
        boolean hasRootNode,
        // boolean allowNullValues,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        Parameters<String> stringParameters = StringParametersUtil.parse (
            charReader, hasRootNode, true, parserConfig );
        try {
            return ParametersCreator.createFromStringParameters (
                stringParameters, parameterSpecs );
        } catch ( InvalidTextException e ) {
            throw new InvalidPdmlDataException ( e );
        }
    }

    public static <V> @Nullable Parameters<V> parseReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        return parse (
            CharReaderImpl.createAndAdvance ( reader, textResource, lineOffset, columnOffset ),
            hasRootNode, parserConfig, parameterSpecs );
    }

    public static <V> @Nullable Parameters<V> parseReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        boolean hasRootNode,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        return parseReader ( reader, textResource, null, null,
            hasRootNode, PdmlParserConfigBuilder.createDefault(), parameterSpecs );
    }

    public static <V> @Nullable Parameters<V> parseString (
        @NotNull String string,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        boolean hasRootNode,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( string ) ) {
            return parseReader ( reader, textResource, lineOffset, columnOffset,
                hasRootNode, parserConfig, parameterSpecs );
        }
    }

    public static <V> @Nullable Parameters<V> parseString (
        @NotNull String string,
        boolean hasRootNode,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( string ) ) {
            return parseReader (
                reader, new String_TextResource ( string ), hasRootNode, parameterSpecs );
        }
    }

    public static <V> @Nullable Parameters<V> parseFile (
        @NotNull Path filePath,
        boolean hasRootNode,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return parseReader (
                reader, new File_TextResource ( filePath ), null, null,
                hasRootNode, parserConfig, parameterSpecs );
        }
    }

    public static <V> @Nullable Parameters<V> parseFile (
        @NotNull Path filePath,
        boolean hasRootNode,
        @NotNull ParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return parseReader (
                reader, new File_TextResource ( filePath ), hasRootNode, parameterSpecs );
        }
    }
}
