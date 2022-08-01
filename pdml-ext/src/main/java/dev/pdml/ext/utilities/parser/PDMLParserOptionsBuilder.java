package dev.pdml.ext.utilities.parser;

import dev.pdml.core.data.formalNode.FormalPDMLNodes;
import dev.pdml.core.parser.PDMLParserOptions;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.core.reader.extensions.PDMLExtensionsHandler;
import dev.pdml.ext.commands.SharedDefaultOptions;
import dev.pdml.ext.extensions.PDMLExtensionsHandlerImpl;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.error.handler.TextErrorHandler;

public class PDMLParserOptionsBuilder<N, R> {

    private final @NotNull PDMLParserEventHandler<N, R> eventHandler;
    private @NotNull TextErrorHandler errorHandler = SharedDefaultOptions.createErrorHandler ();
    private @Nullable FormalPDMLNodes<?> formalNodes = null;
    private @Nullable PDMLExtensionsHandler extensionsHandler = new PDMLExtensionsHandlerImpl();
    private @Nullable ScriptingEnvironment scriptingEnvironment = new ScriptingEnvironmentImpl ( true );
    private boolean ignoreTextAfterEndOfRootNode = false;


    public PDMLParserOptionsBuilder ( @NotNull PDMLParserEventHandler<N, R> eventHandler ) {

        this.eventHandler = eventHandler;
    }


    public PDMLParserOptionsBuilder<N, R> errorHandler ( @NotNull TextErrorHandler errorHandler ) {

        this.errorHandler = errorHandler;
        return this;
    }

    public PDMLParserOptionsBuilder<N, R> formalNodes ( @Nullable FormalPDMLNodes<?> formalNodes ) {

        this.formalNodes = formalNodes;
        return this;
    }

    public PDMLParserOptionsBuilder<N, R> extensionsHandler ( @Nullable PDMLExtensionsHandler extensionsHandler ) {

        this.extensionsHandler = extensionsHandler;
        return this;
    }

    public PDMLParserOptionsBuilder<N, R> scriptingEnvironment ( @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.scriptingEnvironment = scriptingEnvironment;
        return this;
    }

    public PDMLParserOptionsBuilder<N, R> ignoreTextAfterEndOfRootNode ( boolean ignoreTextAfterEndOfRootNode ) {

        this.ignoreTextAfterEndOfRootNode = ignoreTextAfterEndOfRootNode;
        return this;
    }



    public PDMLParserOptions<N, R> build() {

        return new PDMLParserOptions<> (
            eventHandler,
            errorHandler,
            formalNodes,
            extensionsHandler,
            scriptingEnvironment,
            ignoreTextAfterEndOfRootNode );
    }
}
