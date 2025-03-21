package dev.pp.pdml.companion.commands.tocore;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.utils.PdmlToCorePdmlUtil;
import dev.pp.pdml.writer.node.PdmlNodeWriterConfig;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;

import java.nio.file.Path;

import static dev.pp.pdml.companion.commands.SharedParameterSpecs.OPTIONAL_PDML_INPUT_FILE;
import static dev.pp.pdml.companion.commands.SharedParameterSpecs.OPTIONAL_PDML_OUTPUT_FILE;

public class PdmlToCorePdmlCommand {

    // TODO add parameter keep_attributes

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_PDML_INPUT_FILE )
        .add ( OPTIONAL_PDML_OUTPUT_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
            "PDML_to_Core_PDML", PdmlToCorePdmlCommand::execute )
        .alternativeName ( "p2c" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Convert PDML to Core PDML",
            "Convert a PDML document to a Core PDML document (i.e. a standalone PDML document that doesn't use PDML options)",
            """
                p2r input/doc.pdml output/core_doc.pdml
                Explicit version:
                PDML_to_Core_PDML --input input/doc.pdml --output output/core_doc.pdml
                Read from STDIN and write to STDOUT (short version):
                p2r - -
                Read from STDIN and write to STDOUT (explicit version):
                p2r STDIN STDOUT
                """ )
        .build();
        // TODO? Note: This command is not suitable to convert PML documents into a standalone PDML document.""",

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.value ( OPTIONAL_PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.value ( OPTIONAL_PDML_OUTPUT_FILE );

        try ( TextResourceReader pdmlReader =
                TextResourceReader.createForOptionalFilePathOrStdin ( inputFile );
              TextResourceWriter pdmlWriter =
                TextResourceWriter.createForOptionalFilePathOrStdout ( outputFile, true ) ) {

            PdmlToCorePdmlUtil.pdmlCodeToCorePdml (
                pdmlReader, pdmlWriter,
                PdmlParserConfig.defaultConfig(), PdmlNodeWriterConfig.DEFAULT_CONFIG,
                false );

            PdmlCommandsHelper.fileCreatedMessageToStdout ( pdmlWriter );
        }

        return null;
    }
}
