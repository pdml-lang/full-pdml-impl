package dev.pdml.ext.extensions.node.types;

import dev.pdml.core.data.formalNode.types.standard.TextBlockType;
import dev.pp.text.annotations.NotNull;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;

import dev.pdml.ext.extensions.node.PXMLExtensionHandler;

public class RawTextBlockType_ExtensionHandler implements PXMLExtensionHandler {

    public static final @NotNull String NODE_NAME = "text-block";

    public void handleNode ( @NotNull ExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws TextReaderException {

        TextBlockType type = new TextBlockType ();
        TypeExtensionHandlerHelper.handleTypeNode ( context, type, nodeName );
    }
}
