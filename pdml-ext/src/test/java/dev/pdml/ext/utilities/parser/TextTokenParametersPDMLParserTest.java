package dev.pdml.ext.utilities.parser;

import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.error.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TextTokenParametersPDMLParserTest {

    @Test
    void parseStringAndAdd() throws IOException, TextErrorException {

        String code = """
            [config (
                a1 = av1
                a2 = ""
                a3 = "av3 av3"  )
                [n1 v1]
                [n2]
                [n3 v3 v3]
            ]
            """;

        TextTokenParameters parameters = new TextTokenParameters ( null );
        TextTokenParametersPDMLParser.parseStringAndAdd ( code, parameters, true );
        assertNotNull ( parameters );
        assertEquals ( 6, parameters.getCount() );
        assertEquals ( "av1", parameters.getValue ( "a1" ) );
        assertNull ( parameters.getValue ( "a2" ) );
        assertEquals ( "av3 av3", parameters.getValue ( "a3" ) );
        assertEquals ( "v1", parameters.getValue ( "n1" ) );
        assertNull ( parameters.getValue ( "n2" ) );
        assertEquals ( "v3 v3", parameters.getValue ( "n3" ) );

        code = """
            [n1 v1]
            [n2]
            [n3 v3 v3]
            """;

        parameters = new TextTokenParameters ( null );
        TextTokenParametersPDMLParser.parseStringAndAdd ( code, parameters, false );
        assertNotNull ( parameters );
        assertEquals ( 3, parameters.getCount() );
        assertEquals ( "v1", parameters.getValue ( "n1" ) );
        assertNull ( parameters.getValue ( "n2" ) );
        assertEquals ( "v3 v3", parameters.getValue ( "n3" ) );
    }
/*
    @Test
    void parseWithoutRootAndAdd() throws IOException, InvalidTextException {

        String code = """
            [n1 v1]
            [n2]
            [n3 v3 v3]
            """;

        TextTokenParameters parameters = new TextTokenParameters();
        TextTokenParametersPDMLParser.parseStringAndAdd ( code, parameters, false );
        assertNotNull ( parameters );
        assertEquals ( 3, parameters.getCount() );
        assertEquals ( "v1", parameters.getValue ( "n1" ) );
        assertNull ( parameters.getValue ( "n2" ) );
        assertEquals ( "v3 v3", parameters.getValue ( "n3" ) );
    }

 */
}