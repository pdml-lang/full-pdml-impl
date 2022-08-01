package dev.pdml.ext.extensions.scripting;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.scripting.bindings.DocBinding;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.error.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Script_ExtensionHandlerTest {

    @Test
    public void testHandleNode() throws IOException, TextErrorException {

        String code = """
            [s:script doc.insert ( 'foo' );
                stdout.writeLine ( 'Hello' );
                stdout.writeLine ( timeUtils.currentLocalDateTimeSeconds() );
            ]
            """;
        ScriptingEnvironment se = new ScriptingEnvironmentImpl ( true );
        PDMLExtensionsContext context = PDMLExtensionsContext.createForTests ( code, se );
        PDMLReader reader = context.getPDMLReader ();
        DocBinding db = new DocBinding ( reader );
        se.addBinding ( db.bindingName(), db );

        reader.advanceChar();
        ASTNodeName nodeName = PDMLParserHelper.requireNodeName (
            reader, ExtensionsNamespaces.EXTENSION_NAMESPACE_GETTER, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:script", nodeName.qualifiedName() );
        Script_ExtensionHandler handler = Script_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( "foo\n", reader.readRemaining() );
    }
}