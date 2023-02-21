package dev.pdml.utils.parser;

import dev.pdml.data.node.branch.MutableBranchNode;
import dev.pdml.data.node.leaf.MutableCommentNode;
import dev.pdml.data.node.leaf.MutableTextNode;
import dev.pdml.data.node.root.MutableRootNode;
import dev.pdml.data.utils.PdmlTreeWalker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdmlTreeParserUtilsTest {

    @Test
    void parseStringToTree() throws Exception {

        MutableRootNode rootNode = PdmlTreeParserUtils.parseStringToTree ( """
            [root
                [child_1
                    [child_11]
                    [child_12 text_12]
                ]
                [child_2
                    [child_21
                        [child_211 text 211]text_21[child_213]
                    ]
                ]
            ]
            """ );

        StringBuilder names = new StringBuilder();
        PdmlTreeWalker.forEachNodeInTree ( rootNode, true, node -> {

            /* Use this code when this is no more a preview feature in Java
            switch ( node ) {
                case MutableRootNode rn -> names.append ( rn.getName () ).append ( ", " );
                case MutableBranchNode bn -> names.append ( bn.getName () ).append ( ", " );
                case MutableTextNode tn -> {}
                case MutableCommentNode tn -> {}
                default -> throw new IllegalStateException ( "Unexpected value: " + node );
            }
             */

            if ( node instanceof MutableRootNode root ) {
                names.append ( root.getName() ).append ( ", " );
            } else if ( node instanceof MutableBranchNode branchNode ) {
                names.append ( branchNode.getName() ).append ( ", " );
            } else if ( node instanceof MutableTextNode ) {
                // do nothing
            } else if ( node instanceof MutableCommentNode ) {
                // do nothing
            } else {
                throw new IllegalStateException ( "Unexpected value: " + node );
            }
        });
        assertEquals (
            "root, child_1, child_11, child_12, child_2, child_21, child_211, child_213, ",
            names.toString() );


        rootNode = PdmlTreeParserUtils.parseStringToTree (
            "[p Text contains [b bold], [i italic], and [b [i bold/italic]] words.]" );

        StringBuilder fullText = new StringBuilder();
        PdmlTreeWalker.forEachTextInTree ( rootNode, fullText::append );
        assertEquals ( "Text contains bold, italic, and bold/italic words.", fullText.toString() );
    }
}
