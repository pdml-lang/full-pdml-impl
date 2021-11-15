package dev.pdml.core.reader.parser;

import dev.pdml.core.data.formalNode.FormalNodes;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pdml.core.reader.reader.extensions.PXMLExtensionsHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;

public class EventStreamParserConfig<N, R> {

    private final @NotNull ParserEventHandler<N, R> eventHandler;
    private final @NotNull TextErrorHandler errorHandler;
    private final @Nullable FormalNodes formalNodes;
    private final @Nullable PXMLExtensionsHandler extensionsHandler;

    public EventStreamParserConfig (
        @NotNull ParserEventHandler<N, R> eventHandler,
        @NotNull TextErrorHandler errorHandler,
        @Nullable FormalNodes formalNodes,
        @Nullable PXMLExtensionsHandler extensionsHandler ) {

        this.eventHandler = eventHandler;
        this.errorHandler = errorHandler;
        this.formalNodes = formalNodes;
        this.extensionsHandler = extensionsHandler;
    }

    public @NotNull ParserEventHandler<N, R> getEventHandler() { return eventHandler; }

    public @NotNull TextErrorHandler getErrorHandler() { return errorHandler; }

    public @Nullable FormalNodes getFormalNodes() { return formalNodes; }

    public @Nullable PXMLExtensionsHandler getExtensionsHandler() { return extensionsHandler; }
}
