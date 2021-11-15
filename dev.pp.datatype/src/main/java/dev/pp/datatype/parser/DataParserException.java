package dev.pp.datatype.parser;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;

public class DataParserException extends TextReaderException {

    public DataParserException (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @Nullable Exception cause ) {

        super ( id, message, token, cause );
    }
}
