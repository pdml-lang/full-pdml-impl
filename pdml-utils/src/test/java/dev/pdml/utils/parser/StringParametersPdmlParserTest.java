package dev.pdml.utils.parser;

import dev.pp.parameters.parameters.MutableParameters;
import dev.pp.text.inspection.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StringParametersPdmlParserTest {

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

        MutableParameters<String> parameters = new MutableParameters<>();
        StringParametersPdmlParser.parseStringAndAdd ( code, parameters, true );
        assertNotNull ( parameters );
        assertEquals ( 6, parameters.count() );
        assertEquals ( "av1", parameters.value ( "a1" ) );
        assertNull ( parameters.value ( "a2" ) );
        assertEquals ( "av3 av3", parameters.value ( "a3" ) );
        assertEquals ( "v1", parameters.value ( "n1" ) );
        assertNull ( parameters.value ( "n2" ) );
        assertEquals ( "v3 v3", parameters.value ( "n3" ) );

        code = """
            [n1 v1]
            [n2]
            [n3 v3 v3]
            """;

        parameters = new MutableParameters<>();
        StringParametersPdmlParser.parseStringAndAdd ( code, parameters, false );
        assertNotNull ( parameters );
        assertEquals ( 3, parameters.count() );
        assertEquals ( "v1", parameters.value ( "n1" ) );
        assertNull ( parameters.value ( "n2" ) );
        assertEquals ( "v3 v3", parameters.value ( "n3" ) );
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
