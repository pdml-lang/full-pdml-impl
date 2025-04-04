package dev.pp.pdml.parser;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.Node;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.leaf.CommentLeaf;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.pdml.data.util.TestDoc;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PdmlParserUtilTest {

    @Test
    void parseTestDoc() {

        String pdmlTestDoc = TestDoc.getPdmlTestDoc();
        assertDoesNotThrow ( () -> PdmlParserUtil.parseString ( pdmlTestDoc ) );
    }

    @Test
    void parseString() throws Exception {

        TaggedNode rootNode = PdmlParserUtil.parseString ( "[root]" );
        assertEquals ( "root", rootNode.getTag ().qualifiedTag () );

        assertThrows ( PdmlException.class, () ->
            PdmlParserUtil.parseString ( "[root ]" ) );

        rootNode = PdmlParserUtil.parseString ( "[root  ]" );
        assertEquals ( " ", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( "[root  a ]" );
        assertEquals ( " a ", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root
                [child_1
                    [child_11]
                    [child_12 text_12]
                ]
                [child_2
                    [child_21
                        [child_211 text 211]text_21[child_213]
                    ]
                ]
            ]
            """ );

        StringBuilder names = new StringBuilder();
        for ( Node node : rootNode ) {
            if ( node instanceof TaggedNode taggedNode ) {
                names.append ( taggedNode.getTag () ).append ( ", " );
            }
        }
        assertEquals (
            "root, child_1, child_11, child_12, child_2, child_21, child_211, child_213, ",
            names.toString() );

        // Markup
        rootNode = PdmlParserUtil.parseString (
            "[p Text contains [b bold], [i italic], and [b [i bold/italic]] words.]" );
        assertEquals ( "Text contains bold, italic, and bold/italic words.", rootNode.concatenateTreeTexts() );
    }

    @Test
    void testSinglelineCommet() throws Exception {

        TaggedNode rootNode = PdmlParserUtil.parseString ( """
            [root text ^// comment
            ]""" );
        assertEquals ( 1, rootNode.getChildNodes().size() );
        assertEquals ( "text ", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root text ^/ comment
            ]""" );
        assertEquals ( 1, rootNode.getChildNodes().size() );
        assertEquals ( "text \n", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root
                before
                ^// comment
                after
            ]""" );
        assertEquals ( 1, rootNode.getChildNodes().size() );
        assertEquals ( "    before\n        after\n", rootNode.toText() );
    }


    @Test
    void testMultilineComment() throws Exception {

        TaggedNode rootNode = PdmlParserUtil.parseString ( """
            [root text before ^/* comment */ text after]""" );

        assertEquals ( 1, rootNode.getChildNodes().size() );
        TextLeaf textLeaf = (TextLeaf) rootNode.childAt ( 0 );
        assertEquals ( "text before  text after", textLeaf.getText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root text before ^/* included comment */ text after]""",
            new PdmlParserConfigBuilder ().ignoreComments ( false ).build() );
        assertEquals ( 3, rootNode.getChildNodes().size() );
        textLeaf = (TextLeaf) rootNode.childAt ( 0 );
        assertEquals ( "text before ", textLeaf.getText() );
        CommentLeaf commentLeaf = (CommentLeaf) rootNode.childAt ( 1 );
        assertEquals ( "^/* included comment */", commentLeaf.getText() );
        assertEquals ( " included comment ", commentLeaf.textWithoutDelimiters() );
        textLeaf = (TextLeaf) rootNode.childAt ( 2 );
        assertEquals ( " text after", textLeaf.getText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root text before^/* comment
                    ^/* nested comment */
                */text after]""" );
        // This test is valid if comments are not ignored
        // assertEquals ( 3, rootNode.getChildNodes().size() );
        // commentLeaf = (CommentLeaf) rootNode.childAt ( 1 );
        // assertEquals ( """
        //     ^/* comment
        //             ^/* nested comment */
        //         */""", commentLeaf.getText() );
        assertEquals ( 1, rootNode.getChildNodes().size() );
        textLeaf = (TextLeaf) rootNode.childAt ( 0 );
        assertEquals ( "text beforetext after", textLeaf.getText() );

        rootNode = PdmlParserUtil.parseString (
            "[root ^/** comment including */ **/]" );
        // This test is valid if comments are not ignored
        // assertEquals ( 1, rootNode.getChildNodes().size() );
        // commentLeaf = (CommentLeaf) rootNode.childAt ( 0 );
        // assertEquals ( "^/** comment including */ **/", commentLeaf.getText() );
        // assertEquals ( " comment including */ ", commentLeaf.textWithoutDelimiters() );
        assertEquals ( 0, rootNode.getChildNodes().size() );
        assertTrue ( rootNode.isEmpty() );

        rootNode = PdmlParserUtil.parseString ( """
            [root a^/* comment */^/* */b]""" );
        assertEquals ( 1, rootNode.getChildNodes().size() );
        textLeaf = (TextLeaf) rootNode.childAt ( 0 );
        assertEquals ( "ab", textLeaf.getText() );

        assertThrows ( PdmlException.class, () ->
            PdmlParserUtil.parseString ( """
                [root ^(a1 = "v ^/** comment **/ 1")]
                """ ) );
    }

    @Test
    void testAttributes() throws Exception {

        var code = """
            [root ^(a1=v1 a2=v2)  foo bar]""";
        TaggedNode rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "v1", rootNode.getStringAttributes().value ( "a1" ) );
        assertEquals ( "v2", rootNode.getStringAttributes().value ( "a2" ) );
        assertEquals ( " foo bar", rootNode.toText() );
        // writtenCode = PdmlNodeWriterUtil.writeToString ( rootnode, false );
        // assertEquals ( code, writtenCode );

        code = """
            [root ^(a1="v1 v1" a2="")foo bar]""";
        rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "v1 v1", rootNode.getStringAttributes().value ( "a1" ) );
        assertNull ( rootNode.getStringAttributes().value ( "a2" ) );
        assertEquals ( "foo bar", rootNode.toText() );
    }

    @Test
    void testSetGetExtensions() throws Exception {

        String code = """
            [root
                ^[set c1=v1]
                ^[set ^(c3=v3)]

                ^[set a=a]
                ^[set b=b]
                ^[set ab=^[get a]^[get b]]
                ^[set abc=^[get ab]c]

                [n1 ^[get abc]]
                [n_^[get abc] text ^[get abc] text]
            ]""";
        TaggedNode rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "abc", rootNode.child ( "n1" ).toText() );
        assertEquals ( "text abc text", rootNode.child ( "n_abc" ).toText() );
    }

    @Test
    void testScripting() throws Exception {

        String code = "[tag\\[^s[exp 1 + 1]\\] text\\\\^s[exp 2 + 3] end]";
        TaggedNode rootNode = PdmlParserUtil.parseString ( code );
        assertEquals ( "tag[2]", rootNode.getTag ().qualifiedTag () );
        assertEquals ( "text\\5 end", rootNode.toText() );
    }

    @Test
    void testStringLiteralExtension() throws Exception {

        TaggedNode rootNode = PdmlParserUtil.parseString ( "[root ^\"[[]]\"]" );
        assertEquals ( "[[]]", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( "[root before ^~|[[]]|~ after]" );
        assertEquals ( "before [[]] after", rootNode.toText() );

        rootNode = PdmlParserUtil.parseString ( """
            [root ^""\"
                line 1
                    line 2
                ""\"]""" );
        assertEquals ( "line 1\n    line 2", rootNode.toText() );
    }
}
