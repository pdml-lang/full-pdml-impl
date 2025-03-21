package dev.pp.pdml.companion.commands;

import dev.pp.pdml.companion.commands.html.PdmlToHtmlCommand;
import dev.pp.pdml.companion.commands.json.JsonToPdmlCommand;
import dev.pp.pdml.companion.commands.json.PdmlToJsonCommand;
import dev.pp.pdml.companion.commands.list.ListNodeNamesCommand;
import dev.pp.pdml.companion.commands.list.ListTextsCommand;
import dev.pp.pdml.companion.commands.tocore.PdmlToCorePdmlCommand;
import dev.pp.pdml.companion.commands.xml.PdmlToXMLCommand;
import dev.pp.pdml.companion.commands.scripting.ExploreTreeCommand;
import dev.pp.pdml.companion.commands.xml.XMLToPdmlCommand;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.commands.cli.CLICommands;
import dev.pp.core.commands.command.CommandSpecs;

public class PdmlCommands {

    public static final @NotNull String APP_NAME = "pdmlc";

    @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull CommandSpecs COMMAND_SPECS = new CommandSpecs()
        .add ( ListTextsCommand.COMMAND_SPEC, 10 )
        .add ( ListNodeNamesCommand.COMMAND_SPEC, 15 )
        .add ( CheckPdmlDocsCommand.COMMAND_SPEC, 17 )
        .add ( PdmlToCorePdmlCommand.COMMAND_SPEC, 20 )
        .add ( PdmlToHtmlCommand.COMMAND_SPEC, 30 )
        .add ( PdmlToJsonCommand.COMMAND_SPEC, 40 )
        .add ( PdmlToXMLCommand.COMMAND_SPEC, 50 )
        .add ( JsonToPdmlCommand.COMMAND_SPEC, 60 )
        .add ( XMLToPdmlCommand.COMMAND_SPEC, 70 )
        // .add ( CreateCoreScriptingAPIDocCommand.COMMAND_SPEC )
        // .add ( CreateExtensionsScriptingAPIDocCommand.COMMAND_SPEC )
        .add ( ExploreTreeCommand.COMMAND_SPEC, 100 )
        // .add ( TransformCommand.COMMAND_SPEC );

        .add ( PdmlInfoCommand.COMMAND_SPEC, 200 )
        .add ( PdmlVersionCommand.COMMAND_SPEC, 210 )

        .addCommandInfoCommand (
            APP_NAME,
            PdmlInfoCommand.COMMAND_SPEC.getName(),
            220 )
        .addCommandsInfoCommand (
            APP_NAME,
            230 )
        .addHelpCommand (
            APP_NAME,
            PdmlInfoCommand.COMMAND_SPEC.getName(),
            // TODO
            // "article.pml -o output/article.html",
            null,
            240 );


    public static int runCommand ( String[] args ) {

        // return PicocliCommandLineExecutor.executeCommand ( args, "0.5.0", COMMAND_SPECS );
        return CLICommands.runAndReturnExitStatus ( args, COMMAND_SPECS, APP_NAME );
    }
}
