package dev.pdml.parser;

import dev.pdml.parser.eventhandler.PdmlParserEventHandler;
import dev.pdml.parser.nodespec.PdmlNodeSpecs;
import dev.pdml.reader.PdmlReaderOptions;
import dev.pdml.reader.extensions.PdmlExtensionsHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;

public record PdmlParserOptions<N, R> (
    @NotNull PdmlParserEventHandler<N, R> eventHandler,
    @NotNull TextInspectionMessageHandler errorHandler,
    @Nullable PdmlNodeSpecs<?> nodeSpecs,
    @Nullable PdmlExtensionsHandler extensionsHandler,
    @Nullable ScriptingEnvironment scriptingEnvironment,
    boolean ignoreTextAfterEndOfRootNode,
    boolean allowStandardAttributesStartSyntax,
    boolean allowAlternativeAttributesStartSyntax ) {


    public @NotNull PdmlReaderOptions toReaderOptions() {
        return new PdmlReaderOptions ( errorHandler, extensionsHandler, scriptingEnvironment );
    }

}


