package dev.pdml.data.node.root;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pdml.data.node.NodeName;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.List;

public class MutableRootNode extends MutableRootOrBranchNode {

    public MutableRootNode (
        @NotNull NodeName name,
        @Nullable MutableNodeAttributes attributes,
        @Nullable MutableNodeNamespaces namespaceDefinitions ) {

        super ( name, attributes, namespaceDefinitions);
    }

    public MutableRootNode ( @NotNull NodeName name ) {
        super ( name );
    }

    public MutableRootNode ( @NotNull String name ) {
        super ( name );
    }

    public @NotNull List<NodeName> pathNames() { return List.of ( name ); }
}
