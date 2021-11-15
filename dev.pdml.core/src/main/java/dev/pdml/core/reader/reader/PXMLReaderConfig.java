package dev.pdml.core.reader.reader;

import dev.pdml.core.reader.parser.EventStreamParserConfig;
import dev.pdml.core.reader.reader.extensions.PXMLExtensionsHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.handler.Write_TextErrorHandler;
import dev.pp.text.resource.TextResource;

public class PXMLReaderConfig {

    private final @Nullable TextResource resource;
    private final @NotNull TextErrorHandler errorHandler;
    private final @Nullable PXMLExtensionsHandler extensionsHandler;

    public PXMLReaderConfig (
        @Nullable TextResource resource,
        @NotNull TextErrorHandler errorHandler,
        @Nullable PXMLExtensionsHandler extensionsHandler ) {

        this.resource = resource;
        this.errorHandler = errorHandler;
        this.extensionsHandler = extensionsHandler;
    }

    public PXMLReaderConfig (
        @Nullable TextResource resource,
        @NotNull EventStreamParserConfig<?,?> parserConfig ) {

        this.resource = resource;
        this.errorHandler = parserConfig.getErrorHandler();
        this.extensionsHandler = parserConfig.getExtensionsHandler();
    }

    public PXMLReaderConfig ( @Nullable TextResource resource ) {

        this ( resource, new Write_TextErrorHandler (), null );
    }


    public @Nullable TextResource getResource() { return resource; }

    public @NotNull TextErrorHandler getErrorHandler() { return errorHandler; }

    public @Nullable PXMLExtensionsHandler getExtensionsHandler() { return extensionsHandler; }
}
