package dev.pp.pdml.data.exception;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.inspection.message.TextInspectionError;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public class PdmlException extends Exception {

    private final @Nullable String errorId;
    public @Nullable String getErrorId() { return errorId; }

    private final @Nullable String invalidTextFragment;
    public @Nullable String getInvalidTextFragment() { return invalidTextFragment; }

    private final @Nullable TextLocation invalidTextLocation;
    public @Nullable TextLocation getInvalidTextLocation() { return invalidTextLocation; }


    @Deprecated
    public static @NotNull PdmlException createForAbortingProgram() {
        return new PdmlException (
            "Operation aborted because errors were reported",
            "OPERATION_ABORTED",
            null );
    }

    public PdmlException (
        @NotNull String message,
        @Nullable String errorId,
        @Nullable String invalidTextFragment,
        @Nullable TextLocation invalidTextLocation,
        @Nullable Throwable cause ) {

        super ( message, cause );

        this.errorId = errorId;
        this.invalidTextFragment = invalidTextFragment;
        this.invalidTextLocation = invalidTextLocation;
    }

    public PdmlException (
        @NotNull String message,
        @Nullable String errorId,
        @Nullable TextToken errorToken,
        @Nullable Throwable cause ) {

        this ( message, errorId,
            errorToken == null ? null : errorToken.getText(),
            errorToken == null ? null : errorToken.getLocation(), cause );
    }

    public PdmlException (
        @NotNull String message,
        @Nullable String errorId,
        @Nullable TextToken errorToken ) {

        this ( message, errorId, errorToken, null );
    }

    public PdmlException ( @NotNull TextInspectionError textInspectionError ) {

        this ( textInspectionError.getMessage(),
            textInspectionError.getId(),
            textInspectionError.token() );
    }

    public PdmlException ( @NotNull InvalidTextException invalidTextException ) {

        this ( invalidTextException.getMessage(),
            invalidTextException.getErrorId(),
            invalidTextException.getInvalidTextFragment(),
            invalidTextException.getInvalidTextLocation(),
            invalidTextException.getCause() );
    }

    public PdmlException ( @NotNull Exception cause ) {
        this ( cause.getMessage(), null, null, cause );
    }


    public @NotNull InvalidTextException toInvalidTextException() {
        return new InvalidTextException ( getMessage(), errorId, invalidTextFragment, invalidTextLocation, getCause() );
    }

    @Nullable public TextToken textToken() {
        return this.invalidTextFragment == null ? null : new TextToken ( invalidTextFragment, invalidTextLocation );
    }
}
