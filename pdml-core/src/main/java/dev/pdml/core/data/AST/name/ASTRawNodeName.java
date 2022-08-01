package dev.pdml.core.data.AST.name;

import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
// import dev.pdml.core.data.AST.ASTElement;
import dev.pp.text.token.TextToken;

public class ASTRawNodeName { // implements ASTElement {

    private final @NotNull TextToken localName;
    private final @Nullable TextToken namespacePrefix;


    public ASTRawNodeName (
        @NotNull TextToken localName,
        @Nullable TextToken namespacePrefix ) {

        this.localName = localName;
        this.namespacePrefix = namespacePrefix;
    }


    public @NotNull TextToken getLocalName() { return localName; }

    public @Nullable TextToken getNamespacePrefix() { return namespacePrefix; }


    public @Nullable TextLocation getLocation() {
        return namespacePrefix != null ? namespacePrefix.getLocation() : localName.getLocation(); }

    public @NotNull String getLocalNameText() { return localName.getText(); }

    public @Nullable String getNamespacePrefixText() {
        return namespacePrefix != null ? namespacePrefix.getText() : null; }

    public @NotNull String fullName() {

        StringBuilder sb = new StringBuilder();

        if ( namespacePrefix != null ) {
            sb.append ( getNamespacePrefixText() );
            sb.append ( ":" );
        }

        sb.append ( getLocalNameText() );

        return sb.toString();
    }

    public @NotNull String toString() { return fullName(); }
/*
    private final @NotNull String localName;
    private final @Nullable String namespacePrefix;
    private final @Nullable TextLocation location;


    public ASTRawNodeName (
        @NotNull String localName,
        @Nullable String namespacePrefix,
        @Nullable TextLocation location ) {

        this.localName = localName;
        this.namespacePrefix = namespacePrefix;
        this.location = location;
    }


    public @NotNull String getLocalName() { return localName; }

    public @Nullable String getNamespacePrefix() { return namespacePrefix; }

    public @Nullable TextLocation getLocation() { return location; }


    public @NotNull String fullName() {

        StringBuilder sb = new StringBuilder();

        if ( namespacePrefix != null ) {
            sb.append ( namespacePrefix );
            sb.append ( ":" );
        }

        sb.append ( localName );

        return sb.toString();
    }

    public @NotNull String toString() { return fullName(); }
*/
}
