package dev.pdml.ext.extensions.node.standard;

import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.string.StringConstants;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pdml.ext.extensions.node.types.RawTextBlockType_ExtensionHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RawTextBlock_ExtensionHandlerTest {

    @Test
    public void testReadBlockWithEndTag() throws TextReaderException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "    i = 1;",
            "  code]" );
        ExtensionsContext context = ExtensionsContext.createForTests ( code );
        PXMLReader reader = context.getPXMLReader();
        reader.advanceChar();
        String name = reader.readName();
        ASTNodeName nodeName = new ASTNodeName ( new TextToken ( name ), null );
        RawTextBlockType_ExtensionHandler handler = new RawTextBlockType_ExtensionHandler ();

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
        context = ExtensionsContext.createForTests ( code );
        reader = context.getPXMLReader();
        handler = new RawTextBlockType_ExtensionHandler ();

        handler.handleNode ( context, nodeName );
        assertEquals ( expected, reader.readRemaining() );
    }

    @Test
    public void testReadBlockWithDelimiter() throws TextReaderException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "    ====",
            "      i = 1;",
            "    ======]" );
        ExtensionsContext context = ExtensionsContext.createForTests ( code );
        PXMLReader reader = context.getPXMLReader();
        reader.advanceChar();
        String name = reader.readName();
        ASTNodeName nodeName = new ASTNodeName ( new TextToken ( name ), null );
        RawTextBlockType_ExtensionHandler handler = new RawTextBlockType_ExtensionHandler ();

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
        context = ExtensionsContext.createForTests ( code );
        reader = context.getPXMLReader();
        handler = new RawTextBlockType_ExtensionHandler ();

        handler.handleNode ( context, nodeName );
        // assertEquals ( "i = 1;\r\n\r\n  j=2;", reader.readRemaining() );
        assertEquals ( expected, reader.readRemaining() );
    }
}