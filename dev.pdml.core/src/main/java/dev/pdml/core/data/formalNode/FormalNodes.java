package dev.pdml.core.data.formalNode;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.core.data.node.name.NodeName;

import java.util.HashMap;
import java.util.Map;

public class FormalNodes {

    private Map<NodeName, FormalNode> map;


    public FormalNodes() {

        this.map = new HashMap<> ();
    }


    public void add ( @NotNull FormalNode formalNode ) {

        map.put ( formalNode.getName(), formalNode );
    }

    public @Nullable FormalNode getOrNull ( NodeName name ) {

        return map.get ( name );
    }
}
