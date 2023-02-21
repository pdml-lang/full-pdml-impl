package dev.pdml.extscripting;

import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.text.inspection.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Definitions_ExtensionHandlerTest {

    @Test
    void testHandleNode() throws IOException, TextErrorException {

        String code = "[s:def function foo() { return '123'; } ][s:exp foo()]";
        PdmlExtensionsContext context = Expression_ExtensionHandlerTest.contextForTests ( code );
        PdmlReader reader = context.getPdmlReader();
        reader.advanceChar();
        NodeName nodeName = PdmlParserHelper.requireNodeName ( reader, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:def", nodeName.qualifiedName() );

        PdmlExtensionNodeHandler handler = Definitions_ExtensionHandler.INSTANCE;
        handler.handleNode ( context, nodeName );

        reader.advanceChar();
        nodeName = PdmlParserHelper.requireNodeName ( reader, context.getErrorHandler() );
        assertNotNull ( nodeName );
        assertEquals ( "s:exp", nodeName.qualifiedName() );

        handler = Expression_ExtensionHandler.INSTANCE;
        handler.handleNode ( context, nodeName );

        assertEquals ( "123", reader.readRemaining() );
    }
}
