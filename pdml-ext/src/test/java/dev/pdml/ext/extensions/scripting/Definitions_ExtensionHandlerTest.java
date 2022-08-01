package dev.pdml.ext.extensions.scripting;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.error.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Definitions_ExtensionHandlerTest {

    @Test
    public void testHandleNode() throws IOException, TextErrorException {

        String code = "[s:def function foo() { return '123'; } ][s:exp foo()]";
        PDMLExtensionsContext context = PDMLExtensionsContext.createForTests ( code, new ScriptingEnvironmentImpl ( true ) );
        PDMLReader reader = context.getPDMLReader ();
        reader.advanceChar();
        ASTNodeName nodeName = PDMLParserHelper.requireNodeName (
            reader, ExtensionsNamespaces.EXTENSION_NAMESPACE_GETTER, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:def", nodeName.qualifiedName() );

        PDMLExtensionNodeHandler handler = Definitions_ExtensionHandler.INSTANCE;
        handler.handleNode ( context, nodeName );

        reader.advanceChar();
        nodeName = PDMLParserHelper.requireNodeName (
            reader, ExtensionsNamespaces.EXTENSION_NAMESPACE_GETTER, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:exp", nodeName.qualifiedName() );

        handler = Expression_ExtensionHandler.INSTANCE;
        handler.handleNode ( context, nodeName );

        assertEquals ( "123", reader.readRemaining() );
    }
}