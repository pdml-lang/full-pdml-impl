package dev.pdml.core.data.AST.name;

import dev.pdml.core.PDMLConstants;
// import dev.pdml.core.data.AST.ASTElement;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class ASTNodeName { // implements ASTElement {


    private final @NotNull TextToken localName;
    public @NotNull TextToken getLocalName() { return localName; }

    private final @Nullable TextToken namespacePrefix;
    public @Nullable TextToken getNamespacePrefix() { return namespacePrefix; }

    private final @Nullable ASTNamespace namespace;
    public @Nullable ASTNamespace getNamespace() { return namespace; }


    public ASTNodeName (
        @NotNull TextToken localName,
        @Nullable TextToken namespacePrefix,
        @Nullable ASTNamespace namespace ) {

        this.localName = localName;
        this.namespacePrefix = namespacePrefix;
        this.namespace = namespace;
    }

    public ASTNodeName (
        @NotNull TextToken localName ) {

        this ( localName, null, null );
    }


    public @NotNull TextToken getToken() {
        return namespacePrefix != null ? namespacePrefix : localName; }

    public @Nullable TextLocation getLocation () {
        return namespacePrefix != null ? namespacePrefix.getLocation () : localName.getLocation(); }

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


    public @NotNull String qualifiedName() {

        StringBuilder sb = new StringBuilder();

        if ( namespace != null ) {
            sb.append ( getNamespacePrefixText() );
            sb.append ( PDMLConstants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        sb.append ( getLocalNameText() );

        return sb.toString();
    }

    public @NotNull String toString() { return qualifiedName(); }

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
