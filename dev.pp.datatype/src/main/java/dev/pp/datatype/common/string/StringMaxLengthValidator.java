package dev.pp.datatype.common.string;

import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.function.BiFunction;

public class StringMaxLengthValidator implements DataValidator<String> {


    public static String DEFAULT_ERROR_ID = "VALUE_TOO_LONG";

    public static BiFunction<String, Integer, String> DEFAULT_ERROR_MESSAGE_SUPPLIER = ( value, maxLength) ->
        "Value '" + value + "' (" + value.length() + ") is too long. The value cannot be longer than " +
            maxLength + " characters.";


    private final int maxLength;
    private final @NotNull String errorId;
    private final @NotNull BiFunction<String, Integer, String> errorMessageSupplier;


    public StringMaxLengthValidator (
        int maxLength,
        @NotNull String errorId,
        @NotNull BiFunction<String, Integer, String> errorMessageSupplier ) {

        if ( maxLength < 1 ) throw new IllegalArgumentException ( maxLength + " is invalid for maxLength. Must be >= 1." );

        this.maxLength = maxLength;
        this.errorId = errorId;
        this.errorMessageSupplier = errorMessageSupplier;
    }

    public StringMaxLengthValidator ( int maxLength ) {
        this ( maxLength, DEFAULT_ERROR_ID, DEFAULT_ERROR_MESSAGE_SUPPLIER );
    }


    public @NotNull String getErrorId () { return errorId; }

    public @NotNull BiFunction<String, Integer, String> getErrorMessageSupplier () { return errorMessageSupplier; }

    public int getMaxLength () { return maxLength; }


    public void validate ( @NotNull String string, @Nullable TextToken token ) throws DataValidatorException {

        if ( string.length() > maxLength ) throw new DataValidatorException (
            errorId,
            errorMessageSupplier.apply ( string, maxLength ),
            token, null );
    }
}
