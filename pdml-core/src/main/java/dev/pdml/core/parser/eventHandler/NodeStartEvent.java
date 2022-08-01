package dev.pdml.core.parser.eventHandler;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class NodeStartEvent {


    private final @NotNull ASTNodeName name;
    public @NotNull ASTNodeName getName() { return name; }

    private final @Nullable ASTNamespaces declaredNamespaces;
    public @Nullable ASTNamespaces getDeclaredNamespaces() { return declaredNamespaces; }

    private final boolean isEmptyNode;
    public boolean isEmptyNode() { return isEmptyNode; }


    public NodeStartEvent ( @NotNull ASTNodeName name, @Nullable ASTNamespaces declaredNamespaces, boolean isEmptyNode ) {

        this.name = name;
        this.declaredNamespaces = declaredNamespaces;
        this.isEmptyNode = isEmptyNode;
    }


    public @NotNull TextToken getToken() { return name.getToken(); }

    @Override
    public String toString() { return name.toString(); }
}
