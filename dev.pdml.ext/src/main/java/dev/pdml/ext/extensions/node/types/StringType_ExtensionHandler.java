package dev.pdml.ext.extensions.node.types;

import dev.pdml.core.data.formalNode.types.standard.StringType;
import dev.pp.text.annotations.NotNull;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;

import dev.pdml.ext.extensions.node.PXMLExtensionHandler;

public class StringType_ExtensionHandler implements PXMLExtensionHandler {

    public static final @NotNull String NODE_NAME = "string";

    public void handleNode ( @NotNull ExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws TextReaderException {

        // TODO read parameters

        StringType type = new StringType();
        TypeExtensionHandlerHelper.handleTypeNode ( context, type, nodeName );
    }
}
