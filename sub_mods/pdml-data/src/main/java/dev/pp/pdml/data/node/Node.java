package dev.pp.pdml.data.node;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.tagged.ChildNodes;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {


    protected @Nullable TaggedNode parent;
    public @Nullable TaggedNode getParent() { return parent; }

    // CAUTION: must only be called by 'TaggedNode' (to ensure data integrity)
    public void setParent ( @Nullable TaggedNode parent ) { this.parent = parent; }

    protected @Nullable TextLocation startLocation;
    public @Nullable TextLocation getStartLocation() { return startLocation; }
    // public void setStartLocation ( @Nullable TextLocation startLocation ) { this.startLocation = startLocation; }

    // protected @Nullable TextLocation endLocation;
    public abstract @Nullable TextLocation getEndLocation();


    protected Node (
        @Nullable TextLocation startLocation ) {

        this.parent = null;
        this.startLocation = startLocation;
    }

/*
    protected Node (
        @Nullable TextLocation startLocation ) {

        this ( startLocation, null );
    }
 */


    // isxxx

    public abstract boolean isRootNode();
    public abstract boolean isTaggedNode();
    // TODO public abstract boolean isBranchNode();
    public abstract boolean isLeafNode();
    public abstract boolean isTextLeaf();
    public abstract boolean isCommentLeaf();


    // Path

    public @NotNull NodePath path() {

        List<Node> nodes;
        if ( parent == null ) {
            nodes = List.of ( this );
        } else {
            nodes = new ArrayList<> ( parent.path().getNodes () );
            nodes.add ( this );
        }

        return new NodePath ( nodes );
    }


    // Token

    public abstract @Nullable TextToken startToken();


    // Index

    public @Nullable Integer childIndex() {

        if ( parent == null ) {
            return null;
        } else {
            return parent.getChildNodes().indexOfChild ( this );
        }
    }

    public @Nullable Integer childIndexForHumans() {

        Integer index = childIndex();
        return index == null ? null : index + 1;
    }


    // Siblings

    public boolean hasSiblings() {
        return parent != null && parent.getChildNodes().size() > 1;
    }

    public @Nullable Node nextSibling() {

        if ( parent == null ) return null;

        Integer thisIndex = childIndex();
        if ( thisIndex == null ) return null;

        ChildNodes childNodes = parent.getChildNodes();
        if ( childNodes.size() > thisIndex + 1 ) {
            return childNodes.get ( thisIndex + 1 );
        } else {
            return null;
        }
    }

    public @Nullable Node previousSibling() {

        if ( parent == null ) return null;

        Integer thisIndex = childIndex ();
        if ( thisIndex == null ) return null;

        if ( thisIndex > 0 ) {
            return parent.getChildNodes().get ( thisIndex - 1 );
        } else {
            return null;
        }
    }

    public @Nullable Node firstSibling() {

        if ( parent == null ) return null;

        ChildNodes childNodes = parent.getChildNodes();
        if ( childNodes.isNotEmpty() ) {
            return childNodes.first();
        } else {
            return null;
        }
    }

    public @Nullable Node lastSibling() {

        if ( parent == null ) return null;

        ChildNodes childNodes = parent.getChildNodes();
        if ( childNodes.isNotEmpty() ) {
            return childNodes.last();
        } else {
            return null;
        }
    }

    // TODO? public @Nullable List<Node> nextSiblings() {
    // TODO? public @Nullable List<Node> previousSiblings() {
}
