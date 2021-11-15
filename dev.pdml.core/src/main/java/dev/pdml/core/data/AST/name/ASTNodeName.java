package dev.pdml.core.data.AST.name;

import dev.pdml.core.Constants;
// import dev.pdml.core.data.AST.ASTElement;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class ASTNodeName { // implements ASTElement {


    private final @NotNull TextToken localName;
    private final @Nullable ASTNamespace namespace;


    public ASTNodeName (
        @NotNull TextToken localName,
        @Nullable ASTNamespace namespace ) {

        this.localName = localName;
        this.namespace = namespace;
    }


    public @NotNull TextToken getLocalName() { return localName; }

    public @Nullable ASTNamespace getNamespace() { return namespace; }

    public @NotNull TextToken getToken() {
        return namespace != null ? namespace.getPrefix() : localName; }

    public @Nullable TextLocation getLocation () {
        return namespace != null ? namespace.getLocation () : localName.getLocation(); }

    public @NotNull String getLocalNameText() { return localName.getText(); }

    public boolean hasNamespace() { return namespace != null; }

    public @Nullable String getNamespacePrefixText() {

        if ( namespace != null ) {
            return namespace.getPrefixText();
        } else {
            return null;
        }
    }

    public @Nullable String getNamespaceURIText() {

        if ( namespace != null ) {
            return namespace.getURIText();
        } else {
            return null;
        }
    }


    public @NotNull String fullName() {

        StringBuilder sb = new StringBuilder();

        if ( namespace != null ) {
            sb.append ( getNamespacePrefixText() );
            sb.append ( Constants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        sb.append ( getLocalNameText() );

        return sb.toString();
    }

    public @NotNull String toString() { return fullName(); }

/*
    private final @NotNull String localName;
    private final @Nullable ASTNamespace namespace;
    private final @Nullable TextLocation location;


    public ASTNodeName (
        @NotNull String localName,
        @Nullable ASTNamespace namespace,
        @Nullable TextLocation location ) {

        this.localName = localName;
        this.namespace = namespace;
        this.location = location;
    }


    public @NotNull String getLocalName() { return localName; }

    public @Nullable
    ASTNamespace getNamespace() { return namespace; }

    public @Nullable TextLocation getLocation() { return location; }


    public boolean hasNamespace() { return namespace != null; }

    public @Nullable String getNamespacePrefix() {

        if ( namespace != null ) {
            return namespace.getPrefixText();
        } else {
            return null;
        }
    }

    public @Nullable String getNamespaceURI() {

        if ( namespace != null ) {
            return namespace.getURIText();
        } else {
            return null;
        }
    }


    public @NotNull String fullName() {

        StringBuilder sb = new StringBuilder();

        if ( namespace != null ) {
            sb.append ( namespace.getPrefix() );
            sb.append ( Constants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        sb.append ( localName );

        return sb.toString();
    }

    public @NotNull String toString() { return fullName(); }
*/
}
