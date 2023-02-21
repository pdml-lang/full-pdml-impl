package dev.pdml.parser.eventhandler;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.node.NodeName;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.parser.nodespec.PdmlNodeSpec;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public record NodeStartEvent (
    @NotNull NodeName name,
    @Nullable MutableNodeNamespaces declaredNamespaces,
    @Nullable MutableNodeAttributes attributes,
    boolean isEmptyNode,
    @Nullable PdmlNodeSpec<?> nodeSpec ) {

    public @NotNull TextToken nameToken() { return name.localNameToken (); }

    @Override
    public String toString() { return name.toString(); }
}
