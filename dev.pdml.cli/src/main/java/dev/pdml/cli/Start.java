package dev.pdml.cli;

import dev.pdml.cli.commands.LogParserEvents;
import dev.pdml.cli.commands.PXMLToXML;
import dev.pdml.cli.commands.Standalone;
import picocli.CommandLine;
import picocli.CommandLine.Command;

// TODO add XMLToPXML
@Command (
    subcommands = {
        Standalone.class,
        PXMLToXML.class,
        LogParserEvents.class,
        CommandLine.HelpCommand.class
    },
    name = "pxml",
    version = "0.5.0",
    mixinStandardHelpOptions = true
)
public class Start {

    public static void main ( String[] args ) {

        int exitCode = new CommandLine ( new Start() ).execute ( args );
        System.exit ( exitCode );
    }
}
