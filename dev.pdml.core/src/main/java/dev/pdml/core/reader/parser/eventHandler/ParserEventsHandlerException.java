package dev.pdml.core.reader.parser.eventHandler;

import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class ParserEventsHandlerException extends TextReaderException {

    public ParserEventsHandlerException ( @NotNull Exception handlerException, @Nullable TextToken token ) {

        super (
            "PARSER_EVENT_HANDLER_ERROR",
            "The following error occurred in the parser's event handler: " + handlerException.getMessage(),
            token,
            handlerException );
    }
}
