package dev.pp.pdml.data.attribute;

import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameter.Parameter;
import dev.pp.core.parameters.parameters.MutableParameters;
import dev.pp.core.text.token.TextToken;

import java.util.List;

public class NodeAttributes extends MutableParameters<String> {

    public NodeAttributes ( @Nullable TextToken startToken ) {
        super ( startToken );
    }

    public @Nullable List<NodeAttribute> attributesList() {

        List<Parameter<String>> list = list();
        if ( list == null ) return null;
        return list.stream().map ( p -> (NodeAttribute) p ).toList ();
    }
}
