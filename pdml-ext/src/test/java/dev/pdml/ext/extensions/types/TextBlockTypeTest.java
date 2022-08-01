package dev.pdml.ext.extensions.types;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.exception.PDMLDocumentSyntaxException;
import dev.pdml.core.reader.PDMLReaderImpl;
import dev.pdml.core.reader.PDMLReader;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;
import dev.pp.basics.utilities.string.StringConstants;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TextBlockTypeTest {


    @Test
    public void testStandardTextNode() throws IOException, TextErrorException {

        String code = "[code i = 1;]";
        String expectedResult = "i = 1;";
        String result = readCodeNode ( code );
        assertEquals ( expectedResult, result );
    }

    @Test
    public void testReadBlockWithEndTag() throws IOException, TextErrorException {

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
        assertThrows ( PDMLDocumentSyntaxException.class, () -> readCodeNode ( code_2 ) );

        final String code_3 = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "      i = 1;",
            "   ]" );
        assertThrows ( PDMLDocumentSyntaxException.class, () -> readCodeNode ( code_3 ) );
    }

    @Test
    public void testReadBlockWithDelimiter() throws IOException, TextErrorException {

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
        assertThrows ( PDMLDocumentSyntaxException.class, () -> readCodeNode ( code_2 ) );
    }

    private static String readCodeNode ( String code ) throws IOException, TextErrorException {

        PDMLReader pxmlReader = new PDMLReaderImpl ( code );
        pxmlReader.skipOptionalSpacesAndTabs();
        pxmlReader.skipChar ( '[' );
        TextToken nameToken = pxmlReader.readNameToken();
        assert nameToken != null;
        ASTNodeName nodeName = new ASTNodeName ( nameToken );

        PDMLTextBlockType textBlockType = new PDMLTextBlockType ();
        return textBlockType.readPDMLObject ( pxmlReader, nodeName );
    }
}