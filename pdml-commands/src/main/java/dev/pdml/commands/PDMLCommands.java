package dev.pdml.commands;

import dev.pdml.commands.pdmltoxml.PDMLToXMLCommand;
import dev.pdml.commands.scriptingapidoc.CreateCoreScriptingAPIDocCommand;
import dev.pdml.commands.scriptingapidoc.CreateExtensionsScriptingAPIDocCommand;
import dev.pdml.commands.standalone.StandaloneCommand;
import dev.pp.basics.annotations.NotNull;
import dev.pp.commands.command.FormalCommands;
import dev.pp.commands.picocli.PicocliCommandLineExecutor;

public class PDMLCommands {

    public static @NotNull FormalCommands COMMANDS = new FormalCommands()
        .add ( PDMLToXMLCommand.COMMAND )
        .add ( StandaloneCommand.COMMAND )
        .add ( CreateCoreScriptingAPIDocCommand.COMMAND )
        .add ( CreateExtensionsScriptingAPIDocCommand.COMMAND );


    public static int runCommand ( String[] args ) {

        return PicocliCommandLineExecutor.executeCommand ( args, "0.5.0", COMMANDS );
    }
}
