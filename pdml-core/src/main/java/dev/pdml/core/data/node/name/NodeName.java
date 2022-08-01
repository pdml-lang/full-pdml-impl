package dev.pdml.core.data.node.name;

import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.token.TextToken;

public class NodeName {

    public static @NotNull NodeName ASTNodeNameToNodeName ( @NotNull ASTNodeName ASTNodeName ) {

        if ( ! ASTNodeName.hasNamespace() ) {
            return new NodeName ( ASTNodeName );
        } else {
            return new QualifiedNodeName ( ASTNodeName );
        }
    }

    public static @NotNull ASTNodeName NodeNameToASTNodeName ( @NotNull NodeName nodeName ) {

        @Nullable ASTNamespace namespace = null;
        if ( nodeName instanceof QualifiedNodeName ) {
            namespace = ASTNamespace.namespaceToASTNamespace ( ((QualifiedNodeName) nodeName).getNamespace() );
        }

        return new ASTNodeName ( new TextToken ( nodeName.getLocalName(), null ), null, namespace );
    }


    final @NotNull String localName;


    public NodeName ( @NotNull String localName ) {

        this.localName = localName;
    }

    public NodeName ( @NotNull ASTNodeName ASTNodeName ) {

        this ( ASTNodeName.getLocalNameText() );
    }


    public @NotNull String getLocalName() { return localName; }


    public boolean equals ( @Nullable Object otherObject ) {

        if ( otherObject == this ) { return true; }

        if ( ! ( otherObject instanceof NodeName ) ) { return false; }

        NodeName otherName = (NodeName) otherObject;

        return localName.equals ( otherName.getLocalName() );
    }

    public int hashCode() {
        return localName.hashCode();
    }

    public @NotNull String toString() { return localName; }
}
