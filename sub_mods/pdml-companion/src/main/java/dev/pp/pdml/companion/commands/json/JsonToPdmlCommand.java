package dev.pp.pdml.companion.commands.json;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.json.JsonToPdmlUtil;
import dev.pp.pdml.json.JsonTreeToPdmlTreeConverter;
import dev.pp.pdml.writer.node.PdmlNodeWriterConfig;
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

public class JsonToPdmlCommand {

    public static final @NotNull ParameterSpec<Path> OPTIONAL_JSON_INPUT_FILE =
        CommonParameterSpecs.optionalInputFile (
            "JSON Input File",
            "The path of the JSON input file.\n" + CommonParameterSpecs.OPTIONAL_INPUT_FILE_STDIN_DESCR,
            "input/data.json" );

    // TODO add config parameters

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_JSON_INPUT_FILE )
        .add ( OPTIONAL_PDML_OUTPUT_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
        "JSON_to_PDML", JsonToPdmlCommand::execute )
        .alternativeName ( "j2p" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Convert JSON to PDML",
            "Convert a JSON document to a PDML document.",
            "j2p input/data.json output/data.pdml" )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path jsonInputFile = parameters.value ( OPTIONAL_JSON_INPUT_FILE );
        @Nullable Path pdmlOutputFile = parameters.value ( OPTIONAL_PDML_OUTPUT_FILE );
        try ( TextResourceReader jsonReader =
                TextResourceReader.createForOptionalFilePathOrStdin ( jsonInputFile );
              TextResourceWriter pdmlWriter =
                TextResourceWriter.createForOptionalFilePathOrStdout ( pdmlOutputFile, true ) ) {

            JsonToPdmlUtil.readerToWriter (
                jsonReader, pdmlWriter,
                JsonTreeToPdmlTreeConverter.DEFAULT_CONFIG,
                PdmlNodeWriterConfig.DEFAULT_CONFIG,
                false );

            PdmlCommandsHelper.fileCreatedMessageToStdout ( pdmlWriter );
        }

        return null;
    }
}
