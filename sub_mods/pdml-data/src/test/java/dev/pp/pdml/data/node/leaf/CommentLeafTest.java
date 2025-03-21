package dev.pp.pdml.data.node.leaf;

import dev.pp.pdml.data.node.leaf.CommentLeaf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentLeafTest {

    @Test
    void removeDelimiters () {

        assertEquals ( "foo", CommentLeaf.removeDelimiters ( "^/*foo*/" ) );
        assertEquals ( " ", CommentLeaf.removeDelimiters ( "^/* */" ) );
        assertEquals ( " foo ", CommentLeaf.removeDelimiters ( "^/** foo **/" ) );
        assertEquals ( " ", CommentLeaf.removeDelimiters ( "^/*** ***/" ) );
        assertEquals ( "foo", CommentLeaf.removeDelimiters ( "foo" ) );
    }
}
