package dev.pp.pdml.utils.treewalker.handler;

import dev.pp.pdml.data.node.leaf.CommentLeaf;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;

public record CommentEvent(
    @NotNull String comment,
    @Nullable TextLocation location,
    @NotNull TaggedNodeStartEvent startEvent ) {

    // public @NotNull TextToken textToken() { return new TextToken ( comment, location ); }

    public @NotNull String commentWithoutDelimiters() {
        return CommentLeaf.removeDelimiters ( comment );
    }

    @Override
    public String toString() { return commentWithoutDelimiters(); }
}

/*
public record CommentLeafEvent(
    @NotNull CommentLeaf commentLeaf,
    @NotNull BranchNode parentNode ) {

    @Override
    public String toString() { return commentLeaf.getText(); }
}

 */
