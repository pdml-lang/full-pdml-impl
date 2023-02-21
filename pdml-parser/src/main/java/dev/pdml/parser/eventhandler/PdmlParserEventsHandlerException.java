package dev.pdml.parser.eventhandler;

import dev.pdml.shared.exception.PdmlDocumentException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.token.TextToken;

public class PdmlParserEventsHandlerException extends PdmlDocumentException {

    public PdmlParserEventsHandlerException ( @NotNull Exception handlerException, @Nullable TextToken token ) {

        super ( new TextError (
            "The following error occurred in the parser's event handler: " + handlerException.getMessage(),
            "PARSER_EVENT_HANDLER_ERROR",
            token ),
            handlerException );
    }
}
