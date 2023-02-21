package dev.pdml.reader;

import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.token.TextToken;

public class PdmlReaderErrorHelper {

    public static void nonAbortingErrorAtCurrentLocation (
        @NotNull String message,
        @NotNull String id,
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        nonAbortingError ( message, id, reader.currentToken(), errorHandler );
    }

    public static void nonAbortingError (
        @NotNull String message,
        @NotNull String id,
        @Nullable TextToken token,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        errorHandler.handleMessage ( new TextError ( message, id, token ) );
    }

    public static @NotNull PdmlDocumentSyntaxException abortingSyntaxErrorAtCurrentLocation (
        @NotNull String message,
        @NotNull String id,
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        return abortingSyntaxError ( message, id, reader.currentToken(), errorHandler );
    }

    public static @NotNull PdmlDocumentSyntaxException abortingSyntaxError (
        @NotNull String message,
        @NotNull String id,
        @Nullable TextToken token,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        TextError error = new TextError ( message, id, token );
        errorHandler.handleMessage ( error );
        return new PdmlDocumentSyntaxException ( error );
    }
}
