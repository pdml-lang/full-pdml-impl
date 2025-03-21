package dev.pp.pdml.companion.commands.list;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.utils.lists.NodeNamesWriterUtil;
import dev.pp.pdml.utils.lists.TextLeavesWriterUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.datatype.CommonDataTypes;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspec.ParameterSpec;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;

import java.nio.file.Path;

import static dev.pp.pdml.companion.commands.SharedParameterSpecs.*;

public class ListTextsCommand {

    static final @NotNull ParameterSpec<Boolean> SORT_PARAMETER =
        ParameterSpec.builder
            ("sort", CommonDataTypes.BOOLEAN )
            .alternativeName ( "s" )
            .defaultValue ( false )
            .documentation (
                "Sort",
                "Sort alphabetically",
                "--sort true" )
            .build();

    static final @NotNull ParameterSpec<Boolean> DISTINCT_PARAMETER =
        ParameterSpec.builder
            ("distinct", CommonDataTypes.BOOLEAN )
            .alternativeName ( "d" )
            .defaultValue ( false )
            .documentation (
                "Distinct",
                "List only distinct values (i.e. if the same value occurs several times, only the first occurrence is included in the list).",
                "--distinct true" )
            .build();

    static final @NotNull ParameterSpec<String> SEPARATOR_PARAMETER =
        ParameterSpec.builder
            ("separator", CommonDataTypes.STRING_OR_NULL )
            .alternativeName ( "p" )
            .defaultValue ( StringConstants.OS_LINE_BREAK )
            .documentation (
                "Separator",
                "The separator used to separate list elements (default is line break).",
                "--separator \", \"" )
            .build();

    @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull ParameterSpecs PARAMETERS = new MutableParameterSpecs()
        .add ( OPTIONAL_PDML_INPUT_FILE )
        .add ( OPTIONAL_TEXT_OUTPUT_FILE )
        .add ( SORT_PARAMETER )
        .add ( DISTINCT_PARAMETER )
        .add ( SEPARATOR_PARAMETER )
        .makeImmutable();


    @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull CommandSpec COMMAND_SPEC = CommandSpec.builder (
        "list_texts", ListTextsCommand::execute )
        .alternativeName ( "lt" )
        .inputParameters ( PARAMETERS )
        .documentation ( "List Text Leaves in a PDML Document",
            "Create a list of text leaves contained in a PDML document.",
            "lt -i input/document.pdml -o output/texts.txt" )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {
        return executeForTextsOrNames ( false, parameters );
    }

    public static Void executeForTextsOrNames (
        boolean forNames,
        @Nullable Parameters<?> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.castedValue ( OPTIONAL_PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.castedValue ( OPTIONAL_TEXT_OUTPUT_FILE );
        boolean sort = parameters.nonNullCastedValue ( SORT_PARAMETER );
        boolean distinct = parameters.nonNullCastedValue ( DISTINCT_PARAMETER );
        @Nullable String separator = parameters.castedValue ( SEPARATOR_PARAMETER );

        try ( TextResourceReader pdmlReader =
                  TextResourceReader.createForOptionalFilePathOrStdin ( inputFile );
              TextResourceWriter textWriter =
                  TextResourceWriter.createForOptionalFilePathOrStdout ( outputFile, true ) ) {

            if ( forNames ) {
                NodeNamesWriterUtil.writeNames (
                    pdmlReader, PdmlParserConfig.defaultConfig(),
                    textWriter, separator,
                    sort, distinct );
            } else {
                TextLeavesWriterUtil.writeTexts (
                    pdmlReader, PdmlParserConfig.defaultConfig (),
                    textWriter, separator,
                    sort, distinct );
            }

            PdmlCommandsHelper.fileCreatedMessageToStdout ( textWriter );
        }

        return null;
    }
}
