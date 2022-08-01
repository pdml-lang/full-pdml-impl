package dev.pdml.core.exception;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

public abstract class PDMLDocumentException extends TextErrorException {

//    private final PXMLReaderError error;

/*
    public MalformedPXMLDocumentException ( PXMLReaderError error ) {

//        super ( error.getId(), error.getMessage(), error.getLocation() );
        super ( error.getId(), error.getMessage(), error.getLocation(), null );

//        this.error = error;
    }
*/

    public PDMLDocumentException ( @NotNull TextError textError ) {

        super ( textError );
    }

    public PDMLDocumentException ( @NotNull TextError textError, @NotNull Exception cause ) {

        super ( textError, cause );
    }

    protected PDMLDocumentException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token ) {

        super ( id, message, token, null );
    }

/*
    protected PDMLDocumentException (
        @Nullable String id,
        @NotNull String message,
        @Nullable TextToken token,
        @NotNull Exception cause ) {

        super ( id, message, token, cause );
    }

 */
/*
    public String toString() {

        return error.toString();
    }
*/
}
