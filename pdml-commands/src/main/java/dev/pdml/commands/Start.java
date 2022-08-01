package dev.pdml.commands;

import dev.pp.basics.utilities.SimpleLogger;

public class Start {

    public static void main ( String[] args ) {

        init();
        int exitCode = PDMLCommands.runCommand ( args );
        System.exit ( exitCode );
    }

    private static void init() {

        SimpleLogger.useSimpleFormat();
        // SimpleLogger.setLevel ( SimpleLogger.LogLevel.DEBUG );
    }
}
