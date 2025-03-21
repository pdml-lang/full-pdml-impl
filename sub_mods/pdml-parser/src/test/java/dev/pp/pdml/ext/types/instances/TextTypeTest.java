package dev.pp.pdml.ext.types.instances;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.nodespec.PdmlNodeSpec;
import dev.pp.pdml.data.nodespec.PdmlNodeSpecs;
import dev.pp.pdml.ext.types.PdmlType;
import dev.pp.pdml.ext.types.PdmlTypes;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.parser.PdmlParserConfigBuilder;
import dev.pp.pdml.parser.PdmlParserUtil;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TextTypeTest {

    protected static @NotNull TaggedNode parse (
        @NotNull String code,
        @NotNull PdmlType<?> type ) throws IOException, PdmlException {

        PdmlNodeSpec nodeSpec = new PdmlNodeSpec (
            new NodeTag ( "root" ),
            type.getName(), null, null );
        PdmlParserConfig config = new PdmlParserConfigBuilder()
            .types ( new PdmlTypes().add ( type ) )
            .nodeSpecs ( new PdmlNodeSpecs().add ( nodeSpec ) )
            .build();
        return PdmlParserUtil.parseString ( code, config );
    }

    @Test
    void test() throws Exception {

        PdmlType<?> textType = TextType.NON_NULL_INSTANCE;
        PdmlType<?> textOrNullType = TextType.NULLABLE_INSTANCE;

        TaggedNode rootNode = parse ( "[root text]", textType );
        assertEquals ( 1, rootNode.getChildNodes().size() );
        assertEquals ( "text", rootNode.toText() );
        String text = rootNode.getCastedJavaObjectContained();
        assertNotNull ( text );
        assertEquals ( "text", text );

        rootNode = parse ( "[untyped_root ^t[text text2]]", textType );
        assertEquals ( "text2", rootNode.toText() );

        rootNode = parse ( "[root ^t[text text3]]", textType );
        assertEquals ( "text3", rootNode.toText() );

        rootNode = parse ( "[root text \\[\\] text \n]", textType );
        assertEquals ( "text [] text \n", rootNode.toText() );

        rootNode = parse ( "[root  ]", textType );
        assertEquals ( " ", rootNode.toText() );

        assertThrows ( PdmlException.class,
            () -> parse ( "[root]", textType ) );

        rootNode = parse ( "[root]", textOrNullType );
        assertNull ( rootNode.toTextOrNull() );

        assertThrows ( PdmlException.class,
            () -> parse ( "[root text [child] ]", textType ) );
    }
}
