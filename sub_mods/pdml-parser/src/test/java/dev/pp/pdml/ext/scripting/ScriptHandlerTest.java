package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParserUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptHandlerTest {

    @Test
    void testHandleNode() throws Exception {

        String code = """
            [root ^s[script ctx.doc().insertText ( "foo" );]]""";
        TaggedNode rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "foo", rootNode.toText() );

        code = """
            [root ^s[script ""\"
                ctx.doc().insertText ( "[]" );
                ""\"
            ]]
            """;
        rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "[]", rootNode.toText() );

        code = """
            [root ^s[script
                ""\"
                ctx.doc().insertText ( "foo" );
                System.out.println ( "Hello from Java script" );
                ""\"
            ]
            ]
            """;
        rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "foo\n", rootNode.toText() );
    }

/* Old version using JavaScript

    @Test
    void testHandleNode() throws Exception {

        // JavaScript
        String code = """
            [s:script doc.insert ( 'foo' );
                stdout.writeLine ( 'Hello' );
                stdout.writeLine ( timeUtils.currentLocalDateTimeSeconds() );
            ]
            """;

        ScriptingEnvironment se = new JavaScriptScriptingEnvironment ( true );
        // ExtensionNodeHandlerContext context = Expression_ExtensionHandlerTest.contextForTests ( code, se );
        ExtensionNodeHandlerContext context = ExtensionNodeHandlerContext.createForTests ( code, se );
        PdmlReader reader = context.getPdmlReader();
        DocScriptingContext db = new DocScriptingContext ( reader );
        se.addBinding ( db.bindingName(), db );

        NodeName nodeName = context.getPdmlParser().requireBranchNodeStartAndNameAndSeparator().getName();
        assertNotNull ( nodeName );
        assertEquals ( "s:script", nodeName.qualifiedName() );
        ScriptHandler handler = ScriptHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( "foo\n", reader.readRemaining() );
    }
 */
}
