package dev.pp.pdml.companion.cli;

import dev.pp.pdml.companion.commands.PdmlCommands;
import dev.pp.core.basics.utilities.SimpleLogger;

public class Main {

    public static void main ( String[] args ) {

        init();
        int exitCode = PdmlCommands.runCommand ( args );
        System.exit ( exitCode );
    }

    private static void init() {

        SimpleLogger.useSimpleFormat();
        // SimpleLogger.setLevel ( SimpleLogger.LogLevel.DEBUG );
    }
}
