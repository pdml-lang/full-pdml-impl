package dev.pp.pdml.utils;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.parser.PdmlParserUtil;
import dev.pp.pdml.writer.node.PdmlNodeWriter;
import dev.pp.pdml.writer.node.PdmlNodeWriterConfig;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;

public class PdmlToCorePdmlUtil {

    /* not used
    public static void pdmlFileToToCorePdmlFile (
        @NotNull Path pdmlInputFile,
        @NotNull Path pdmlOutputFile,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlNodeWriterConfig writerConfig ) throws Exception {

        try ( TextResourceReader pdmlReader = new TextResourceReader ( pdmlInputFile );
          TextResourceWriter pdmlWriter = new TextResourceWriter ( pdmlOutputFile, true ) ) {

            pdmlCodeToCorePdml ( pdmlReader, pdmlWriter, parserConfig, writerConfig );
        }
    }
     */

    public static void pdmlCodeToCorePdml (
        @NotNull TextResourceReader pdmlCodeReader,
        @NotNull TextResourceWriter corePdmlCodeWriter,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlNodeWriterConfig writerConfig,
        boolean keepAttributes ) throws Exception {

        @NotNull TaggedNode rootNode = PdmlParserUtil.parseReader (
            pdmlCodeReader, parserConfig );
        treeToCorePdml ( rootNode, corePdmlCodeWriter, writerConfig, keepAttributes );
    }

    private static void treeToCorePdml (
        @NotNull TaggedNode rootNode,
        @NotNull TextResourceWriter corePdmlCodeWriter,
        @NotNull PdmlNodeWriterConfig writerConfig,
        boolean keepAttributes ) throws Exception {

        rootNode.replaceAttributesWithTextNodes();

        if ( ! keepAttributes ) {

            // generates ConcurrentModificationException
            // rootNode.treeBranchNodeStream ( true ).forEach ( BranchNode::replaceAttributesWithTextNodes );

            for ( TaggedNode child : rootNode.treeTaggedNodeStream ( true ).toList() ) {
                child.replaceAttributesWithTextNodes();
            }
        }

        PdmlNodeWriter nodeWriter = new PdmlNodeWriter (
            corePdmlCodeWriter.getWriter(), writerConfig );
        nodeWriter.writeRootNode ( rootNode );
    }
}
