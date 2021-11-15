package dev.pp.datatype.common.string;

import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.string.StringUtils;

import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class StringMatchesRegexValidator implements DataValidator<String> {


    public static String DEFAULT_ERROR_ID = "INVALID_STRING_VALUE";

    public static BiFunction<String, Pattern, String> DEFAULT_ERROR_MESSAGE_SUPPLIER = ( string, pattern) ->
        "The value '" + string + "' does not match the regular expression '" + pattern + "'.";


    private final @NotNull Pattern pattern;
    private final @NotNull String errorId;
    private final @NotNull BiFunction<String, Pattern, String> errorMessageSupplier;


    public StringMatchesRegexValidator (
        @NotNull Pattern pattern,
        @NotNull String errorId,
        @NotNull BiFunction<String, Pattern, String> errorMessageSupplier ) {

        this.pattern = pattern;
        this.errorId = errorId;
        this.errorMessageSupplier = errorMessageSupplier;
    }

    public StringMatchesRegexValidator ( @NotNull Pattern pattern ) {
        this ( pattern, DEFAULT_ERROR_ID, DEFAULT_ERROR_MESSAGE_SUPPLIER );
    }

    public StringMatchesRegexValidator ( @NotNull String pattern ) {
        this ( Pattern.compile ( pattern ) );
    }


    public @NotNull String getErrorId () { return errorId; }

    public @NotNull BiFunction<String, Pattern, String> getErrorMessageSupplier () { return errorMessageSupplier; }

    public @NotNull Pattern getPattern () { return pattern; }


    public void validate ( @NotNull String string, @Nullable TextToken token ) throws DataValidatorException {

        if ( ! StringUtils.stringMatchesRegex ( string, pattern ) )
            throw new DataValidatorException (
                errorId,
                errorMessageSupplier.apply ( string, pattern ),
                token, null );
    }
}
