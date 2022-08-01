package dev.pdml.core.parser;

import dev.pdml.core.data.formalNode.FormalPDMLNodes;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.core.reader.extensions.PDMLExtensionsHandler;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;

public record PDMLParserOptions<N, R> (
    @NotNull PDMLParserEventHandler<N, R> eventHandler,
    @NotNull TextErrorHandler errorHandler,
    @Nullable FormalPDMLNodes formalNodes,
    @Nullable PDMLExtensionsHandler extensionsHandler,
    @Nullable ScriptingEnvironment scriptingEnvironment,
    boolean ignoreTextAfterEndOfRootNode ) {}


