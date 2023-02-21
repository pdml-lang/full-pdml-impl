package dev.pdml.parser.eventhandler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public record NodeCommentEvent(
    @NotNull String comment,
    @Nullable TextLocation location,
    @NotNull NodeStartEvent startEvent ) {

    public @NotNull TextToken textToken() { return new TextToken ( comment, location ); }
}

