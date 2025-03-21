package dev.pp.pdml.companion.commands;

import dev.pp.pdml.data.PdmlVersion;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.parameters.parameters.Parameters;

public class PdmlVersionCommand {

    public static final @NotNull String NAME = "version";

    public static final @NotNull CommandSpec<Void,Void> COMMAND_SPEC = CommandSpec.<Void,Void>builder (
        NAME, PdmlVersionCommand::execute )
        .documentation (
             "Display the PDML Version",
            "Write the PDML version number to the standard output device.",
            PdmlCommands.APP_NAME + " " + NAME )
        .build();

    public static Void execute ( @Nullable Parameters<?> parameters ) {

        System.out.println ( PdmlVersion.VERSION_TEXT );

        return null;
    }
}
