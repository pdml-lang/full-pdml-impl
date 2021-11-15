package dev.pdml.core.reader.parser.eventHandler;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

public class NodeEndEvent {

    private final @NotNull
    ASTNodeName name;
    // private final @Nullable Namespaces declaredNamespaces;
    private final boolean isEmptyNode;
    private final @Nullable TextLocation location;


    public NodeEndEvent ( @NotNull ASTNodeName name, boolean isEmptyNode, @Nullable TextLocation location ) {

        this.name = name;
        this.isEmptyNode = isEmptyNode;
        this.location = location;
    }


    public @NotNull
    ASTNodeName getName() { return name; }

    public boolean isEmptyNode() { return isEmptyNode; }

    public @Nullable TextLocation getLocation() { return location; }
}
