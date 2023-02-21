package dev.pdml.data.node.leaf;

import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pdml.data.node.branch.MutableChildNode;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;

public abstract class MutableLeafNode implements MutableChildNode {

    private @Nullable MutableRootOrBranchNode parent;

    public @Nullable MutableRootOrBranchNode getParent() { return parent; }
    public void setParent ( @Nullable MutableRootOrBranchNode parent ) { this.parent = parent; }

    private @NotNull String text;
    public @NotNull String getText() { return text; }
    public void setText ( @NotNull String text ) { this.text = text; }

    private @Nullable TextLocation location;
    public @Nullable TextLocation getLocation() { return location; }
    public void setLocation ( @Nullable TextLocation location ) { this.location = location; }


    protected MutableLeafNode (
        @Nullable MutableRootOrBranchNode parent,
        @NotNull String text,
        @Nullable TextLocation location ) {

        this.parent = parent;
        this.text = text;
        this.location = location;
    }
}
