package dev.pdml.commands.standalone;

import dev.pdml.commands.pdmltoxml.PdmlToXMLParameters;
import dev.pdml.utils.StandaloneUtils;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.commands.command.CommandSpec;
import dev.pp.parameters.parameters.Parameters;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public class PdmlToStandaloneCommand {

    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.builder (
        "PDML_to_standalone", PdmlToStandaloneCommand::execute )
        .alternativeName ( "pdml2sa" )
        .inputParameters ( StandaloneParameters.SPECS )
        .documentation ( "Create Standalone PDML",
            """
            Convert a PDML document into a standalone PDML document.
            Note: This command is not suitable to convert PML documents into a standalone PDML document.""",
            "pdml pdml2sa input/book.pml output/standalone_book.pml" )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.value ( PdmlToXMLParameters.PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.value ( PdmlToXMLParameters.XML_OUTPUT_FILE );

        @Nullable Reader reader = null;
        @Nullable Writer writer = null;
        try {
            reader = TextFileIO.getUTF8FileOrStdinReader ( inputFile );
            writer = TextFileIO.getUTF8FileOrStdoutWriter ( outputFile );

            StandaloneUtils.createStandalone ( reader, null, writer );
        } finally {
            TextFileIO.closeIfFileReader ( reader );
            TextFileIO.closeIfFileWriter ( writer );
        }

        return null;
    }
}
