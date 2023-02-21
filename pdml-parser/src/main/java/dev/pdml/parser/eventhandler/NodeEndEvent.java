package dev.pdml.parser.eventhandler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;

public record NodeEndEvent (
    @Nullable TextLocation location,
    @NotNull NodeStartEvent startEvent ) {

    public boolean isEmptyNode() { return startEvent.isEmptyNode(); }
}
