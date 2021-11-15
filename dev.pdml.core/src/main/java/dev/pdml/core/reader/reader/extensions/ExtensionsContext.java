package dev.pdml.core.reader.reader.extensions;

import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.handler.Write_TextErrorHandler;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.reader.DefaultPXMLReader;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.token.TextToken;

import java.util.HashMap;
import java.util.Map;

public class ExtensionsContext {

    private final @NotNull PXMLReader pXMLReader;
    private final @NotNull TextErrorHandler errorHandler;

    private final @NotNull Map<String, String> declaredConstants;

    public ExtensionsContext (
        @NotNull PXMLReader pXMLReader,
        @NotNull TextErrorHandler errorHandler ) {

        this.pXMLReader = pXMLReader;
        this.errorHandler = errorHandler;

        this.declaredConstants = new HashMap<>();
    }

    public static ExtensionsContext createForTests ( String code ) throws PXMLResourceException {

        return new ExtensionsContext (
            new DefaultPXMLReader ( code ),
            new Write_TextErrorHandler () );
    }

    public @NotNull PXMLReader getPXMLReader() { return pXMLReader; }

    public @NotNull TextErrorHandler getErrorHandler() { return errorHandler; }

    public @NotNull Map<String, String> getDeclaredConstants() { return declaredConstants; }

    public void handleNonCancelingError ( @NotNull String id, @NotNull String message, @Nullable TextToken token ) {

        errorHandler.handleError ( id, message, token );
    }

    public void handleWarning ( @NotNull String id, @NotNull String message, @Nullable TextToken token ) {

        errorHandler.handleWarning ( id, message, token );
    }
}
