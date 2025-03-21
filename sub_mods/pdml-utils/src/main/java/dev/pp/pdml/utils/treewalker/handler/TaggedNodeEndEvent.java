package dev.pp.pdml.utils.treewalker.handler;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;

public record TaggedNodeEndEvent(

    @Nullable TextLocation location,
    @NotNull TaggedNodeStartEvent startEvent ) {

    // public boolean isEmptyNode() { return startEvent.isEmptyNode(); }

    @Override
    public String toString() { return startEvent.tag () + " end"; }
}

/*
public record BranchNodeEndEvent(
    @NotNull BranchNode node ) {

    @Override
    public String toString() { return node.getName() + " end"; }
}

 */
