package dev.pdml.writer.data;

import dev.pdml.data.node.root.MutableRootNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PdmlDataWriterTest {

    @Test
    void test() throws IOException {

        PdmlDataWriterConfig config = new PdmlDataWriterConfig ( false, true );

        MutableRootNode rootNode = new MutableRootNode ( "root" );
        String result = PdmlDataWriterUtils.writeToString ( rootNode, config );
        assertEquals ( "[root]", result );

        rootNode
            .addAttribute ( "a1", "v 1" )
            .addAttribute ( "a2", null )
            .addAttribute ( "a3", "v3" );
        result = PdmlDataWriterUtils.writeToString ( rootNode, config );
        assertEquals ( """
            [root [@ a1="v 1" a2="" a3=v3]]""", result );

        rootNode = new MutableRootNode ( "root" );
        rootNode.appendText ( "foo bar" )
            .appendText ( " []\\" );
        result = PdmlDataWriterUtils.writeToString ( rootNode, config );
        assertEquals ( "[root foo bar \\[\\]\\\\]", result );
    }

}
