package dev.pdml.utils.parser;

import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pdml.data.node.root.MutableRootNode;
import dev.pdml.parser.eventhandler.PdmlParserEventHandler;
import dev.pdml.parser.nodespec.PdmlNodeSpecs;
import dev.pdml.utils.SharedDefaultOptions;
import dev.pdml.utils.parser.eventhandlers.CreateTree_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class PdmlTreeParserUtils {

    public static @NotNull MutableRootNode parseFileToTree ( @NotNull Path file ) throws Exception {

        return parseFileToTree ( file, SharedDefaultOptions.createErrorHandler(), null );
    }

    public static @NotNull MutableRootNode parseFileToTree (
        @NotNull Path file,
        @NotNull TextInspectionMessageHandler errorHandler,
        @Nullable PdmlNodeSpecs<?> nodeSpecs ) throws Exception {

        try ( Reader reader = TextFileIO.getUTF8FileReader ( file ) ) {
            return parseToTree (
                reader, new File_TextResource ( file ), null, null, errorHandler, nodeSpecs );
        }
    }

    public static @NotNull MutableRootNode parseStringToTree ( @NotNull String pdmlCode ) throws Exception {

        return parseStringToTree ( pdmlCode, null, SharedDefaultOptions.createErrorHandler(), null );
    }

    public static @NotNull MutableRootNode parseStringToTree (
        @NotNull String pdmlCode,
        @Nullable TextResource resource,
        @NotNull TextInspectionMessageHandler errorHandler,
        @Nullable PdmlNodeSpecs<?> nodeSpecs ) throws Exception {

        try ( Reader reader = new StringReader ( pdmlCode ) ) {
            return parseToTree ( reader, resource, null, null, errorHandler, nodeSpecs );
        }
    }

    public static @NotNull MutableRootNode parseToTree (
        @NotNull Reader pdmlCodeReader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull TextInspectionMessageHandler errorHandler,
        @Nullable PdmlNodeSpecs<?> nodeSpecs ) throws Exception {

        PdmlParserEventHandler<MutableRootOrBranchNode, MutableRootNode> eventHandler =
            new CreateTree_ParserEventHandler ();

        new PdmlParserBuilder<> ( eventHandler )
            .errorHandler ( errorHandler )
            .nodeSpecs ( nodeSpecs )
            .allowAlternativeAttributesStartSyntax ( true )
            .parseReader ( pdmlCodeReader, resource, lineOffset, columnOffset );

        MutableRootNode result = eventHandler.getResult();
        assert result != null;
        return result;
    }
}
