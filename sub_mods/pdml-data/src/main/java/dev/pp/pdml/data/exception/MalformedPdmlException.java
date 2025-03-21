package dev.pp.pdml.data.exception;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.inspection.message.TextInspectionError;
import dev.pp.core.text.token.TextToken;

public class MalformedPdmlException extends PdmlException {

    public MalformedPdmlException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }

    public MalformedPdmlException ( @NotNull TextInspectionError textInspectionError ) {
        super ( textInspectionError );
    }

    public MalformedPdmlException ( @NotNull InvalidTextException invalidTextException ) {
        super ( invalidTextException );
    }
}
