package dev.pdml.commands;

import dev.pdml.commands.pdmltoxml.PdmlToXMLCommand;
import dev.pdml.commands.scriptingapidoc.CreateCoreScriptingAPIDocCommand;
import dev.pdml.commands.scriptingapidoc.CreateExtensionsScriptingAPIDocCommand;
import dev.pdml.commands.standalone.PdmlToStandaloneCommand;
import dev.pp.basics.annotations.NotNull;
import dev.pp.commands.command.CommandSpecs;
import dev.pp.commands.picocli.PicocliCommandLineExecutor;

public class PdmlCommands {

    @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull CommandSpecs COMMAND_SPECS = new CommandSpecs ()
        .add ( PdmlToXMLCommand.COMMAND_SPEC )
        .add ( PdmlToStandaloneCommand.COMMAND_SPEC )
        .add ( CreateCoreScriptingAPIDocCommand.COMMAND_SPEC )
        .add ( CreateExtensionsScriptingAPIDocCommand.COMMAND_SPEC );


    public static int runCommand ( String[] args ) {

        return PicocliCommandLineExecutor.executeCommand ( args, "0.5.0", COMMAND_SPECS );
    }
}
