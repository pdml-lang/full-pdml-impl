package dev.pdml.data.node;

import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

public record NodeName (
    @NotNull String localName,
    @Nullable TextLocation localNameLocation,
    @Nullable String namespacePrefix,
    @Nullable TextLocation namespacePrefixLocation ) {


    public NodeName ( @NotNull String localName ) {
        this ( localName, null, null, null );
    }

    public NodeName ( @NotNull TextToken localNameToken ) {
        this ( localNameToken.getText(), localNameToken.getLocation(), null, null );
    }

    public NodeName ( @NotNull TextToken localNameToken, @NotNull TextToken namespacePrefixToken ) {
        this ( localNameToken.getText(), localNameToken.getLocation(),
            namespacePrefixToken.getText(), namespacePrefixToken.getLocation() );
    }

    public NodeName ( @NotNull String localName, @NotNull String namespacePrefix ) {
        this ( localName, null, namespacePrefix, null );
    }


    public boolean hasNamespacePrefix() { return namespacePrefix != null; }
    public @NotNull TextToken localNameToken () { return new TextToken ( localName, localNameLocation ); }

    public @Nullable TextToken namespacePrefixToken() {
        return namespacePrefix == null ? null : new TextToken ( namespacePrefix, namespacePrefixLocation );
    }

/*
    public @Nullable NodeNamespace namespace ( @NotNull NamespaceGetter namespaceGetter ) {

        return namespacePrefix == null ? null : namespaceGetter.getByPrefixOrNull ( namespacePrefix );
    }
 */

    public @NotNull String qualifiedName() {

        StringBuilder sb = new StringBuilder();
        if ( namespacePrefix != null ) {
            sb.append ( namespacePrefix );
            sb.append ( PdmlExtensionsConstants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        sb.append ( localName );

        return sb.toString();
    }

    @Override
    public @NotNull String toString() { return qualifiedName(); }
}
