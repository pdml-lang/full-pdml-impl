package dev.pp.pdml.parser;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.core.basics.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class PdmlParserTest {

    @Test
    void requireRootNode() throws IOException, PdmlException {

        PdmlParser parser = createParser ( "\r\n\t [root]\n" );
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
    }

    @Test
    void parseTag () throws IOException, PdmlException {

        PdmlParser parser = createParser ( "tag" );
        NodeTag name = parser.parseTag ();
        assertEquals ( "tag", name.tag () );
        assertNull ( name.namespacePrefix() );
        assertEquals ( "tag", name.qualifiedTag () );

        parser = createParser ( "ns|local" );
        name = parser.parseTag ();
        assertEquals ( "ns", name.namespacePrefix() );
        assertEquals ( "local", name.tag () );
        assertEquals ( "ns|local", name.qualifiedTag () );
    }

    private @NotNull PdmlParser createParser ( @NotNull String code ) throws IOException {
        StringReader stringReader = new StringReader ( code );
        return PdmlParser.create ( stringReader, PdmlParserConfig.defaultConfig() );
    }
}
