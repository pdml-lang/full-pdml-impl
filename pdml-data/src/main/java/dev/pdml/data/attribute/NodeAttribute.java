package dev.pdml.data.attribute;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public class NodeAttribute extends Parameter<String> {

    public NodeAttribute (
        @NotNull String name,
        @Nullable String value,
        @Nullable ParameterSpec<String> spec,
        @Nullable TextLocation nameLocation,
        @Nullable TextLocation valueLocation ) {

        super ( name, value, spec, nameLocation, valueLocation );
    }

    public NodeAttribute (
        @NotNull String name,
        @Nullable String value,
        @Nullable TextLocation nameLocation,
        @Nullable TextLocation valueLocation ) {

        super ( name, value, nameLocation, valueLocation );
    }

    public NodeAttribute (
        @NotNull TextToken nameToken,
        @Nullable TextToken valueToken ) {

        super ( nameToken.getText(), valueToken == null ? null : valueToken.getText(),
            nameToken.getLocation(), valueToken == null ? null : valueToken.getLocation() );
    }

    public NodeAttribute (
        @NotNull String name,
        @Nullable String value ) {

        super ( name, value );
    }
}
