package dev.pdml.shared.exception;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

public abstract class PdmlDocumentException extends TextErrorException {

    protected PdmlDocumentException ( @NotNull TextError textError ) {
        super ( textError );
    }

    protected PdmlDocumentException ( @NotNull TextError textError, @NotNull Exception cause ) {
        super ( textError, cause );
    }

    protected PdmlDocumentException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }
}
