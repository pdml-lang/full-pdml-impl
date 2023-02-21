package dev.pdml.parser.nodespec;

import dev.pdml.data.node.NodeName;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PdmlNodeSpecs<N> {

    private final Map<String, PdmlNodeSpec<N>> map;


    public PdmlNodeSpecs () {
        this.map = new HashMap<> ();
    }


    public boolean containsQualifiedName ( @NotNull String qualifiedName ) { return map.containsKey ( qualifiedName ); }


    public void add ( @NotNull PdmlNodeSpec<N> nodeSpec ) {
        checkNotExists ( nodeSpec );
        map.put ( nodeSpec.qualifiedName(), nodeSpec );
    }

    public @Nullable PdmlNodeSpec<N> getOrNull ( NodeName name ) {

        return map.get ( name.qualifiedName() );
    }

    private void checkExists ( @NotNull PdmlNodeSpec<N> nodeSpec ) {

        String name = nodeSpec.qualifiedName();
        if ( ! containsQualifiedName ( name ) )
            throw new IllegalArgumentException ( "Node spec. '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull PdmlNodeSpec<N> nodeSpec ) {

        String name = nodeSpec.qualifiedName();
        if ( containsQualifiedName ( name ) )
            throw new IllegalArgumentException ( "Node spec. '" + name + "' exists already." );
    }
}
