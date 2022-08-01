package dev.pdml.core.exception;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.token.TextToken;

public class PDMLDocumentSyntaxException extends PDMLDocumentException {

//    private final PXMLReaderError error;

/*
    public MalformedPXMLDocumentException ( PXMLReaderError error ) {

//        super ( error.getId(), error.getMessage(), error.getLocation() );
        super ( error.getId(), error.getMessage(), error.getLocation(), null );

//        this.error = error;
    }
*/

    public PDMLDocumentSyntaxException ( @NotNull TextError textError ) {

        super ( textError );
    }

    public PDMLDocumentSyntaxException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        super ( id, message, token );
    }
/*
    public String toString() {

        return error.toString();
    }
*/
}
