package dev.pdml.ext.extensions.node.standard;

import dev.pp.text.annotations.NotNull;
import dev.pdml.ext.extensions.node.PXMLExtensionHandler;
import dev.pdml.ext.extensions.node.types.RawTextBlockType_ExtensionHandler;
import dev.pdml.ext.extensions.node.types.StringType_ExtensionHandler;

import java.util.Map;

import static java.util.Map.entry;

public class StandardExtensionsHandlers {

    private StandardExtensionsHandlers() {}

    public static final @NotNull Map<String, PXMLExtensionHandler> map = Map.ofEntries (
        entry ( ConstantDeclaration_ExtensionHandler.NODE_NAME, new ConstantDeclaration_ExtensionHandler() ),
        entry ( InsertConstant_ExtensionHandler.NODE_NAME, new InsertConstant_ExtensionHandler() ),
        entry ( InsertFile_ExtensionHandler.NODE_NAME, new InsertFile_ExtensionHandler() ),
        entry ( StringType_ExtensionHandler.NODE_NAME, new StringType_ExtensionHandler() ),
        entry ( RawTextBlockType_ExtensionHandler.NODE_NAME, new RawTextBlockType_ExtensionHandler() ) );
}
