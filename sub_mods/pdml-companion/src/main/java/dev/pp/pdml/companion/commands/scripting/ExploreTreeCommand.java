package dev.pp.pdml.companion.commands.scripting;

import dev.pp.pdml.utils.scripting.TreeExplorerScriptUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.datatype.CommonDataTypes;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspec.ParameterSpec;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.pjse.PjseConfig;
import dev.pp.pjse.util.SourceCodeFileUtil;
import dev.pp.core.text.resource.reader.TextResourceReader;

import java.nio.file.Path;

import static dev.pp.pdml.companion.commands.SharedParameterSpecs.OPTIONAL_PDML_INPUT_FILE;

public class ExploreTreeCommand {

    public static final @NotNull ParameterSpec<Path> EXPLORER_JAVA_SOURCE_CODE_FILE =
        ParameterSpec.builder (
            "explorer", CommonDataTypes.FILE_PATH )
            .alternativeName ( "e" )
            .documentation (
                "Explorer Java Source Code File",
                "The path of the file that contains the Java source code of the explorer.",
                "utils/my-explorer.java" )
            .build();

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_PDML_INPUT_FILE )
        .add ( EXPLORER_JAVA_SOURCE_CODE_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
        "explore_tree", ExploreTreeCommand::execute )
        .alternativeName ( "et" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Explore a PDML AST With Java Source Code",
            "",
            "explore data/data.pdml -e utils/my-explorer.java" )
        .build();

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.value ( OPTIONAL_PDML_INPUT_FILE );
        @NotNull Path explorerFile = parameters.nonNullValue ( EXPLORER_JAVA_SOURCE_CODE_FILE );
        // execute ( inputFile, outputFile, transformerFile );

        // DebugUtils.writeNameValue ( "inputFile", inputFile );

        boolean isOnlyJavaMethodBodyCode = SourceCodeFileUtil.isJavaSourceCodeSnippetFile ( explorerFile );
        // DebugUtils.writeNameValue ( "isOnlyJavaMethodBodyCode", isOnlyJavaMethodBodyCode );
        // TextResourceReader textResourceReader = TextFileReaderUtil.getUTF8FileOrStdinReader ( inputFile );
        // ExplorerUtil.explorePdmlFile ( inputFile, explorerFile, isOnlyJavaMethodBodyCode );
        TreeExplorerScriptUtil.exploreCode (
            TextResourceReader.createForOptionalFilePathOrStdin ( inputFile ),
            new TextResourceReader ( explorerFile ), isOnlyJavaMethodBodyCode, PjseConfig.DEFAULT_CONFIG );

        return null;
    }

/*
    public static void execute (
        @Nullable Path pdmlInputFile,
        @Nullable Path pdmlOutputFile,
        @NotNull Path javaSourceCodeTransformerFile ) throws Exception {

        // TODO
        assert pdmlInputFile != null;
        assert pdmlOutputFile != null;
        @NotNull TaggedNode originalRoot = PdmlTreeParserUtil.parseFileToTree ( pdmlInputFile );

        @Nullable TaggedNode transformedRoot =  FunctionalInterfaceUtil.executeMethodInSourceCodeFile (
            javaSourceCodeTransformerFile,
            PdmlTreeTransformer.class,
            new String[]{"node"},
            new Object[]{originalRoot},
            null,
            "pdml",
            "TransformerImpl",
            PjseConfig.DEFAULT_CONFIG );

        @Nullable TaggedNode transformedRoot = FunctionUtil.executeApplyMethodInSourceCodeFile (
            javaSourceCodeTransformerFile,
            "pdml.Transformer",
            originalRoot,
            "node",
            TaggedNode.class,
            TaggedNode.class,
            PjseConfig.DEFAULT_CONFIG );

        if ( transformedRoot != null ) {
            PdmlDataWriterUtil.writeToFile ( pdmlOutputFile, transformedRoot,
                new PdmlDataWriterConfig ( true, true ) );
        }
    }
*/
}
