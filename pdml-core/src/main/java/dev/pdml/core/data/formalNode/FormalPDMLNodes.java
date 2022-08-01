package dev.pdml.core.data.formalNode;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pdml.core.data.node.name.NodeName;

import java.util.HashMap;
import java.util.Map;

public class FormalPDMLNodes<N> {

    private final Map<NodeName, FormalPDMLNode<N>> map;


    public FormalPDMLNodes () {

        this.map = new HashMap<> ();
    }


    public void add ( @NotNull FormalPDMLNode<N> formalNode ) {

        map.put ( formalNode.getName(), formalNode );
    }

    public @Nullable FormalPDMLNode<N> getOrNull ( NodeName name ) {

        return map.get ( name );
    }
}
