package dev.pdml.commands.scriptingapidoc;

import dev.pdml.ext.utilities.scriptingapidoc.ScriptingAPIDocCreator;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.commands.command.FormalCommand;
import dev.pp.parameters.parameter.list.Parameters;

import java.nio.file.Path;
import java.util.Map;

public class CreateExtensionsScriptingAPIDocCommand {

    public static @NotNull FormalCommand<Void> COMMAND = FormalCommand.builder (
        "create_extensions_scripting_API_doc", CreateExtensionsScriptingAPIDocCommand::execute )
        .alternativeName ( "ead" )
        .inputParameters ( ScriptingAPIDocFormalParameters.PARAMETERS )
        .documentation (
        "Create Extensions Scripting API Documentation",
            null,
            null )
        .build();

    public static Void execute (
        @Nullable Map<String, String> stringParameters,
        @Nullable Parameters parameters ) throws Exception {

        assert parameters != null;

        @NotNull Path sourcesDirectory = parameters.getNonNull ( ScriptingAPIDocFormalParameters.SOURCE_DIRECTORY );
        @NotNull Path outputFile = parameters.getNonNull ( ScriptingAPIDocFormalParameters.OUTPUT_FILE );

        ScriptingAPIDocCreator.createExtensionsDoc ( sourcesDirectory, outputFile );

        return null;
    }
}
