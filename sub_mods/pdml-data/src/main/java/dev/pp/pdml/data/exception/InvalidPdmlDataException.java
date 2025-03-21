package dev.pp.pdml.data.exception;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.inspection.InvalidDataException;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.inspection.message.TextInspectionError;
import dev.pp.core.text.token.TextToken;

public class InvalidPdmlDataException extends PdmlException {

    public  InvalidPdmlDataException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token,
        @Nullable Throwable cause ) {

        super ( message, id, token, cause );
    }

    public InvalidPdmlDataException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }

    public InvalidPdmlDataException ( @NotNull TextInspectionError textInspectionError ) {
        super ( textInspectionError );
    }

    public InvalidPdmlDataException ( @NotNull InvalidTextException invalidTextException ) {
        super ( invalidTextException );
    }

    public InvalidPdmlDataException (
        @NotNull InvalidDataException invalidDataException ) {

        this ( invalidDataException.getMessage(),
            invalidDataException.getErrorId(),
            invalidDataException.invalidTextToken(),
            invalidDataException.getCause() );
    }

    public InvalidPdmlDataException ( @NotNull Exception cause ) {
        super ( cause );
    }
}
