package dev.pdml.core.reader.extensions;

import dev.pdml.core.exception.PDMLDocumentException;
import dev.pdml.core.exception.PDMLDocumentSemanticException;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.handler.Write_TextErrorHandler;
import dev.pdml.core.reader.PDMLReaderImpl;
import dev.pdml.core.reader.PDMLReader;
import dev.pp.basics.annotations.NotNull;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PDMLExtensionsContext {

    private final @NotNull PDMLReader PDMLReader;
    private final @NotNull TextErrorHandler errorHandler;
    private final @Nullable ScriptingEnvironment scriptingEnvironment;

    private final @NotNull Map<String, String> declaredConstants;

    public PDMLExtensionsContext (
        @NotNull PDMLReader PDMLReader,
        @NotNull TextErrorHandler errorHandler,
        @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.PDMLReader = PDMLReader;
        this.errorHandler = errorHandler;
        this.scriptingEnvironment = scriptingEnvironment;

        this.declaredConstants = new HashMap<>();
    }

    public static PDMLExtensionsContext createForTests ( String code ) throws IOException {

        return new PDMLExtensionsContext (
            new PDMLReaderImpl ( code ),
            new Write_TextErrorHandler (),
            null );
    }

    public static PDMLExtensionsContext createForTests ( String code, @Nullable ScriptingEnvironment scriptingEnvironment )
        throws IOException {

        return new PDMLExtensionsContext (
            new PDMLReaderImpl ( code ),
            new Write_TextErrorHandler (),
            scriptingEnvironment );
    }

    public @NotNull PDMLReader getPDMLReader() { return PDMLReader; }

    public @NotNull TextErrorHandler getErrorHandler() { return errorHandler; }

    public @NotNull Map<String, String> getDeclaredConstants() { return declaredConstants; }

    public @Nullable ScriptingEnvironment getScriptingEnvironment() { return scriptingEnvironment; }

    public void handleCancelingError ( @NotNull String id, @NotNull String message, @Nullable TextToken token )
        throws PDMLDocumentException {

        throw new PDMLDocumentSemanticException ( id, message, token );
    }

    public void handleNonAbortingError (
        @NotNull String id, @NotNull String message, @Nullable TextToken token ) {

        errorHandler.handleNonAbortingError ( id, message, token );
    }

    public void handleWarning (
        @NotNull String id, @NotNull String message, @Nullable TextToken token ) {

        errorHandler.handleNonAbortingWarning ( id, message, token );
    }
}
