package dev.pp.pdml.ext.utils;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParserUtil;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GetConstantHandlerTest {

    @Test
    void handleNode() throws IOException, PdmlException {

        String code = "[root ^[set c1=foo]^[get c1]]";
        @NotNull TaggedNode rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "foo", rootNode.toText() );

        code = "[root ^[set c1=foo]^[set c2=^[get c1]bar]^[get c2]]";
        rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "foobar", rootNode.toText() );

        code = """
            [root
                ^u[set c1=foo]
                ^u[set c2="^[get c1] bar"]
                [child start ^[get c2] end]
            ]
            """;
        rootNode = PdmlParserUtil.parseString ( code );
        TaggedNode childNode = rootNode.child ( "child" );
        assertEquals ( "start foo bar end", childNode.toText() );
    }
}
