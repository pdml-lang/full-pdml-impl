package dev.pp.pdml.companion.commands.xml;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.parser.PdmlParserConfigBuilder;
import dev.pp.pdml.xml.PdmlToXMLUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspec.CommonParameterSpecs;
import dev.pp.core.parameters.parameterspec.ParameterSpec;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;

import java.nio.file.Path;

import static dev.pp.pdml.companion.commands.SharedParameterSpecs.*;

public class PdmlToXMLCommand {

    public static final @NotNull ParameterSpec<Path> OPTIONAL_XML_OUTPUT_FILE =
        CommonParameterSpecs.optionalOutputFile (
            "XML Output File",
            "The path of the XML output file.\n" + CommonParameterSpecs.OPTIONAL_OUTPUT_FILE_STDOUT_DESCR,
            "output/data.xml" );

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_PDML_INPUT_FILE )
        .add ( OPTIONAL_XML_OUTPUT_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
            "PDML_to_XML", PdmlToXMLCommand::execute )
        .alternativeName ( "p2x" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Convert PDML to XML",
            "Convert a PDML document to an XML document.",
            "p2x input/data.pdml output/data.xml" )
        .build();
        // TODO? Note: This command is not suitable to convert PML documents into a standalone PDML document.""",

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.value ( OPTIONAL_PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.value ( OPTIONAL_XML_OUTPUT_FILE );

        try ( TextResourceReader pdmlReader =
                TextResourceReader.createForOptionalFilePathOrStdin ( inputFile );
              TextResourceWriter xmlWriter =
                TextResourceWriter.createForOptionalFilePathOrStdout ( outputFile, true ) ) {

            PdmlToXMLUtil.readerToWriter (
                pdmlReader, xmlWriter,
                // PdmlParserConfig.defaultConfig() );
                new PdmlParserConfigBuilder().ignoreComments ( false ).build() );

            PdmlCommandsHelper.fileCreatedMessageToStdout ( xmlWriter );
        }

        return null;
    }
}
