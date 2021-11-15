package dev.pdml.core.reader.parser;

import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.resource.URL_TextResource;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.URLUtilities;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.reader.DefaultPXMLReader;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.PXMLReaderConfig;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

public interface EventStreamParser<N, R> {

    void parse (
        @NotNull PXMLReader PXMLReader,
        @NotNull EventStreamParserConfig<N, R> config ) throws TextReaderException;

    default void parseReader (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @NotNull EventStreamParserConfig<N, R> config ) throws TextReaderException {

        parse ( new DefaultPXMLReader ( reader, new PXMLReaderConfig ( resource, config ) ), config );
    }

    default void parseFile (
        @NotNull File file,
        @NotNull EventStreamParserConfig<N, R> config ) throws TextReaderException, IOException {

        parseReader ( FileUtilities.getUTF8FileReader ( file ), new File_TextResource ( file ), config );
    }

    default void parseString (
        @NotNull String string,
        @NotNull EventStreamParserConfig<N, R> config ) throws TextReaderException {

        parseReader ( new StringReader ( string ), new String_TextResource ( string ), config );
    }

    default void parseURL (
        @NotNull URL url,
        @NotNull EventStreamParserConfig<N, R> config ) throws TextReaderException, IOException {

        parseReader ( URLUtilities.getUTF8URLReader ( url ), new URL_TextResource ( url ), config );
    }
}
