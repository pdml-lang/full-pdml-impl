package dev.pdml.reader.extensions;

import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.exception.PdmlDocumentException;
import dev.pdml.shared.exception.PdmlDocumentSemanticException;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.message.TextWarning;
import dev.pp.text.token.TextToken;

import java.util.HashMap;
import java.util.Map;

public class PdmlExtensionsContext {


    private final @NotNull PdmlReader pdmlReader;
    public @NotNull PdmlReader getPdmlReader() { return pdmlReader; }

    private final @NotNull TextInspectionMessageHandler errorHandler;
    public @NotNull TextInspectionMessageHandler getErrorHandler() { return errorHandler; }

    private final @Nullable ScriptingEnvironment scriptingEnvironment;
    public @Nullable ScriptingEnvironment getScriptingEnvironment() { return scriptingEnvironment; }

    private final @NotNull Map<String, String> declaredConstants;
    public @NotNull Map<String, String> getDeclaredConstants() { return declaredConstants; }


    public PdmlExtensionsContext (
        @NotNull PdmlReader PdmlReader,
        @NotNull TextInspectionMessageHandler errorHandler,
        @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.pdmlReader = PdmlReader;
        this.errorHandler = errorHandler;
        this.scriptingEnvironment = scriptingEnvironment;

        this.declaredConstants = new HashMap<>();
    }


    public void handleCancelingError ( @NotNull String message, @NotNull String id, @Nullable TextToken token )
        throws PdmlDocumentException {

        throw new PdmlDocumentSemanticException ( message, id, token );
    }

    public void handleNonAbortingError (
        @NotNull String message, @NotNull String id, @Nullable TextToken token ) {

        errorHandler.handleMessage ( new TextError ( message, id, token ) );
    }

    public void handleWarning (
        @NotNull String message, @NotNull String id, @Nullable TextToken token ) {

        errorHandler.handleMessage ( new TextWarning ( message, id, token ) );
    }
}
