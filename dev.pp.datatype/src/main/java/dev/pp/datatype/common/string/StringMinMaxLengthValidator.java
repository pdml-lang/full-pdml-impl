package dev.pp.datatype.common.string;

import dev.pp.datatype.validator.ChainedDataValidator;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class StringMinMaxLengthValidator implements DataValidator<String> {


    private final @NotNull ChainedDataValidator<String> chainedValidator;


    public StringMinMaxLengthValidator (
        int minLength,
        @NotNull String minErrorId,
        @NotNull BiFunction<String, Integer, String> minErrorMessageSupplier,
        int maxLength,
        @NotNull String maxErrorId,
        @NotNull BiFunction<String, Integer, String> maxErrorMessageSupplier ) {

        if ( minLength > maxLength ) throw new IllegalArgumentException (
            "minLength (" + minLength + ") cannot be greater than maxLength (" + maxLength + ")." );

        List<DataValidator<String>> list = new ArrayList<>();
        list.add ( new StringMinLengthValidator ( minLength, minErrorId, minErrorMessageSupplier ) );
        list.add ( new StringMaxLengthValidator ( maxLength, maxErrorId, maxErrorMessageSupplier ) );

        this.chainedValidator = new ChainedDataValidator<String> ( list );
    }


    public StringMinMaxLengthValidator ( int minLength, int maxLength ) {
        this (
            minLength, StringMinLengthValidator.DEFAULT_ERROR_ID, StringMinLengthValidator.DEFAULT_ERROR_MESSAGE_SUPPLIER,
            maxLength, StringMaxLengthValidator.DEFAULT_ERROR_ID, StringMaxLengthValidator.DEFAULT_ERROR_MESSAGE_SUPPLIER );
    }


    public @NotNull ChainedDataValidator<String> getChainedValidator () { return chainedValidator; }


    public void validate ( @NotNull String string, @Nullable TextToken token ) throws DataValidatorException {

        chainedValidator.validate ( string, token );
    }
}
