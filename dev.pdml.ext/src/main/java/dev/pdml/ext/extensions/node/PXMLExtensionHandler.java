package dev.pdml.ext.extensions.node;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pp.text.annotations.NotNull;

public interface PXMLExtensionHandler {

    // when this method is called, the node name and a space after the node name are consumed already by the textReader
    void handleNode ( @NotNull ExtensionsContext context, @NotNull ASTNodeName nodeName ) throws TextReaderException;
}
