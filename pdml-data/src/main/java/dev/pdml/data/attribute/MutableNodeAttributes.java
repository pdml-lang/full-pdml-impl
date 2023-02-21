package dev.pdml.data.attribute;

import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameters.MutableParameters;
import dev.pp.text.token.TextToken;

public class MutableNodeAttributes extends MutableParameters<String> {

    public MutableNodeAttributes ( @Nullable TextToken startToken ) {
        super ( startToken );
    }
}
