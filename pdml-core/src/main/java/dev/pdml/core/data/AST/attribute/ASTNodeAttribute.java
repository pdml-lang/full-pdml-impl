package dev.pdml.core.data.AST.attribute;

import dev.pdml.core.PDMLConstants;
import dev.pp.parameters.textTokenParameter.TextTokenParameter;
import dev.pp.text.location.TextLocation;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class ASTNodeAttribute { // implements ASTElement {


    private final @NotNull ASTNodeName name;
    private final @NotNull TextToken value;


    public ASTNodeAttribute (
        @NotNull ASTNodeName name,
        @NotNull TextToken value ) {

        this.name = name;
        this.value = value;
    }


    public @NotNull ASTNodeName getName() { return name; }

    public @NotNull TextToken getValue() { return value; }


    public @Nullable TextLocation getLocation() { return name.getLocation(); }

    public @NotNull TextToken getLocalName() { return name.getLocalName(); }

    public @NotNull String getLocalNameText() { return name.getLocalName().getText(); }

    public boolean hasNamespace() { return getNamespace() != null; }

    public @Nullable ASTNamespace getNamespace() { return name.getNamespace(); }

    public @Nullable TextToken getNamespacePrefix() {

        ASTNamespace namespace = getNamespace();
        if ( namespace != null ) {
            return namespace.getPrefix();
        } else {
            return null;
        }
    }

    public @Nullable String getNamespacePrefixText() {

        ASTNamespace namespace = getNamespace();
        if ( namespace != null ) {
            return namespace.getPrefixText ();
        } else {
            return null;
        }
    }

    public @Nullable TextToken getNamespaceURI() {

        ASTNamespace namespace = getNamespace();
        if ( namespace != null ) {
            return namespace.getURIToken ();
        } else {
            return null;
        }
    }

    public @Nullable String getNamespaceURIText() {

        ASTNamespace namespace = getNamespace();
        if ( namespace != null ) {
            return namespace.getURIText();
        } else {
            return null;
        }
    }

    public @NotNull String getValueText() { return value.getText(); }


    public @NotNull TextTokenParameter toTextParameter() {

        return new TextTokenParameter (
            new TextToken ( name.qualifiedName(), name.getLocation() ),
            value );
    }


    public @NotNull String toString() {

        return name.qualifiedName () + " " + PDMLConstants.ATTRIBUTE_ASSIGN + " " + getValueText();
    }

/*
    private final @NotNull ASTNodeName name;
    private final @NotNull ASTNodeText value;


    public ASTNodeAttribute (
        @NotNull ASTNodeName name,
        @NotNull ASTNodeText value ) {

        this.name = name;
        this.value = value;
    }


    public @NotNull ASTNodeName getName() { return name; }

    public @NotNull ASTNodeText getValue() { return value; }

    public @Nullable TextLocation getLocation() { return name.getLocation(); }


    public @NotNull String getLocalName() { return name.getLocalName (); }

    public boolean hasNamespace() { return getNamespace() != null; }

    public @Nullable ASTNamespace getNamespace() { return name.getNamespace(); }

    public @Nullable String getNamespacePrefix() {

        ASTNamespace namespace = getNamespace();
        if ( namespace != null ) {
            return namespace.getPrefix();
        } else {
            return null;
        }
    }

    public @Nullable URI getNamespaceURI() {

        ASTNamespace namespace = getNamespace();
        if ( namespace != null ) {
            return namespace.getURI();
        } else {
            return null;
        }
    }

    public @NotNull String getValueText() { return value.getText(); }


    public @NotNull TextParameter toTextParameter() {

        return new TextParameter (
            new TextToken ( name.fullName(), name.getLocation() ),
            new TextToken ( value.getText(), value.getLocation() ) );
    }


    public @NotNull String toString() {

        return name.toString() + " = " + value.toString();
    }
*/
}
