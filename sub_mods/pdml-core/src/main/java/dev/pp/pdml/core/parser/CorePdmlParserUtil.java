package dev.pp.pdml.core.parser;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.resource.File_TextResource;
import dev.pp.core.text.resource.String_TextResource;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.utilities.file.TextFileReaderUtil;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class CorePdmlParserUtil {

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull CorePdmlParserConfig config ) throws IOException, PdmlException {

        CorePdmlParser parser = new CorePdmlParser (
            reader, resource, lineOffset, columnOffset, config );
        return parser.requireRootNode();
    }

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @NotNull CorePdmlParserConfig config ) throws IOException, PdmlException {

        return parseReader ( reader, resource, null, null, config );
    }

    public static @NotNull TaggedNode parseReader (
        @NotNull Reader reader )
        throws IOException, PdmlException {

        return parseReader ( reader, null, new CorePdmlParserConfig() );
    }

    public static @NotNull TaggedNode parseString (
        @NotNull String string,
        @NotNull CorePdmlParserConfig config )
        throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( string ) ) {
            return parseReader ( reader, new String_TextResource ( string ), config );
        }
    }

    public static @NotNull TaggedNode parseString (
        @NotNull String string )
        throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( string ) ) {
            return parseReader ( reader );
        }
    }

    public static @NotNull TaggedNode parseFile (
        @NotNull Path filePath,
        @NotNull CorePdmlParserConfig config )
        throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return parseReader ( reader, new File_TextResource ( filePath ), config );
        }
    }

    public static @NotNull TaggedNode parseFile (
        @NotNull Path filePath )
        throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return parseReader ( reader );
        }
    }
}
