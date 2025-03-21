package dev.pp.pdml.companion.commands.xml;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.writer.node.PdmlNodeWriterConfig;
import dev.pp.pdml.xml.XMLToPdmlUtil;
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

public class XMLToPdmlCommand {

    public static final @NotNull ParameterSpec<Path> OPTIONAL_XML_INPUT_FILE =
        CommonParameterSpecs.optionalInputFile (
            "XML Input File",
            "The path of the XML input file.\n" + CommonParameterSpecs.OPTIONAL_INPUT_FILE_STDIN_DESCR,
            "input/data.xml" );

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_XML_INPUT_FILE )
        .add ( OPTIONAL_PDML_OUTPUT_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
        "XML_to_PDML", XMLToPdmlCommand::execute )
        .alternativeName ( "x2p" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Convert XML to PDML",
            "Convert an XML document into a PDML document",
            "x2p input/data.xml output/data.pdml" )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path xmlInputFile = parameters.value ( OPTIONAL_XML_INPUT_FILE );
        @Nullable Path pdmlOutputFile = parameters.value ( OPTIONAL_PDML_OUTPUT_FILE );
        try ( TextResourceReader xmlReader =
                TextResourceReader.createForOptionalFilePathOrStdin ( xmlInputFile );
              TextResourceWriter pdmlWriter =
                TextResourceWriter.createForOptionalFilePathOrStdout ( pdmlOutputFile, true ) ) {

            XMLToPdmlUtil.readerToWriter (
                xmlReader, pdmlWriter,
                PdmlNodeWriterConfig.DEFAULT_CONFIG );

            PdmlCommandsHelper.fileCreatedMessageToStdout ( pdmlWriter );
        }

        return null;
    }
}
