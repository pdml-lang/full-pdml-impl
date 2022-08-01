package dev.pdml.ext.utilities.parser;

import dev.pp.text.error.TextErrorException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PDMLStringMapParserTest {

    @Test
    public void testParseString() throws IOException, TextErrorException {

        String code = """
            [config
                [a1 v1]
                [a2 v2 v2
                    line 2
                    end]
                
                [- comment a3 -]
                [a3]
                
                [a4  ]
                
                [- comment a5 -]
                [a5 ]
            ]
            """;
        Map<String, String> result = StringMapPDMLParser.parse ( code );
        assertNotNull ( result );
        assertEquals ( 5, result.size() );
        assertEquals ( "v1", result.get ( "a1" ) );
        String v2 = result.get ( "a2" );
        assertTrue ( v2.startsWith ( "v2 v2" ) );
        assertTrue ( v2.endsWith ( "end" ) );
        assertTrue ( v2.contains ( "\n" ) );
        assertTrue ( result.containsKey ( "a3" ) );
        assertNull ( result.get ( "a3" ) );
        assertEquals ( " ", result.get ( "a4" ) );
        assertNull ( result.get ( "a5" ) );

        assertNull ( StringMapPDMLParser.parse ( "[config]" ) );

        assertNull ( StringMapPDMLParser.parse ( "[config   [- comment-] ]" ) );

        result = StringMapPDMLParser.parse ( "[config [a1]]" );
        assertNotNull ( result );
        assertTrue ( result.containsKey ( "a1" ) );
        assertNull ( result.get ( "a1" ) );

        // Invalid code

        // key defined twice
        final String code_2 = """
            [config
                [a1 v1]
                [a1 v2]
            ]
            """;
        assertThrows ( Exception.class, () -> StringMapPDMLParser.parse ( code_2 ) );

        // child nodes not allowed
        final String code_3 = """
            [config
                [size [width=10]]
            """;
        assertThrows ( Exception.class, () -> StringMapPDMLParser.parse ( code_3 ) );

        // attributes not allowed
        final String code_4 = """
            [config
                [length (unit=cm) 100]
            """;
        assertThrows ( Exception.class, () -> StringMapPDMLParser.parse ( code_4 ) );

        // text in root node not allowed
        final String code_5 = """
            [config
                text
                [a1 v1]
            """;
        assertThrows ( Exception.class, () -> StringMapPDMLParser.parse ( code_5 ) );
    }
}
