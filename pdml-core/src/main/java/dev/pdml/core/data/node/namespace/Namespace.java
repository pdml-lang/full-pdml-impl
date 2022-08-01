package dev.pdml.core.data.node.namespace;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.namespace.ASTNamespace;

import java.net.URI;

public class Namespace {

    private final @NotNull String prefix; // @Nullable?
    private final @NotNull URI URI;


    public Namespace ( @NotNull String prefix, @NotNull URI URI ) {

        this.prefix = prefix;
        this.URI = URI;
    }

    public Namespace ( @NotNull ASTNamespace ASTNamespace ) {

        this ( ASTNamespace.getPrefixText (), ASTNamespace.getURI() );
    }


    public @NotNull String getPrefix() { return prefix; }

    public @NotNull URI getURI() { return URI; }


//    public boolean isDefaultNamespace() { return prefix.equals ( DEFAULT_NAMESPACE_PREFIX ); }

    public final boolean equals ( @Nullable Object otherObject ) {

        if ( otherObject == this ) { return true; }

        if ( ! ( otherObject instanceof Namespace ) ) { return false; }

        Namespace otherNamespace = (Namespace) otherObject;

        return URI.equals ( otherNamespace.getURI() );
    }

    public final int hashCode() {
        return URI.hashCode();
    }

    public @NotNull String toString() { return prefix + PDMLConstants.ATTRIBUTE_ASSIGN + URI; }
}
