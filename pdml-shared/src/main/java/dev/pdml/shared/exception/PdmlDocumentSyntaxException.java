package dev.pdml.shared.exception;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.token.TextToken;

public class PdmlDocumentSyntaxException extends PdmlDocumentException {

    public PdmlDocumentSyntaxException ( @NotNull TextError textError ) {
        super ( textError );
    }

    public PdmlDocumentSyntaxException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }
}
