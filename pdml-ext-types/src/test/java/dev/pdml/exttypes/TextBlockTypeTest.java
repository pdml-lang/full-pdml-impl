package dev.pdml.exttypes;

import dev.pdml.data.node.NodeName;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.PdmlReaderImpl;
import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class TextBlockTypeTest {


    @Test
    void testStandardTextNode() throws IOException, TextErrorException {

        String code = "[code i = 1;]";
        String expectedResult = "i = 1;";
        String result = readCodeNode ( code );
        assertEquals ( expectedResult, result );
    }

/*
    @Test
    void testReadBlockWithEndTag() throws IOException, TextErrorException {

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
        assertThrows ( PdmlDocumentSyntaxException.class, () -> readCodeNode ( code_2 ) );

        final String code_3 = String.join ( StringConstants.OS_NEW_LINE,
            "   [code",
            "      i = 1;",
            "   ]" );
        assertThrows ( PdmlDocumentSyntaxException.class, () -> readCodeNode ( code_3 ) );
    }
 */

    @Test
    void testReadBlockWithDelimiter() throws IOException, TextErrorException {

        String code = """
            [code
               ~~~
               i = 1;
               ~~~"
            ]""";
        String expectedResult = "i = 1;";
        String result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = """
            [code
               ~~~
                  i = 1;
                     j = 2;
               ~~~~~~
            ]""";
        // expectedResult = String.join ( StringConstants.OS_NEW_LINE,
        expectedResult = String.join ( StringConstants.UNIX_NEW_LINE,
            "   i = 1;",
            "      j = 2;" );
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = """
            [code
            ====
               i = 1;

            j = 2;
            ========]""";
        expectedResult = """
               i = 1;

            j = 2;""";
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = """
            [code
                ====
                i = 1;

                  j=2;
                ====

            ]""";
        expectedResult = """
            i = 1;

              j=2;""";
        result = readCodeNode ( code );
        assertEquals ( expectedResult, result );

        code = """
            [code
            ====
            ========]""";
        assertNull ( readCodeNode ( code ) );

        String code_2 = """
            [code
               ~~~
               i = 1;
               ~~
            ]""";
        assertThrows ( PdmlDocumentSyntaxException.class, () -> readCodeNode ( code_2 ) );

        String code_3 = """
            [code
               ~~
               i = 1;
               ~~~
            ]""";
        assertThrows ( PdmlDocumentSyntaxException.class, () -> readCodeNode ( code_3 ) );

        String code_4 = """
            [code
               +++
               i = 1;
               +++
            ]""";
        assertThrows ( PdmlDocumentSyntaxException.class, () -> readCodeNode ( code_4 ) );
    }

    private static String readCodeNode ( String code ) throws IOException, TextErrorException {

        PdmlReader pdmlReader = new PdmlReaderImpl ( new StringReader ( code ) );
        pdmlReader.skipOptionalSpacesAndTabs();
        pdmlReader.skipChar ( '[' );
        TextToken nameToken = pdmlReader.readNameToken();
        assert nameToken != null;
        NodeName nodeName = new NodeName ( nameToken );

        PdmlTextBlockType textBlockType = new PdmlTextBlockType ();
        return textBlockType.readPDMLObject ( pdmlReader, nodeName );
    }
}
