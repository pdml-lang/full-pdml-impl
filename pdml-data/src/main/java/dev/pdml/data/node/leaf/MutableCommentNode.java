package dev.pdml.data.node.leaf;

import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;

public class MutableCommentNode extends MutableLeafNode {

    public MutableCommentNode (
        @Nullable MutableRootOrBranchNode parent,
        @NotNull String text,
        @Nullable TextLocation location ) {

        super ( parent, text, location );
    }

    public MutableCommentNode (
        @NotNull String text,
        @Nullable TextLocation location ) {

        this ( null, text, location );
    }

    public MutableCommentNode (
        @NotNull String text ) {

        this ( null, text, null );
    }
}
