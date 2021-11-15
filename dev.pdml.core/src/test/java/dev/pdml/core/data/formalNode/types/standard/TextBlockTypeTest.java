package dev.pdml.core.data.formalNode.types.standard;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pdml.core.reader.reader.DefaultPXMLReader;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.string.StringConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextBlockTypeTest {

    @Test
    public void testReadBlockWithEndTag() throws TextReaderException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "   i = 1;",
            "code]" );
        String expectedResult = "i = 1;";
        String result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "         i = 1;",
            "      j = 2;",
            "   code]" );
        expectedResult = String.join ( StringConstants.OS_NEW_LINE,
            "   i = 1;",
            "j = 2;" );
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "         i = 1;",
            "",
            "      j = 2;",
            "   code]" );
        expectedResult = String.join ( StringConstants.OS_NEW_LINE,
            "   i = 1;",
            "",
            "j = 2;" );
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "i = 1;",
            "   code]" );
        expectedResult = "i = 1;";
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "   if cond then",
            "      foo",
            "",
            "   .",
            "   code]" );
        expectedResult = String.join ( StringConstants.OS_NEW_LINE,
            "if cond then",
            "   foo",
            "",
            ".");
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "   code]" );
        assertNull ( readCodeNode ( code ) );

        final String code_2 = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "   i = 1;",
            "   cod]" );
        assertThrows ( MalformedPXMLDocumentException.class, () -> readCodeNode ( code_2 ) );

        final String code_3 = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "      i = 1;",
            "   ]" );
        assertThrows ( MalformedPXMLDocumentException.class, () -> readCodeNode ( code_3 ) );
    }

    @Test
    public void testReadBlockWithDelimiter() throws TextReaderException {

        String code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "   ~~~",
            "   i = 1;",
            "   ~~~",
            "]" );
        String expectedResult = "i = 1;";
        String result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "   ~~~",
            "      i = 1;",
            "         j = 2;",
            "   ~~~~~~",
            "]" );
        expectedResult = String.join ( StringConstants.OS_NEW_LINE,
            "   i = 1;",
            "      j = 2;" );
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "====",
            "   i = 1;",
            "",
            "j = 2;",
            "========]" );
        expectedResult = String.join ( StringConstants.OS_NEW_LINE,
            "   i = 1;",
            "",
            "j = 2;" );
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "    ====",
            "    i = 1;",
            "",
            "      j=2;",
            "    ====   ",
            "",
            "]" );
        expectedResult = String.join ( StringConstants.OS_NEW_LINE,
            "i = 1;",
            "",
            "  j=2;" );
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "====",
            "========]" );
        assertNull ( readCodeNode ( code ) );

        String code_2 = String.join ( StringConstants.OS_NEW_LINE,
            "[code",
            "   ~~~",
            "   i = 1;",
            "   ~~",
            "]" );
        assertThrows ( MalformedPXMLDocumentException.class, () -> readCodeNode ( code_2 ) );
    }

    private static String readCodeNode ( String code ) throws TextReaderException {

        PXMLReader pxmlReader = new DefaultPXMLReader ( code );
        pxmlReader.skipOptionalSpacesAndTabs();
        pxmlReader.skipChar ( '[' );
        TextToken nameToken = pxmlReader.readNameToken();
        assert nameToken != null;
        ASTNodeName nodeName = new ASTNodeName ( nameToken, null );

        TextBlockType textBlockType = new TextBlockType();
        return textBlockType.readPDMLObject ( pxmlReader, nodeName );
    }
}