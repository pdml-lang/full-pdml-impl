package dev.pp.pdml.core.parser;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.pdml.data.util.TextNode;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class CorePdmlParserTest {


    @Test
    public void test() throws Exception {

        CorePdmlParser parser = createParser ( "\r\n\t [root]\n" );
        TaggedNode rootNode = parser.requireRootNode();
        assertEquals ( "root", rootNode.getTag ().qualifiedTag () );
        assertTrue ( rootNode.isEmpty () );

        parser = createParser ( "[root [child foo bar]]" );
        rootNode = parser.requireRootNode();
        assertEquals ( "root", rootNode.getTag ().qualifiedTag () );
        assertFalse ( rootNode.isEmpty () );

        assertEquals ( 1, rootNode.getChildNodes().size() );
        TaggedNode childNode = (TaggedNode) rootNode.getChildNodes().get ( 0 );
        assertEquals ( "child", childNode.getTag ().qualifiedTag () );
        assertFalse ( childNode.isEmpty() );

        TextLeaf textLeaf = (TextLeaf) childNode.getChildNodes().get ( 0 );
        assertEquals ( "foo bar", textLeaf.getText() );

        // Invalid
        parser = createParser ( "[root" );
        assertThrows ( PdmlException.class, parser::parseRootNode );
        parser = createParser ( "[root]]" );
        assertThrows ( PdmlException.class, parser::parseRootNode );
        parser = createParser ( "[root^]" );
        assertThrows ( PdmlException.class, parser::parseRootNode );
        parser = createParser ( "[ root]" );
        assertThrows ( PdmlException.class, parser::parseRootNode );
        parser = createParser ( "[root ]" );
        assertThrows ( PdmlException.class, parser::parseRootNode );
        parser = createParser ( "[root[child]]" );
        assertThrows ( PdmlException.class, parser::parseRootNode );
    }

    @Test
    public void testRequireTextNode() throws Exception {

        CorePdmlParser parser = createParser ( "[name1 text1][tag\\[2\\] text2\\\\\\[\\] end\\n]" );

        TextNode textNode = parser.requireTextNode();
        assertEquals ( "name1", textNode.getName().qualifiedTag () );
        assertEquals ( "text1", textNode.getText() );

        textNode = parser.requireTextNode();
        assertEquals ( "tag[2]", textNode.getName().qualifiedTag () );
        assertEquals ( "text2\\[] end\n", textNode.getText() );
    }

    @Test
    public void testRequireTaggedLeafNode () throws Exception {

        CorePdmlParser parser = createParser ( "[name1][tag\\[2\\]]" );

        TaggedNode emptyNode = parser.requireTaggedLeafNode ();
        assertEquals ( "name1", emptyNode.getTag ().qualifiedTag () );
        assertTrue ( emptyNode.isEmpty() );

        emptyNode = parser.requireTaggedLeafNode ();
        assertEquals ( "tag[2]", emptyNode.getTag ().qualifiedTag () );
        assertTrue ( emptyNode.isEmpty() );
    }

    private @NotNull CorePdmlParser createParser ( @NotNull String code ) throws IOException {
        StringReader stringReader = new StringReader ( code );
        return new CorePdmlParser ( stringReader );
    }
}
