package dev.pdml.core.exception;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class PDMLDocumentSemanticException extends PDMLDocumentException {

//    private final PXMLReaderError error;

/*
    public MalformedPXMLDocumentException ( PXMLReaderError error ) {

//        super ( error.getId(), error.getMessage(), error.getLocation() );
        super ( error.getId(), error.getMessage(), error.getLocation(), null );

//        this.error = error;
    }
*/

    public PDMLDocumentSemanticException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        super ( id, message, token );
    }

/*
    public PDMLDocumentSemanticException (
        @NotNull String message,
        @Nullable TextToken token ) {

        this ( "INVALID_PDML_DOCUMENT", message, token );
    }

    public String toString() {

        return error.toString();
    }
*/
}
