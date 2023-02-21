package dev.pdml.exttypes.handlers;

import dev.pdml.data.node.NodeName;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.PdmlReaderImpl;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.Write_TextInspectionMessageHandler;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.token.TextToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RawTextBlock_ExtensionHandlerTest {

    protected static PdmlExtensionsContext contextForTests ( String code ) throws IOException {

        return contextForTests ( code, new ScriptingEnvironmentImpl (  true ) );
    }

    protected static PdmlExtensionsContext contextForTests ( String code, ScriptingEnvironment scriptingEnvironment ) throws IOException {

        return new PdmlExtensionsContext (
            new PdmlReaderImpl ( new StringReader ( code ), new String_TextResource ( code) ),
            new Write_TextInspectionMessageHandler (),
            scriptingEnvironment );
    }

/*
    @Test
    void testReadBlockWithEndTag() throws IOException, TextErrorException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "    i = 1;",
            "  code]" );
        PdmlExtensionsContext context = contextForTests ( code );
        PdmlReader reader = context.getPdmlReader();
        reader.advanceChar();
        String name = reader.readName();
        assertNotNull ( name );
        NodeName nodeName = new NodeName ( new TextToken ( name ) );
        RawTextBlockType_ExtensionHandler handler = RawTextBlockType_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( "i = 1;", reader.readRemaining() );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "",
            "    i = 1;",
            "",
            "    j=2;",
            "code]" );
        String expected = String.join ( StringConstants.OS_NEW_LINE,
            "i = 1;",
            "",
            "j=2;" );
        context = contextForTests ( code );
        reader = context.getPdmlReader();
        // handler = RawTextBlockType_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( expected, reader.readRemaining() );
    }
 */

    @Test
    void testReadBlockWithDelimiter() throws IOException, TextErrorException {

        String code = """
            [code
                ====
                  i = 1;
                ======]""";
        PdmlExtensionsContext context = contextForTests ( code );
        PdmlReader reader = context.getPdmlReader();
        reader.advanceChar();
        String name = reader.readName();
        NodeName nodeName = new NodeName ( new TextToken ( name ) );
        RawTextBlockType_ExtensionHandler handler = RawTextBlockType_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( "  i = 1;", reader.readRemaining() );

        code = """

                ====
                i = 1;

                  j=2;
                ====

            ]""";
        String expected = """
            i = 1;

              j=2;""";
        context = contextForTests ( code );
        reader = context.getPdmlReader();
        // handler = new RawTextBlockType_ExtensionHandler ();

        handler.handleNode ( context, nodeName );
        // assertEquals ( "i = 1;\r\n\r\n  j=2;", reader.readRemaining() );
        assertEquals ( expected, reader.readRemaining() );
    }
}
