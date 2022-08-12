package dev.pdml.commands.pdmltoxml;

import dev.pdml.ext.commands.SharedDefaultOptions;
import dev.pdml.ext.utilities.xml.PDMLToXMLConverter;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.basics.utilities.directory.DirectoryCreator;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.commands.command.FormalCommand;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.text.resource.File_TextResource;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class PDMLToXMLCommand {

    public static @NotNull FormalCommand<Void> COMMAND = FormalCommand.builder (
        "PDML_to_XML", PDMLToXMLCommand::execute )
        .alternativeNames ( Set.of ( "pdml2xml" ) )
        .inputParameters ( PDMLToXMLFormalParameters.FORMAL_PARAMETERS )
        .documentation ( "Convert PDML to XML",
            """
            Convert a PDML document into a standalone XML document.
            Note: This command is not suitable to convert PML documents into XML.""",
            "pdml pdml2xml input/doc.pdml output/doc.xml" )
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

            PDMLToXMLConfig config = new PDMLToXMLConfig (
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


    public static void execute ( @NotNull PDMLToXMLConfig config ) throws Exception {

        @Nullable Path XMLOutputDirectory = config.XMLOutputDirectory();
        if ( XMLOutputDirectory != null ) {
            DirectoryCreator.createWithParentsIfNotExists ( XMLOutputDirectory );
        }

        PDMLToXMLConverter.pipePDMLReaderToXMLWriter (
            config.PMLInputReader(),
            config.XMLOutputWriter(),
            config.PMLInputTextResource() );

        if ( XMLOutputDirectory != null ) {
            SimpleLogger.info ( "Output stored in directory " + XMLOutputDirectory + "." );
        }
    }
}
