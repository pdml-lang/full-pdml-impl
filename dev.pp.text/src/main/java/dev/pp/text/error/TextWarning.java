package dev.pp.text.error;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class TextWarning extends TextErrorOrWarning {

    public static final String LABEL = "Warning";

    public static @NotNull String toString (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        return TextErrorOrWarning.toString ( LABEL, id, message, token );
    }

    public static @NotNull String toLogString (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        return TextErrorOrWarning.toLogString ( LABEL, id, message, token );
    }


    public TextWarning ( @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        super ( id, message, token );
    }


    public @NotNull String getLabel() { return LABEL; };

    public @NotNull String toString() { return toString ( id, message, token ); }

    public @NotNull String toLogString () { return toLogString ( id, message, token ); }

    // public @NotNull String getLabel() { return LABEL; }
}
