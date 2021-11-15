package dev.pdml.core.reader.parser;

import dev.pdml.core.data.formalNode.FormalNodes;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.handler.Write_TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pdml.core.reader.reader.extensions.PXMLExtensionsHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.resource.TextResource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;

public class EventStreamParserBuilder<N, R> {

    private @NotNull ParserEventHandler<N, R> eventHandler;
    private @NotNull TextErrorHandler errorHandler = new Write_TextErrorHandler ();
    private @Nullable FormalNodes formalNodes;
    private @Nullable PXMLExtensionsHandler extensionsHandler = null;

    private final @NotNull EventStreamParser<N, R> parser = new DefaultEventStreamParser<>();


    public EventStreamParserBuilder ( @NotNull ParserEventHandler<N, R> eventHandler ) {

        this.eventHandler = eventHandler;
    }


    // setters

    public EventStreamParserBuilder<N, R> setEventHandler ( @NotNull ParserEventHandler<N, R> eventHandler ) {

        this.eventHandler = eventHandler;
        return this;
    }

    public EventStreamParserBuilder<N, R> setErrorHandler ( @NotNull TextErrorHandler errorHandler ) {

        this.errorHandler = errorHandler;
        return this;
    }

    public EventStreamParserBuilder<N, R> setFormalNodes ( @Nullable FormalNodes formalNodes ) {

        this.formalNodes = formalNodes;
        return this;
    }

    public EventStreamParserBuilder<N, R> setExtensionsHandler ( @Nullable PXMLExtensionsHandler extensionsHandler ) {

        this.extensionsHandler = extensionsHandler;
        return this;
    }


    // config

    public @NotNull EventStreamParserConfig<N, R> getConfig() {

        return new EventStreamParserConfig<> ( eventHandler, errorHandler, formalNodes, extensionsHandler );
    }


    // parse

    public void parseFile ( @NotNull File file ) throws TextReaderException, IOException {

        parser.parseFile ( file, getConfig() );
    }

    public void parseFile ( @NotNull Path path ) throws TextReaderException, IOException {

        parseFile ( path.toFile() );
    }

    public void parseFile ( @NotNull String path ) throws TextReaderException, IOException {

        parseFile ( new File ( path ) );
    }

    public void parseString ( @NotNull String string ) throws TextReaderException {

        parser.parseString ( string, getConfig() );
    }

    public void parseURL ( @NotNull URL url ) throws TextReaderException, IOException {

        parser.parseURL ( url, getConfig() );
    }

    public void parseURL ( @NotNull String url ) throws TextReaderException, IOException {

        parseURL ( new URL ( url ) );
    }

    public void parseReader ( @NotNull Reader reader, @Nullable TextResource resource ) throws TextReaderException {

        parser.parseReader ( reader, resource, getConfig() );
    }

    public void parseReader ( @NotNull Reader reader ) throws TextReaderException {

        parseReader ( reader, null );
    }
}
