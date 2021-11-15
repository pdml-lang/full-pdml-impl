package dev.pp.text.token;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.location.TextLocation;
import dev.pp.text.utilities.string.StringTruncator;

public class TextToken {


    private final @NotNull String text;
    private final @Nullable TextLocation location;


    public TextToken ( @NotNull String text, @Nullable TextLocation location ) {
        if ( text.isEmpty() ) throw new IllegalArgumentException ( "'text' cannot be empty." );

        this.text = text;
        this.location = location;
    }

    public TextToken ( char c, @Nullable TextLocation location ) { this ( String.valueOf ( c ), location ); }

    public TextToken ( @NotNull String text ) { this ( text, null ); }


    public @NotNull String getText () { return text; }

    public @Nullable TextLocation getLocation () { return location; }


    /* TODO
        public @Nullable TextLocation getEndLocation () {
            // compute from text and location
        }
    */

    public @Nullable String getResourceName() { return location == null? null : location.getResourceName(); }


    @Override public String toString() {
        return StringTruncator.truncateWithEllipses ( text ) +
        ( location != null ? " at " + location : "" ); }
}
