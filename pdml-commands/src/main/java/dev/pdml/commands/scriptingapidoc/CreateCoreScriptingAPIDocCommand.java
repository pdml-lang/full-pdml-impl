package dev.pdml.commands.scriptingapidoc;

import dev.pdml.utils.scriptingapidoc.ScriptingAPIDocCreator;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.commands.command.CommandSpec;
import dev.pp.parameters.parameters.Parameters;

import java.nio.file.Path;

public class CreateCoreScriptingAPIDocCommand {

    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.builder (
        "create_core_scripting_API_doc", CreateCoreScriptingAPIDocCommand::execute )
        .alternativeName ( "cad" )
        .inputParameters ( ScriptingAPIDocParameters.SPECS )
        .documentation (
        "Create Core Scripting API Documentation",
            null,
            null )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @NotNull Path sourcesDirectory = parameters.nonNullValue ( ScriptingAPIDocParameters.SOURCE_DIRECTORY );
        @NotNull Path outputFile = parameters.nonNullValue ( ScriptingAPIDocParameters.OUTPUT_FILE );

        ScriptingAPIDocCreator.createCoreDoc ( sourcesDirectory, outputFile );

        return null;
    }
}
