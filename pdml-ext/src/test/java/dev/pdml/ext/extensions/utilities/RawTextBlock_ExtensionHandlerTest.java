package dev.pdml.ext.extensions.utilities;

import dev.pp.text.token.TextToken;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.error.TextErrorException;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.ext.extensions.types.handlers.RawTextBlockType_ExtensionHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RawTextBlock_ExtensionHandlerTest {

    @Test
    public void testReadBlockWithEndTag() throws IOException, TextErrorException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "    i = 1;",
            "  code]" );
        PDMLExtensionsContext context = PDMLExtensionsContext.createForTests ( code );
        PDMLReader reader = context.getPDMLReader ();
        reader.advanceChar();
        String name = reader.readName();
        assertNotNull ( name );
        ASTNodeName nodeName = new ASTNodeName ( new TextToken ( name ) );
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
        context = PDMLExtensionsContext.createForTests ( code );
        reader = context.getPDMLReader ();
        // handler = RawTextBlockType_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( expected, reader.readRemaining() );
    }

    @Test
    public void testReadBlockWithDelimiter() throws IOException, TextErrorException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "    ====",
            "      i = 1;",
            "    ======]" );
        PDMLExtensionsContext context = PDMLExtensionsContext.createForTests ( code );
        PDMLReader reader = context.getPDMLReader ();
        reader.advanceChar();
        String name = reader.readName();
        ASTNodeName nodeName = new ASTNodeName ( new TextToken ( name ) );
        RawTextBlockType_ExtensionHandler handler = RawTextBlockType_ExtensionHandler.INSTANCE;

        handler.handleNode ( context, nodeName );
        assertEquals ( "  i = 1;", reader.readRemaining() );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "",
            "    ====",
            "    i = 1;",
            "",
            "      j=2;",
            "    ====   ",
            "",
            "]" );
        String expected = String.join ( StringConstants.OS_NEW_LINE,
            "i = 1;",
            "",
            "  j=2;" );
        context = PDMLExtensionsContext.createForTests ( code );
        reader = context.getPDMLReader ();
        // handler = new RawTextBlockType_ExtensionHandler ();

        handler.handleNode ( context, nodeName );
        // assertEquals ( "i = 1;\r\n\r\n  j=2;", reader.readRemaining() );
        assertEquals ( expected, reader.readRemaining() );
    }
}