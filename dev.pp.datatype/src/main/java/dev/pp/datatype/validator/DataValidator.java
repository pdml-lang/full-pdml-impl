package dev.pp.datatype.validator;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

public interface DataValidator<T> {

    void validate ( @NotNull T object, @Nullable TextToken token ) throws DataValidatorException;

    default boolean isValid ( @NotNull T object ) {

        try {
            validate ( object, null );
            return true;
        } catch ( DataValidatorException e ) {
            return false;
        }
    }

    /* TODO?
    default void checkError (
        @NotNull T object,
        @Nullable TextLocation location,
        @NotNull TextErrorHandler errorHandler ) {

        try {
            validate ( object, null );
        } catch ( ObjectValidatorException e ) {
            errorHandler.handleException ( e );
            errorHandler.handleError ( e.getId(), e.getMessage(), location );
        }
    }
    */
}
