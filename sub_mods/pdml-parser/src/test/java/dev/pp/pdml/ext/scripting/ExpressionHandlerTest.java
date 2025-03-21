package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParserUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionHandlerTest {

    @Test
    void testHandleNode() throws Exception {

        TaggedNode rootNode = PdmlParserUtil.parseString ( "[root ^s[exp 1 + 1]]" );
        assertEquals ( "2", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root ^s[exp "2 + 2"]]""" );
        assertEquals ( "4", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( "[root ^s[exp ~|3+3|~]]" );
        assertEquals ( "6", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root ^s[exp ""\"
                4 + 4
                ""\"
                ]]
            """ );
        assertEquals ( "8", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( "[root a ^s[exp 5 + 5] b]" );
        assertEquals ( "a 10 b", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root ^s[exp  ~|"[]"|~ ]
            ]
            """ );
        assertEquals ( "[]\n", rootNode.toText() );
    }

/* Old version with JavaScript
    @Test
    void testHandleNode() throws Exception {

        String code = "[s:exp 1 + 1]end";
        // ExtensionNodeHandlerContext context = contextForTests ( code );
        ExtensionNodeHandlerContext context = ExtensionNodeHandlerContext.createForTests ( code );
        NodeName nodeName = context.getPdmlParser().requireBranchNodeStartAndNameAndSeparator().getName();
        assertNotNull ( nodeName );
        assertEquals ( "s:exp", nodeName.qualifiedName() );
        ExpressionHandler handler = ExpressionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        PdmlReader reader = context.getPdmlReader();
        assertEquals ( '2', reader.currentChar() );
        reader.advanceChar();
        assert ( reader.isAtString ( "end" ) );
    }
 */
}
