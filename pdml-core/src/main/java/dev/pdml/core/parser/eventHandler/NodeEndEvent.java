package dev.pdml.core.parser.eventHandler;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

public class NodeEndEvent {

    private final @NotNull ASTNodeName name;
    public @NotNull ASTNodeName getName() { return name; }

    // private final @Nullable Namespaces declaredNamespaces;

    private final boolean isEmptyNode;
    public boolean isEmptyNode() { return isEmptyNode; }

    private final @Nullable TextLocation location;
    public @Nullable TextLocation getLocation() { return location; }

    // TODO? private final @NotNUll NodeStartEvent startEvent;


    public NodeEndEvent ( @NotNull ASTNodeName name, boolean isEmptyNode, @Nullable TextLocation location ) {

        this.name = name;
        this.isEmptyNode = isEmptyNode;
        this.location = location;
    }
}
