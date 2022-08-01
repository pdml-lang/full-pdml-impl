package dev.pdml.core.parser.eventHandler;

import dev.pdml.core.exception.PDMLDocumentException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.token.TextToken;

public class PDMLParserEventsHandlerException extends PDMLDocumentException {

    public PDMLParserEventsHandlerException ( @NotNull Exception handlerException, @Nullable TextToken token ) {

        super ( new TextError (
            "PARSER_EVENT_HANDLER_ERROR",
            "An error occurred in the parser's event handler.",
            token ),
            handlerException );
    }
}
