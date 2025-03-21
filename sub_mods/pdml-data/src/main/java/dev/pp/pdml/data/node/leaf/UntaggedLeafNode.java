package dev.pp.pdml.data.node.leaf;

import dev.pp.pdml.data.node.Node;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public abstract class UntaggedLeafNode extends Node {

    protected @NotNull String text;
    public @NotNull String getText() { return text; }
    public void setText ( @NotNull String text ) {
        checkTextNotEmpty ( text );
        this.text = text;
    }

    private void checkTextNotEmpty ( @NotNull String text ) {

        if ( text.isEmpty() ) {
            throw new IllegalStateException ( "The node's text cannot be empty." );
        }
    }


    protected UntaggedLeafNode (
        @NotNull String text,
        @Nullable TextLocation startLocation ) {

        super ( startLocation );
        checkTextNotEmpty ( text );
        this.text = text;
    }


    public boolean isRootNode() { return false; }

    public boolean isTaggedNode () { return false; }
    public boolean isLeafNode() { return true; }

    public @NotNull TextToken textToken() {
        return new TextToken ( text, startLocation );
    }

    public @NotNull TextToken startToken() { return textToken(); }

    public @Nullable TextLocation getEndLocation() {

        // TODO compute endLocation from startLocation and text
        return null;
    }

    /* Not used
    public @Nullable BranchNode rootNodeOrNull() {

        if ( parent == null ) {
            return null;
        } else {
            // return parent.rootNode();
            return path().rootNode();
        }
    }
     */

    @Override
    public @NotNull String toString() { return text; }
}
