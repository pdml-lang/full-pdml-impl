package dev.pdml.reader;

import dev.pdml.reader.extensions.PdmlExtensionsHandler;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.texttable.writer.pretty.utilities.TextInspectionMessage_FormWriter;

public record PdmlReaderOptions(
    @NotNull TextInspectionMessageHandler errorHandler,
    @Nullable PdmlExtensionsHandler extensionsHandler,
    @Nullable ScriptingEnvironment scriptingEnvironment ) {


    public PdmlReaderOptions() {

        this ( TextInspectionMessage_FormWriter.createLogMessageHandler(), null, null );
    }
}
