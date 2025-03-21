package dev.pp.pdml.utils.treewalker.handler;

import dev.pp.pdml.data.attribute.NodeAttributes;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.namespace.NodeNamespaces;
import dev.pp.pdml.data.nodespec.PdmlNodeSpec;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public record TaggedNodeStartEvent(
    @Nullable TextLocation startLocation,
    @NotNull NodeTag tag,
    @Nullable NodeNamespaces declaredNamespaces,
    @Nullable NodeAttributes attributes,
    boolean isEmptyNode,
    @Nullable PdmlNodeSpec nodeSpec ) {


    public @NotNull TextToken tagToken() { return tag.qualifiedTagToken(); }

    @Override
    public String toString() { return tag + " start"; }
}
