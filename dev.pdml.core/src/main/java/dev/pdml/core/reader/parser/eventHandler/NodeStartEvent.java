package dev.pdml.core.reader.parser.eventHandler;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

public class NodeStartEvent {

    private final @NotNull
    ASTNodeName name;
    private final @Nullable
    ASTNamespaces declaredNamespaces;
    private final boolean isEmptyNode;


    public NodeStartEvent ( @NotNull ASTNodeName name, @Nullable ASTNamespaces declaredNamespaces, boolean isEmptyNode ) {

        this.name = name;
        this.declaredNamespaces = declaredNamespaces;
        this.isEmptyNode = isEmptyNode;
    }


    public @NotNull
    ASTNodeName getName() { return name; }

    public @Nullable
    ASTNamespaces getDeclaredNamespaces() { return declaredNamespaces; }

    public boolean isEmptyNode() { return isEmptyNode; }
}
