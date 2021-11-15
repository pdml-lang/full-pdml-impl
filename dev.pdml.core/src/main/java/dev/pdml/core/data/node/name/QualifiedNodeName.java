package dev.pdml.core.data.node.name;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import dev.pdml.core.Constants;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.node.namespace.Namespace;

import java.net.URI;
import java.net.URISyntaxException;

public class QualifiedNodeName extends NodeName {

    private final @NotNull Namespace namespace;


    public QualifiedNodeName ( @NotNull String localName, @NotNull Namespace namespace ) {

        super ( localName );
        this.namespace = namespace;
    }

    public QualifiedNodeName ( @NotNull ASTNodeName ASTNodeName ) {

        super ( ASTNodeName.getLocalNameText() );

        ASTNamespace ASTNamespace = ASTNodeName.getNamespace();
        assert ASTNamespace != null;
        this.namespace = new Namespace ( ASTNamespace );
    }


    public @NotNull String getLocalName() { return localName; }

    public @NotNull Namespace getNamespace() { return namespace; }


    public @NotNull String getNamespacePrefix() { return namespace.getPrefix(); }

    public @NotNull URI getNamespaceURI() { return namespace.getURI(); }


    public final boolean equals ( @Nullable Object otherObject ) {

        if ( otherObject == this ) { return true; }

        if ( ! ( otherObject instanceof QualifiedNodeName ) ) { return false; }

        QualifiedNodeName otherName = (QualifiedNodeName) otherObject;

        return localName.equals ( otherName.getLocalName() )
            && namespace.equals ( otherName.getNamespace() );
    }

    public final int hashCode() {
        return namespace.hashCode() ^ localName.hashCode();
    }

    public @NotNull String toString() {

        return namespace.getPrefix () +
            Constants.NAMESPACE_PREFIX_NAME_SEPARATOR +
            localName;
    }
}
