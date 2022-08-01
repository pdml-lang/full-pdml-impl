package dev.pdml.ext.extensions.scripting;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.error.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Expression_ExtensionHandlerTest {

    @Test
    public void testHandleNode() throws IOException, TextErrorException {

        String code = "[s:exp 1 + 1]end";
        // String code = "[!exp new Date().toISOString().substr(0,10)]";
        // String code = "[!exp ISOTime.currentLocalDate()]";
        // String code = "[!exp ISOTime.currentLocalDateTimeMinutes()]";
        PDMLExtensionsContext context = PDMLExtensionsContext.createForTests ( code, new ScriptingEnvironmentImpl (  true ) );
        PDMLReader reader = context.getPDMLReader ();
        reader.advanceChar();
        ASTNodeName nodeName = PDMLParserHelper.requireNodeName (
            reader, ExtensionsNamespaces.EXTENSION_NAMESPACE_GETTER, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:exp", nodeName.qualifiedName() );
        Expression_ExtensionHandler handler = Expression_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        // assert ( reader.isAtChar ( '2' ) );
        assertEquals ( '2', reader.currentChar() );
        reader.advanceChar();
        assert ( reader.isAtString ( "end" ) );
        // assertEquals ( "2", reader.readRemaining() );
    }
}