package dev.pdml.extshared;

import dev.pdml.data.node.NodeName;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.text.inspection.TextErrorException;

import java.io.IOException;

public interface PdmlExtensionNodeHandler {

    @NotNull NodeName getNodeName();

    default @NotNull String getQualifiedNodeName() { return getNodeName().qualifiedName(); };

    default @NotNull String getLocalNodeName() { return getNodeName().localName(); }

    // when this method is called, the node name and a space after the node name are consumed already by the textReader
    void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException;
}
