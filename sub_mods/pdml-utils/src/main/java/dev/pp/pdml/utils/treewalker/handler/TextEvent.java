package dev.pp.pdml.utils.treewalker.handler;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public record TextEvent(
    @NotNull String text,
    @Nullable TextLocation location,
    @NotNull TaggedNodeStartEvent startEvent ) {

    public @NotNull TextToken textToken() { return new TextToken ( text, location ); }

    @Override
    public String toString() { return text; }
}

/*
public record TextLeafEvent(
    @NotNull TextLeaf textLeaf,
    @NotNull BranchNode parentNode ) {

    @Override
    public String toString() { return textLeaf.getText(); }
}
 */


