package dev.pdml.utils.parser;

import dev.pdml.ext.PdmlExtensionsHandlerImpl;
import dev.pdml.parser.PdmlParserOptions;
import dev.pdml.parser.eventhandler.PdmlParserEventHandler;
import dev.pdml.parser.nodespec.PdmlNodeSpecs;
import dev.pdml.reader.extensions.PdmlExtensionsHandler;
import dev.pdml.utils.SharedDefaultOptions;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;

public class PdmlParserOptionsBuilder<N, R> {

    private final @NotNull PdmlParserEventHandler<N, R> eventHandler;
    private @NotNull TextInspectionMessageHandler errorHandler = SharedDefaultOptions.createErrorHandler ();
    private @Nullable PdmlNodeSpecs<?> nodeSpecs = null;
    private @Nullable PdmlExtensionsHandler extensionsHandler = new PdmlExtensionsHandlerImpl ();
    private @Nullable ScriptingEnvironment scriptingEnvironment = new ScriptingEnvironmentImpl ( true );
    private boolean ignoreTextAfterEndOfRootNode = false;
    private boolean allowStandardAttributesStartSyntax = true;
    private boolean allowAlternativeAttributesStartSyntax = false;



    public PdmlParserOptionsBuilder ( @NotNull PdmlParserEventHandler<N, R> eventHandler ) {

        this.eventHandler = eventHandler;
    }


    public PdmlParserOptionsBuilder<N, R> errorHandler ( @NotNull TextInspectionMessageHandler errorHandler ) {

        this.errorHandler = errorHandler;
        return this;
    }

    public PdmlParserOptionsBuilder<N, R> nodeSpecs ( @Nullable PdmlNodeSpecs<?> nodeSpecs ) {

        this.nodeSpecs = nodeSpecs;
        return this;
    }

    public PdmlParserOptionsBuilder<N, R> extensionsHandler ( @Nullable PdmlExtensionsHandler extensionsHandler ) {

        this.extensionsHandler = extensionsHandler;
        return this;
    }

    public PdmlParserOptionsBuilder<N, R> scriptingEnvironment ( @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.scriptingEnvironment = scriptingEnvironment;
        return this;
    }

    public PdmlParserOptionsBuilder<N, R> ignoreTextAfterEndOfRootNode ( boolean ignoreTextAfterEndOfRootNode ) {

        this.ignoreTextAfterEndOfRootNode = ignoreTextAfterEndOfRootNode;
        return this;
    }

    public PdmlParserOptionsBuilder<N, R> allowStandardAttributesStartSyntax ( boolean allowStandardAttributesStartSyntax ) {

        this.allowStandardAttributesStartSyntax = allowStandardAttributesStartSyntax;
        return this;
    }

    public PdmlParserOptionsBuilder<N, R> allowAlternativeAttributesStartSyntax ( boolean allowAlternativeAttributesStartSyntax ) {

        this.allowAlternativeAttributesStartSyntax = allowAlternativeAttributesStartSyntax;
        return this;
    }



    public PdmlParserOptions<N, R> build() {

        return new PdmlParserOptions<> (
            eventHandler,
            errorHandler,
            nodeSpecs,
            extensionsHandler,
            scriptingEnvironment,
            ignoreTextAfterEndOfRootNode,
            allowStandardAttributesStartSyntax,
            allowAlternativeAttributesStartSyntax );
    }
}
