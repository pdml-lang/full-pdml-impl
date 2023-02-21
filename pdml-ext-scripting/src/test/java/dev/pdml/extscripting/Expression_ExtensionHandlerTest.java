package dev.pdml.extscripting;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.PdmlReaderImpl;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.Write_TextInspectionMessageHandler;
import dev.pp.text.resource.String_TextResource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Expression_ExtensionHandlerTest {

    protected static PdmlExtensionsContext contextForTests ( String code ) throws IOException {

        return contextForTests ( code, new ScriptingEnvironmentImpl (  true ) );
    }

    protected static PdmlExtensionsContext contextForTests ( String code, ScriptingEnvironment scriptingEnvironment ) throws IOException {

        return new PdmlExtensionsContext (
            new PdmlReaderImpl ( new StringReader ( code ), new String_TextResource ( code) ),
            new Write_TextInspectionMessageHandler (),
            scriptingEnvironment );
    }

    @Test
    void testHandleNode() throws IOException, TextErrorException {

        String code = "[s:exp 1 + 1]end";
        // String code = "[!exp new Date().toISOString().substr(0,10)]";
        // String code = "[!exp ISOTime.currentLocalDate()]";
        // String code = "[!exp ISOTime.currentLocalDateTimeMinutes()]";
        PdmlExtensionsContext context = contextForTests ( code );
        PdmlReader reader = context.getPdmlReader();
        reader.advanceChar();
        NodeName nodeName = PdmlParserHelper.requireNodeName ( reader, context.getErrorHandler() );
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
