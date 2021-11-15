package dev.pp.parameters.parameter;

import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.string.StringConstants;
import dev.pp.text.utilities.string.StringTruncator;

public class Parameter<T> {


    private final @NotNull String name;
    private final @Nullable T value;
    private final @Nullable FormalParameter<T> formalParameter;


    public Parameter ( @NotNull String name, @Nullable T value, @Nullable FormalParameter<T> formalParameter ) {

        this.name = name;
        this.value = value;
        this.formalParameter = formalParameter;
    }

    public Parameter ( @NotNull String name, T value ) {

        this ( name, value, null );
    }


    public @NotNull String getName () { return name; }

    public @Nullable T getValue () { return value; }

    public @Nullable FormalParameter<T> getFormalParameter () { return formalParameter; }


    public String valueAsString() {

        if ( formalParameter != null ) {
            return formalParameter.objectToString ( value, StringConstants.NULL_AS_STRING );
        } else {
            return value == null ? StringConstants.NULL_AS_STRING : value.toString();
        }
    }


    @Override
    public String toString() {

        return name + ": " + StringTruncator.truncateWithEllipses ( valueAsString() );
    }
}
