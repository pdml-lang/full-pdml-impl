package dev.pdml.extscripting;

import dev.pdml.data.node.NodeName;
import dev.pdml.extscripting.bindings.DocBinding;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.inspection.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Script_ExtensionHandlerTest {

    @Test
    void testHandleNode() throws IOException, TextErrorException {

        String code = """
            [s:script doc.insert ( 'foo' );
                stdout.writeLine ( 'Hello' );
                stdout.writeLine ( timeUtils.currentLocalDateTimeSeconds() );
            ]
            """;
        ScriptingEnvironment se = new ScriptingEnvironmentImpl ( true );
        PdmlExtensionsContext context = Expression_ExtensionHandlerTest.contextForTests ( code, se );
        PdmlReader reader = context.getPdmlReader();
        DocBinding db = new DocBinding ( reader );
        se.addBinding ( db.bindingName(), db );

        reader.advanceChar();
        NodeName nodeName = PdmlParserHelper.requireNodeName ( reader, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:script", nodeName.qualifiedName() );
        Script_ExtensionHandler handler = Script_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( "foo\n", reader.readRemaining() );
    }
}
