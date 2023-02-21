package dev.pdml.shared.exception;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class PdmlDocumentSemanticException extends PdmlDocumentException {

    public PdmlDocumentSemanticException (
        @NotNull String message,
        @Nullable String id,
        @Nullable TextToken token ) {

        super ( message, id, token );
    }
}
