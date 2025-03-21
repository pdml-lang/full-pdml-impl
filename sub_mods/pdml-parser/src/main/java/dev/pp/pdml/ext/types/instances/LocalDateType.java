package dev.pp.pdml.ext.types.instances;

import dev.pp.pdml.data.exception.InvalidPdmlDataException;
import dev.pp.pdml.ext.types.AbstractPdmlType;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.token.TextToken;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDateType extends AbstractPdmlType<LocalDate> {

    private static final @NotNull AbstractPdmlType.ObjectParser<LocalDate>
        DEFAULT_OBJECT_PARSER = pdmlParser -> {

        @NotNull TextToken textToken = readTrimmedTextToken ( pdmlParser.getPdmlReader() );
        if ( textToken.isNullText() ) {
            return new ObjectTokenPair<> ( null, textToken );
        } else {
            String stringValue = textToken.getText();
            try {
                return new ObjectTokenPair<> ( LocalDate.parse ( stringValue ), textToken );
            } catch ( DateTimeParseException e ) {
                throw new InvalidPdmlDataException (
                    "'" + stringValue + "' is an invalid date. Reason: " + e.getMessage (),
                    "ILLEGAL_DATE_VALUE",
                    textToken );
            }
        }
    };

    private static final @Nullable AbstractPdmlType.ObjectValidator<LocalDate>
        DEFAULT_OBJECT_VALIDATOR = null;

    private static final @Nullable AbstractPdmlType.ObjectHandler<LocalDate>
        DEFAULT_OBJECT_HANDLER = ( objectTokenPair, parentNode, pdmlReader ) -> {
            LocalDate localDate = objectTokenPair.object();
            handleTextObject (
                localDate, localDate == null ? null : localDate.toString(),
                parentNode, pdmlReader, false );
        };


    public static final @NotNull LocalDateType NON_NULL_INSTANCE = new LocalDateType (
        "local_date", false, DEFAULT_OBJECT_PARSER, DEFAULT_OBJECT_VALIDATOR, DEFAULT_OBJECT_HANDLER );

    public static final @NotNull LocalDateType NULLABLE_INSTANCE = new LocalDateType (
        "local_date_or_null", true, DEFAULT_OBJECT_PARSER, DEFAULT_OBJECT_VALIDATOR, DEFAULT_OBJECT_HANDLER );


    public LocalDateType (
        @NotNull String name,
        boolean isNullAllowed,
        @NotNull ObjectParser<LocalDate> objectParser,
        @Nullable ObjectValidator<LocalDate> objectValidator,
        @Nullable ObjectHandler<LocalDate> objectHandler ) {

        super ( name, isNullAllowed, objectParser, objectValidator, objectHandler );
    }
}
