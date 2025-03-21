package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

import java.io.IOException;

public class InsertCodeHandler implements ExtensionNodeHandler {

    public static final @NotNull String NAME = "icode";
    public static final InsertCodeHandler INSTANCE = new InsertCodeHandler ();


    private InsertCodeHandler() {}


    @Override
    public @NotNull String getExtensionKind() {
        return ScriptingHandlerUtil.EXTENSION_KIND;
    }

    public @NotNull String getExtensionName() {
        return NAME;
    }

    @Override
    public @Nullable InsertStringExtensionResult handleNode (
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws IOException, PdmlException {

        return InsertTextHandler.handleNode ( context, nodeName, false );
    }
}
