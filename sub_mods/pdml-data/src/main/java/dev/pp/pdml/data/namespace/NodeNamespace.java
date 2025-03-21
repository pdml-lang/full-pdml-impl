package dev.pp.pdml.data.namespace;

import dev.pp.pdml.data.PdmlExtensionsConstants;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameter.Parameter;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public record NodeNamespace (
    @NotNull String namePrefix,
    @NotNull String URI,
    @Nullable TextLocation namePrefixLocation,
    @Nullable TextLocation URILocation ) {


    public NodeNamespace (
        @NotNull String namePrefix,
        @NotNull String URI ) {

        this ( namePrefix, URI, null, null );
    }

    public NodeNamespace (
        @NotNull TextToken namePrefixToken,
        @NotNull TextToken URIToken ) {

        this (
            namePrefixToken.getText(),
            URIToken.getText(),
            namePrefixToken.getLocation(),
            URIToken.getLocation() );
    }

    public NodeNamespace ( @NotNull Parameter<String> stringParameter ) {
        this ( stringParameter.nameToken(), stringParameter.valueToken() );
    }


    public @NotNull TextToken URIToken() { return  new TextToken ( URI, URILocation ); }


    // TODO? public boolean isDefaultNamespace() { return namePrefix.equals ( DEFAULT_NAMESPACE_PREFIX ); }

    @Override
    public @NotNull String toString() {
        return namePrefix + PdmlExtensionsConstants.ATTRIBUTE_ASSIGN_CHAR + URI;
    }
}
