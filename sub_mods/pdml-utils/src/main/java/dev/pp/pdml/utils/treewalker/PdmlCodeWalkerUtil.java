package dev.pp.pdml.utils.treewalker;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.utils.treewalker.handler.PdmlTreeWalkerEventHandler;
import dev.pp.pdml.parser.PdmlParserConfigBuilder;
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

public class PdmlCodeWalkerUtil {

    public static <N,R> R walkReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        PdmlCodeWalker<N, R> walker = new PdmlCodeWalker<> (
            reader, textResource, lineOffset, columnOffset, parserConfig, eventHandler );
        walker.walk();
        return eventHandler.getResult();
    }

    public static <N,R> R walkReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        return walkReader ( reader, textResource, null, null, parserConfig, eventHandler );
    }

    public static <N,R> R walkReader (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        PdmlParserConfig parserConfig = new PdmlParserConfigBuilder().build();
        return walkReader ( reader, textResource, null, null, parserConfig, eventHandler );
    }

    public static <N,R> R walkCode (
        @NotNull String code,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( code ) ) {
            return walkReader ( reader, new String_TextResource ( code ), parserConfig, eventHandler );
        }
    }

    public static <N,R> R walkCode (
        @NotNull String code,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        try ( StringReader reader = new StringReader ( code ) ) {
            return walkReader ( reader, new String_TextResource ( code ), eventHandler );
        }
    }

    public static <N,R> R walkFile (
        @NotNull Path filePath,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return walkReader ( reader, new File_TextResource ( filePath ), parserConfig, eventHandler );
        }
    }

    public static <N,R> R walkFile (
        @NotNull Path filePath,
        @NotNull PdmlTreeWalkerEventHandler<N,R> eventHandler ) throws IOException, PdmlException {

        try ( FileReader reader = TextFileReaderUtil.createUTF8FileReader ( filePath ) ) {
            return walkReader ( reader, new File_TextResource ( filePath ), eventHandler );
        }
    }
}
