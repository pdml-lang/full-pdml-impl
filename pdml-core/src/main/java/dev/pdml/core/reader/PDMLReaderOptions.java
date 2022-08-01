package dev.pdml.core.reader;

import dev.pdml.core.parser.PDMLParserOptions;
import dev.pdml.core.reader.extensions.PDMLExtensionsHandler;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.texttable.writer.pretty.utilities.TextErrorOrWarning_FormWriter;

public record PDMLReaderOptions(
    @NotNull TextErrorHandler errorHandler,
    @Nullable PDMLExtensionsHandler extensionsHandler,
    @Nullable ScriptingEnvironment scriptingEnvironment ) {

    // TODO? add builder

    public PDMLReaderOptions() {

        this ( TextErrorOrWarning_FormWriter.createLogErrorHandler(), null, null );
    }

    public PDMLReaderOptions ( @NotNull PDMLParserOptions<?,?> parserOptions ) {

        this (
            parserOptions.errorHandler(),
            parserOptions.extensionsHandler(),
            parserOptions.scriptingEnvironment() );
    }
}
