package dev.pp.texttable.data.impls;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.util.function.Function;

public class FormField<VALUE> {


    private final @Nullable String label;
    private final @Nullable VALUE value;
    private final @NotNull Function<VALUE, String> valueToStringConverter;


    public FormField (
        @Nullable String label,
        @Nullable VALUE value,
        @NotNull Function<VALUE, String> valueToStringConverter ) {

        this.label = label;
        this.value = value;
        this.valueToStringConverter = valueToStringConverter;
    }

    public FormField (
        @Nullable String label,
        @Nullable VALUE value ) {

        this ( label, value, Object::toString );
    }


    public @Nullable String getLabel() { return label; }

    public @Nullable VALUE getValue() { return value; }

    public @NotNull Function<VALUE, String> getValueToStringConverter() { return valueToStringConverter; }


    public @Nullable String getValueAsString() {

        if ( value != null ) {
            return valueToStringConverter.apply ( value );
        } else {
            return null;
        }
    }
}
