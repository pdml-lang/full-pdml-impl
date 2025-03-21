package dev.pp.pdml.data.node;

import dev.pp.pdml.data.PdmlExtensionsConstants;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public record NodeTag(
    @NotNull String tag,
    @Nullable TextLocation tagLocation,
    @Nullable String namespacePrefix,
    @Nullable TextLocation namespacePrefixLocation ) {


    public static @NotNull NodeTag create (
        @NotNull String qualifiedTag,
        @Nullable TextLocation location ) {

        int separatorIndex = qualifiedTag.indexOf ( PdmlExtensionsConstants.NAMESPACE_SEPARATOR_CHAR );
        if ( separatorIndex == -1 || separatorIndex == 0 || separatorIndex == qualifiedTag.length() -1 ) {
            return new NodeTag ( qualifiedTag, location, null, null );
        } else {
            String namespacePrefix = qualifiedTag.substring ( 0, separatorIndex );
            String tag = qualifiedTag.substring ( separatorIndex + 1 );
            return new NodeTag ( tag, null, namespacePrefix, location );
        }
    }

    public static @NotNull NodeTag create ( @NotNull String qualifiedTag ) {
        return create ( qualifiedTag, null );
    }

    public static @NotNull NodeTag create ( @NotNull TextToken qualifiedTagToken ) {
        return create ( qualifiedTagToken.getText(), qualifiedTagToken.getLocation() );
    }

    public NodeTag ( @NotNull String tag ) {
        this ( tag, null, null, null );
    }

    /* Not used
    public NodeName (
        @NotNull TextToken localNameToken,
        @NotNull TextToken namespacePrefixToken ) {

        this ( localNameToken.getText(), localNameToken.getLocation(),
            namespacePrefixToken.getText(), namespacePrefixToken.getLocation() );
    }
     */

    public NodeTag (
        @NotNull String tag,
        @NotNull String namespacePrefix ) {

        this ( tag, null, namespacePrefix, null );
    }


    public boolean hasNamespacePrefix() { return namespacePrefix != null; }

    public @Nullable TextLocation location() {
        TextLocation location = namespacePrefixLocation;
        return location != null ? location : tagLocation;
    }

    public @NotNull TextToken qualifiedTagToken() {
        return new TextToken ( qualifiedTag (), location() );
    }

    public @NotNull TextToken tagToken() {
        return new TextToken ( tag, tagLocation );
    }

    public @Nullable TextToken namespacePrefixToken() {
        return namespacePrefix == null ? null : new TextToken ( namespacePrefix, namespacePrefixLocation );
    }

    public @NotNull String qualifiedTag() {

        StringBuilder sb = new StringBuilder();
        if ( namespacePrefix != null ) {
            sb.append ( namespacePrefix );
            sb.append ( PdmlExtensionsConstants.NAMESPACE_SEPARATOR_CHAR );
        }

        sb.append ( tag );

        return sb.toString();
    }

    @Override
    public boolean equals ( Object other ) {

        if ( other instanceof NodeTag o ) {
            return qualifiedTag ().equals ( o.qualifiedTag () );
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return qualifiedTag ().hashCode();
    }

    @Override
    public @NotNull String toString() { return qualifiedTag (); }
}
