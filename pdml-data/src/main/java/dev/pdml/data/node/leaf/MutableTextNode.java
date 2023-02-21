package dev.pdml.data.node.leaf;

import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;

public class MutableTextNode extends MutableLeafNode {

    public MutableTextNode (
        @Nullable MutableRootOrBranchNode parent,
        @NotNull String text,
        @Nullable TextLocation location ) {

        super ( parent, text, location );
    }

    public MutableTextNode (
        @NotNull String text,
        @Nullable TextLocation location ) {

        this ( null, text, location );
    }

    public MutableTextNode (
        @NotNull String text ) {

        this ( null, text, null );
    }
}
