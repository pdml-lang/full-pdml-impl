package dev.pp.pdml.ext;

import dev.pp.pdml.data.PdmlExtensionsConstants;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

import java.io.IOException;

public interface ExtensionNodeHandler {


    static @NotNull String createIdentifier (
        @NotNull String extensionKind,
        @NotNull String extensionName ) {

        return extensionKind +
            PdmlExtensionsConstants.NAMESPACE_SEPARATOR_CHAR +
            extensionName;
    }


    @NotNull String getExtensionKind();

    @NotNull String getExtensionName();

    default @NotNull String getIdentifier() {
        return createIdentifier ( getExtensionKind(), getExtensionName () );
    }

    // When this method is called, the node tag and a tag/value separator are consumed already
    // (e.g. ^s[exp 1+1] -> the reader is on "1+1"
    // void handleNode ( @NotNull ExtensionNodeHandlerContext context, @NotNull NodeName nodeName )
    //    throws IOException, PdmlException;

    // returns text to be inserted
    // When this method is called, the node tag and a tag/value separator are consumed already
    // (e.g. ^s[exp 1+1] -> the reader is on "1+1"
    @Nullable InsertStringExtensionResult handleNode ( @NotNull ExtensionNodeHandlerContext context, @NotNull NodeTag nodeName )
        throws IOException, PdmlException;
}
