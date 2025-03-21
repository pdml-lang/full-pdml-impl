package dev.pp.pdml.companion.commands;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.parser.PdmlParserUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.commands.command.CommandSpec;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.texttable.writer.pretty.utilities.TextInspectionMessage_FormWriter;

import java.nio.file.Path;
import java.util.List;

import static dev.pp.pdml.companion.commands.SharedParameterSpecs.*;

public class CheckPdmlDocsCommand {

    // @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull ParameterSpecs<List<Path>> PARAMETERS = new MutableParameterSpecs<List<Path>>()
        .add ( OPTIONAL_PDML_INPUT_FILES )
        .makeImmutable();

    // @SuppressWarnings ( {"unchecked", "rawtypes"} )
    public static final @NotNull CommandSpec<List<Path>, Boolean> COMMAND_SPEC = CommandSpec.<List<Path>, Boolean>builder (
        "check_PDML_docs", CheckPdmlDocsCommand::execute )
        .alternativeName ( "ch" )
        .inputParameters ( PARAMETERS )
        .documentation ( "Check PDML Documents For Errors",
            "Parse one or more PDML documents and report errors encountered.",
            "ch -i input/document.pdml" )
        .build();

    public static @NotNull Boolean execute ( @Nullable Parameters<List<Path>> parameters ) throws Exception {

        assert parameters != null;

        @Nullable List<Path> inputFiles = parameters.value ( OPTIONAL_PDML_INPUT_FILES );

        PdmlParserConfig config = PdmlParserConfig.defaultConfig();
        // boolean success = true;
        Exception parserException = null;

        if ( inputFiles != null ) {
            for ( Path pdmlFile : inputFiles ) {
                try ( TextResourceReader reader = new TextResourceReader ( pdmlFile ) ) {
                    PdmlParserUtil.parseReader ( reader, config );
                } catch ( Exception e ) {
                    reportError ( e );
                    if ( parserException == null ) {
                        parserException = e;
                    }
                }
            }
        } else {
            try {
                PdmlParserUtil.parseReader ( TextResourceReader.STDIN_READER, config );
            } catch ( Exception e ) {
                reportError ( e );
                parserException = e;
            }
        }

        if ( parserException == null ) {
            System.out.println ( "No errors detected." );
            return true;
        } else {
            // System.err.println ( "Errors detected." );
            throw new Exception ( "Errors detected." );
        }
    }

    private static void reportError ( @NotNull Exception e ) {

        if ( e instanceof PdmlException pdmlException ) {
            InvalidTextException textException = pdmlException.toInvalidTextException();
            TextInspectionMessageHandler handler = TextInspectionMessage_FormWriter.createLogMessageHandler();
            handler.handleMessage ( textException.textInspectionError() );

        } else {
            System.err.println ( e.getMessage() );
        }
    }
}
