package dev.pp.pdml.companion.commands.list;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.parameters.parameters.Parameters;

import java.nio.file.Path;

public class ListNodeNamesCommand {

    @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull CommandSpec COMMAND_SPEC = CommandSpec.builder (
        "list_node_names", ListNodeNamesCommand::execute )
        .alternativeName ( "ln" )
        .inputParameters ( ListTextsCommand.PARAMETERS )
        .documentation ( "List Node Names in a PDML Document",
            "Create a list of node names contained in a PDML document.",
            "ln -i input/document.pdml -o output/names.txt" )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {
        return ListTextsCommand.executeForTextsOrNames ( true, parameters );
    }
}
