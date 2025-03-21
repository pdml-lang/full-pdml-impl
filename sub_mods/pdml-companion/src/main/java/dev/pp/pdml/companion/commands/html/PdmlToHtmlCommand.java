package dev.pp.pdml.companion.commands.html;

import dev.pp.pdml.companion.commands.PdmlCommandsHelper;
import dev.pp.pdml.html.treeview.PdmlToHtmlTreeViewUtil;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.gui.DesktopUtil;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspec.CommonParameterSpecs;
import dev.pp.core.parameters.parameterspec.ParameterSpec;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;

import java.nio.file.Path;

import static dev.pp.pdml.companion.commands.SharedParameterSpecs.*;

public class PdmlToHtmlCommand {

    public static final @NotNull ParameterSpec<Path> OPTIONAL_HTML_OUTPUT_FILE =
        CommonParameterSpecs.optionalOutputFile (
            "HTML Output File",
            "The path of the HTML output file.\n" + CommonParameterSpecs.OPTIONAL_OUTPUT_FILE_STDOUT_DESCR,
            "output/tree_view.html" );

    // TODO
    // add parameter boolean display_whitespace_nodes
    // add parameter boolean open_browser

    public static final @NotNull ParameterSpecs<Path> PARAMETERS = new MutableParameterSpecs<Path> ()
        .add ( OPTIONAL_PDML_INPUT_FILE )
        .add ( OPTIONAL_HTML_OUTPUT_FILE )
        .makeImmutable();


    public static final @NotNull CommandSpec<Path,Void> COMMAND_SPEC = CommandSpec.<Path,Void>builder (
            "PDML_to_HTML", PdmlToHtmlCommand::execute )
        .alternativeName ( "p2h" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Convert PDML to HTML Tree View",
            "Convert a PDML document to an HTML document that displays the PDML data as a tree view.",
            "p2h input/doc.pdml output/tree_view.html" )
        .build();
        // TODO? Note: This command is not suitable to convert PML documents into a standalone PDML document.""",

    public static Void execute ( @Nullable Parameters<Path> parameters ) throws Exception {

        assert parameters != null;

        @Nullable Path inputFile = parameters.value ( OPTIONAL_PDML_INPUT_FILE );
        @Nullable Path outputFile = parameters.value ( OPTIONAL_HTML_OUTPUT_FILE );

        try ( TextResourceReader pdmlReader =
                TextResourceReader.createForOptionalFilePathOrStdin ( inputFile );
              TextResourceWriter pdmlWriter =
                TextResourceWriter.createForOptionalFilePathOrStdout ( outputFile, true ) ) {

            PdmlToHtmlTreeViewUtil.readerToWriter (
                pdmlReader, pdmlWriter,
                PdmlParserConfig.defaultConfig(), true );

            PdmlCommandsHelper.fileCreatedMessageToStdout ( pdmlWriter );

            if ( DesktopUtil.isDesktopSupported() ) {
                Path htmlFilePath = pdmlWriter.getResourceAsFilePath ();
                if ( htmlFilePath != null ) {
                    DesktopUtil.openInDefaultBrowser ( htmlFilePath );
                }
            }
        }

        return null;
    }
}
