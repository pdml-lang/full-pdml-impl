package dev.pp.datatype.validator;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainedDataValidator<T> implements DataValidator<T> {

    private final @NotNull List<DataValidator<T>> validators;


    public ChainedDataValidator ( @NotNull List<DataValidator<T>> validators ) {
        this.validators = validators;
    }

    public ChainedDataValidator ( @NotNull DataValidator<T>[] validators ) {
        this.validators = Arrays.asList ( validators );
    }

    public ChainedDataValidator () {
        this.validators = new ArrayList<>();
    }


    public ChainedDataValidator<T> add ( @NotNull DataValidator<T> validator ) {

        validators.add ( validator );
        return this;
    }

    public void validate ( @NotNull T object, @Nullable TextToken token ) throws DataValidatorException {

        for ( DataValidator<T> validator : validators ) {
            validator.validate ( object, token );
        }
    }

}
