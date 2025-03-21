package dev.pp.pdml.parser;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.resource.File_TextResource;
import dev.pp.core.text.resource.String_TextResource;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.utilities.file.TextFileReaderUtil;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class PdmlParserUtil {


    // Reader

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull PdmlParserConfig config ) throws IOException, PdmlException {

        PdmlParser parser = PdmlParser.create ( reader, textResource, lineOffset, columnOffset, config );
        return parser.requireRootNode();
    }

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull PdmlParserConfig config ) throws IOException, PdmlException {

        return parseReader ( reader, textResource, null, null, config );
    }

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader,
        @NotNull PdmlParserConfig config ) throws IOException, PdmlException {

        return parseReader ( reader, null, config );
    }

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader ) throws IOException, PdmlException {

        return parseReader ( reader, PdmlParserConfig.defaultConfig() );
    }


    // TextResourceReader

    public static @NotNull TaggedNode parseReader (
        @NotNull TextResourceReader textResourceReader,
        @NotNull PdmlParserConfig config ) throws IOException, PdmlException {

        return parseReader (
            textResourceReader.getReader(),
            textResourceReader.getTextResource(),
            config );
    }


    // String

    public static @NotNull TaggedNode parseString (
        @NotNull String string,
        @NotNull PdmlParserConfig config ) throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( string ) ) {
            return parseReader ( reader, new String_TextResource ( string ), config );
        }
    }

    public static @NotNull TaggedNode parseString (
        @NotNull String string ) throws IOException, PdmlException {

        return parseString ( string, PdmlParserConfig.defaultConfig() );
    }


    // File

    public static @NotNull TaggedNode parseFile (
        @NotNull Path filePath,
        @NotNull PdmlParserConfig config ) throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return parseReader ( reader, new File_TextResource ( filePath ), config );
        }
    }

    public static @NotNull TaggedNode parseFile (
        @NotNull Path filePath ) throws IOException, PdmlException {

        return parseFile ( filePath, PdmlParserConfig.defaultConfig() );
    }
}
