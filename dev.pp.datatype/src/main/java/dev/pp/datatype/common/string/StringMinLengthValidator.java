package dev.pp.datatype.common.string;

import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.function.BiFunction;

public class StringMinLengthValidator implements DataValidator<String> {


    public static String DEFAULT_ERROR_ID = "VALUE_TOO_SHORT";

    public static BiFunction<String, Integer, String> DEFAULT_ERROR_MESSAGE_SUPPLIER = ( value, minLength) ->
        "Value '" + value + "' (" + value.length() + ") is too short. The value must at least be " +
            minLength + " characters long.";


    private final int minLength;
    private final @NotNull String errorId;
    private final @NotNull BiFunction<String, Integer, String> errorMessageSupplier;


    public StringMinLengthValidator (
        int minLength,
        @NotNull String errorId,
        @NotNull BiFunction<String, Integer, String> errorMessageSupplier ) {

        if ( minLength < 1 ) throw new IllegalArgumentException ( minLength + " is invalid for minLength. Must be >= 1." );

        this.minLength = minLength;
        this.errorId = errorId;
        this.errorMessageSupplier = errorMessageSupplier;
    }

    public StringMinLengthValidator ( int minLength ) {
        this ( minLength, DEFAULT_ERROR_ID, DEFAULT_ERROR_MESSAGE_SUPPLIER );
    }


    public @NotNull String getErrorId () { return errorId; }

    public @NotNull BiFunction<String, Integer, String> getErrorMessageSupplier () { return errorMessageSupplier; }

    public int getMinLength () { return minLength; }


    public void validate ( @NotNull String string, @Nullable TextToken token ) throws DataValidatorException {

        if ( string.length() < minLength ) throw new DataValidatorException (
            errorId,
            errorMessageSupplier.apply ( string, minLength ),
            token, null );
    }
}
