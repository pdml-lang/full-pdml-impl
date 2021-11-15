package dev.pdml.core.data.AST.children;

import dev.pdml.core.data.AST.ASTNode;
import dev.pp.text.annotations.NotNull;

public class Node_ASTNodeChild extends ASTNodeChild {

    private final @NotNull ASTNode node;


    public Node_ASTNodeChild ( @NotNull ASTNode node ) {

        this.node = node;
    }


    @NotNull public ASTNode getNode () { return node; }


    public @NotNull String toString() { return node.toString(); }
}
