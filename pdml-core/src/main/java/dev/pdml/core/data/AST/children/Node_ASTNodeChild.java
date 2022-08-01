package dev.pdml.core.data.AST.children;

import dev.pdml.core.data.AST.PDMLNodeAST;
import dev.pp.basics.annotations.NotNull;

public class Node_ASTNodeChild extends ASTNodeChild {

    private final @NotNull PDMLNodeAST node;


    public Node_ASTNodeChild ( @NotNull PDMLNodeAST node ) {

        this.node = node;
    }


    @NotNull public PDMLNodeAST getNode () { return node; }


    public @NotNull String toString() { return node.toString(); }
}
