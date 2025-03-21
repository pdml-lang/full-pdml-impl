package dev.pp.pdml.data.validation;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.nodespec.PdmlNodeSpec;
import dev.pp.core.basics.annotations.NotNull;

public interface TaggedNodeValidator {

    boolean validate (
        @NotNull PdmlNodeSpec nodeSpec,
        @NotNull TaggedNode taggedNode,
        @NotNull TaggedNodeValidatorContext context );
}
