package dev.pp.pdml.companion.commands.json;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.json.PdmlToJsonUtil;
import dev.pp.pdml.parser.PdmlParserConfig;
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

public class PdmlToJsonCommand {

    public static final @NotNull ParameterSpec<Path> OPTIONAL_JSON_OUTPUT_FILE =
        CommonParameterSpecs.optionalOutputFile (
            "JSON Output File",
            "The path of the JSON output file.\n" + CommonParameterSpecs.OPTIONAL_OUTPUT_FILE_STDOUT_DESCR,
            "output/data.json" );

    // TODO
    // add parameter boolean include_whitespace_nodes
    // add parameter boolean include_node_paths (default=no)
    // add parameter boolean use_pretty_printing (default=yes)
    // ? add parameter boolean open_file (default=no)

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_PDML_INPUT_FILE )
        .add ( OPTIONAL_JSON_OUTPUT_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
            "PDML_to_JSON", PdmlToJsonCommand::execute )
        .alternativeName ( "p2j" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Convert PDML to JSON",
            "Convert a PDML document to a JSON document.",
            "p2j input/data.pdml output/data.json" )
        .build();
        // TODO? Note: This command is not suitable to convert PML documents into a standalone PDML document.""",

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.value ( OPTIONAL_PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.value ( OPTIONAL_JSON_OUTPUT_FILE );

        try ( TextResourceReader pdmlReader =
                TextResourceReader.createForOptionalFilePathOrStdin ( inputFile );
              TextResourceWriter jsonWriter =
                TextResourceWriter.createForOptionalFilePathOrStdout ( outputFile, true ) ) {

            PdmlToJsonUtil.readerToWriter (
                pdmlReader, jsonWriter,
                PdmlParserConfig.defaultConfig(), true, true );

            PdmlCommandsHelper.fileCreatedMessageToStdout ( jsonWriter );
        }

        return null;
    }
}
