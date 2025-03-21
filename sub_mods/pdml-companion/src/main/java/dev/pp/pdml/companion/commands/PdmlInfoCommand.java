package dev.pp.pdml.companion.commands;

import dev.pp.pdml.data.PdmlVersion;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.os.OSDirectories;
import dev.pp.core.basics.utilities.os.OSName;
import dev.pp.core.basics.utilities.string.HTextAlign;
import dev.pp.core.basics.utilities.string.StringAligner;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.parameters.parameters.Parameters;

public class PdmlInfoCommand {

    public static final @NotNull String NAME = "info";

    public static final @NotNull CommandSpec<Void,Void> COMMAND_SPEC = CommandSpec.<Void,Void>builder (
        NAME, PdmlInfoCommand::execute )
        .documentation (
             "Display Info About PDML",
            "Display info about PDML in the terminal.",
            PdmlCommands.APP_NAME + " " + NAME )
        .build();

    public static Void execute ( @Nullable Parameters<?> parameters ) {

        StringBuilder sb = new StringBuilder();

        append ( "Application tag", PdmlVersion.APPLICATION_NAME, sb );
        append ( "Short tag", PdmlVersion.APPLICATION_SHORT_NAME, sb );
        append ( "Version", PdmlVersion.VERSION, sb );
        append ( "Version date", PdmlVersion.VERSION_DATE, sb );
        // append ( "Shared data dir.", PMLCResources.ROOT_DIRECTORY.toString(), sb );
        append ( "Working dir.", OSDirectories.currentWorkingDirectory().toString(), sb );
        append ( "OS tag", OSName.name (), sb );
        append ( "Java version.", System.getProperty ( "java.version" ), sb );

        System.out.println ( sb.toString() );

        return null;
    }

    private static void append ( @NotNull String label, @NotNull String value, @NotNull StringBuilder sb ) {

        sb.append ( StringAligner.align ( label + ":", 20, HTextAlign.RIGHT ) );
        sb.append ( " " );
        sb.append ( value );
        sb.append ( StringConstants.OS_LINE_BREAK );
    }
}
