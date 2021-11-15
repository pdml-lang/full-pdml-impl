package dev.pdml.core.reader.exception;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PXMLResourceException extends TextReaderException {

    public PXMLResourceException (
        @NotNull String id, @NotNull String message, @Nullable TextToken token, @NotNull IOException ioException ) {

        super ( id, message, token, ioException );
    }
}
