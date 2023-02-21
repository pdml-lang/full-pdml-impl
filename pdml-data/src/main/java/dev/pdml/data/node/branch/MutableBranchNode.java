package dev.pdml.data.node.branch;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.node.NodeName;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MutableBranchNode extends MutableRootOrBranchNode implements MutableChildNode {


    private @Nullable MutableRootOrBranchNode parent;

    public @Nullable MutableRootOrBranchNode getParent() { return parent; }
    public void setParent ( @Nullable MutableRootOrBranchNode parent ) { this.parent = parent; }


    public MutableBranchNode (
        @NotNull NodeName name,
        @Nullable MutableNodeAttributes attributes,
        @Nullable MutableNodeNamespaces namespaceDefinitions ) {

        super ( name, attributes, namespaceDefinitions);
    }

    public MutableBranchNode ( @NotNull NodeName name ) {
        super ( name );
    }

    public MutableBranchNode (
        @NotNull String name ) {

        super ( name );
    }


    public @NotNull List<NodeName> pathNames() {

        assert parent != null;

        List<NodeName> names = new ArrayList<> ( parent.pathNames() );
        names.add ( name );
        return names;
    }
}
