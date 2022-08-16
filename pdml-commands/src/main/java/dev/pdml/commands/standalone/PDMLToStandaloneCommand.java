package dev.pdml.commands.standalone;

import dev.pdml.commands.pdmltoxml.PDMLToXMLFormalParameters;
import dev.pdml.ext.utilities.StandaloneUtils;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.commands.command.FormalCommand;
import dev.pp.parameters.parameter.Parameters;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

public class PDMLToStandaloneCommand {

    public static @NotNull FormalCommand<Void> COMMAND = FormalCommand.builder (
        "PDML_to_standalone", PDMLToStandaloneCommand::execute )
        .alternativeName ( "pdml2sa" )
        .inputParameters ( StandaloneFormalParameters.FORMAL_PARAMETERS )
        .documentation ( "Create Standalone PDML",
            """
            Convert a PDML document into a standalone PDML document.
            Note: This command is not suitable to convert PML documents into a standalone PDML document.""",
            "pdml pdml2sa input/book.pml output/standalone_book.pml" )
        .build();

    public static Void execute (
        @Nullable Map<String, String> stringParameters,
        @Nullable Parameters parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.getNullable ( PDMLToXMLFormalParameters.PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.getNullable ( PDMLToXMLFormalParameters.XML_OUTPUT_FILE );

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
