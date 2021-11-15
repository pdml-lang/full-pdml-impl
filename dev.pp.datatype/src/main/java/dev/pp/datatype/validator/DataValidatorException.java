package dev.pp.datatype.validator;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;

public class DataValidatorException extends TextReaderException {

    public DataValidatorException (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable Exception cause ) {

        super ( id, message, token, cause );
    }
}
