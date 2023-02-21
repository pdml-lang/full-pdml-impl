package dev.pdml.commands.pdmltoxml;

import dev.pdml.utils.SharedDefaultOptions;
import dev.pdml.xml.PdmlToXMLConverter;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.basics.utilities.directory.DirectoryCreator;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.commands.command.CommandSpec;
import dev.pp.parameters.parameters.Parameters;
import dev.pp.text.resource.File_TextResource;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Set;

public class PdmlToXMLCommand {

    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
        "PDML_to_XML", PdmlToXMLCommand::execute )
        .alternativeNames ( Set.of ( "pdml2xml" ) )
        .inputParameters ( PdmlToXMLParameters.SPECS )
        .documentation ( "Convert PDML to XML",
            """
            Convert a PDML document into a standalone XML document.
            Note: This command is not suitable to convert PML documents into XML.""",
            "pdml pdml2xml input/doc.pdml output/doc.xml" )
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

            PdmlToXMLConfig config = new PdmlToXMLConfig (
                reader,
                inputFile != null ? new File_TextResource ( inputFile ) : null,
                outputFile != null ? outputFile.getParent() : null,
                writer,
                SharedDefaultOptions.createErrorHandler (),
                SharedDefaultOptions.VERBOSITY,
                null );
            execute ( config );
        } finally {
            TextFileIO.closeIfFileReader ( reader );
            TextFileIO.closeIfFileWriter ( writer );
        }

        return null;
    }


    public static void execute ( @NotNull PdmlToXMLConfig config ) throws Exception {

        @Nullable Path XMLOutputDirectory = config.XMLOutputDirectory();
        if ( XMLOutputDirectory != null ) {
            DirectoryCreator.createWithParentsIfNotExists ( XMLOutputDirectory );
        }

        PdmlToXMLConverter.pipePDMLReaderToXMLWriter (
            config.PMLInputReader(),
            config.XMLOutputWriter(),
            config.PMLInputTextResource() );

        if ( XMLOutputDirectory != null ) {
            SimpleLogger.info ( "Output stored in directory " + XMLOutputDirectory + "." );
        }
    }
}
