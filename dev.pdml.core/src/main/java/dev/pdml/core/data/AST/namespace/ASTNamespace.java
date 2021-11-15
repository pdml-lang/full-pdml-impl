package dev.pdml.core.data.AST.namespace;

import dev.pdml.core.Constants;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
// import dev.pdml.core.data.AST.ASTElement;
import dev.pp.text.token.TextToken;

import java.net.URI;

public class ASTNamespace { // implements ASTElement {

    public static final String DEFAULT_NAMESPACE_PREFIX = "_default";

    public static @NotNull ASTNamespace namespaceToASTNamespace ( @NotNull Namespace namespace ) {

        return new ASTNamespace (
            new TextToken ( namespace.getPrefix() ),
            new TextToken ( namespace.getURI().toString() ),
            namespace.getURI() );
    }


    private final @NotNull TextToken prefix; // @Nullable? // rename to prefixToken
    private final @NotNull TextToken URIToken; // rename to nameToken
    private final @NotNull URI URI; // TODO remove


    public ASTNamespace (
        @NotNull TextToken prefix,
        @NotNull TextToken URIToken,
        @NotNull URI URI ) {

        this.prefix = prefix;
        this.URIToken = URIToken;
        this.URI = URI;
    }


    public @NotNull TextToken getPrefix() { return prefix; }

    public @NotNull TextToken getURIToken () { return URIToken; }

    public @NotNull URI getURI() { return URI; }


    public @NotNull String getPrefixText() { return prefix.getText(); }

    public @NotNull String getURIText() { return URIToken.getText(); }

    public @Nullable TextLocation getLocation () { return prefix.getLocation(); }

    public boolean isDefaultNamespace() { return getPrefixText().equals ( DEFAULT_NAMESPACE_PREFIX ); }

    public @NotNull String toString() {

        return getPrefixText() + Constants.ATTRIBUTE_ASSIGN + getURIText();
    }

/*
    public static @NotNull ASTNamespace namespaceToASTNamespace ( @NotNull Namespace namespace ) {

        return new ASTNamespace ( namespace.getPrefix(), namespace.getURI(), null );
    }


    private final @NotNull String prefix;
    private final @NotNull URI URI;
    private final @Nullable TextLocation declarationLocation;


    public ASTNamespace (
        @NotNull String prefix,
        @NotNull URI URI,
        @Nullable TextLocation declarationLocation ) {

        this.prefix = prefix;
        this.URI = URI;
        this.declarationLocation = declarationLocation;
    }


    public @NotNull String getPrefix() { return prefix; }

    public @NotNull URI getURI() { return URI; }

    public @Nullable TextLocation getLocation () { return declarationLocation; }


    public boolean isDefaultNamespace() { return prefix.equals ( DEFAULT_NAMESPACE_PREFIX ); }

    public @NotNull String toString() {

        return prefix + Constants.ATTRIBUTE_ASSIGN + URI;
    }
*/
}
