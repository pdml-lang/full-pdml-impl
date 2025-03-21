package dev.pp.pdml.companion.commands.scripting;

import dev.pp.pdml.data.node.tagged.TaggedNode;
// import dev.pp.core.annotations.basics.NotNull;
// import dev.pp.core.annotations.basics.Nullable;

@Deprecated
public interface PdmlTreeTransformer {

    // @Nullable TaggedNode transform ( @NotNull TaggedNode node ) throws Exception;
    TaggedNode transform ( TaggedNode node ) throws Exception;
}
