package dev.pp.pdml.ext.types;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

import java.io.IOException;

public class TypeNodeHandler<T> implements ExtensionNodeHandler {


    public static final @NotNull String EXTENSION_KIND = "t";


    private final @NotNull PdmlType<T> type;
    public @NotNull PdmlType<T> getType() { return type; }


    public TypeNodeHandler ( @NotNull PdmlType<T> type ) {
        this.type = type;
    }


    @Override
    public @NotNull String getExtensionKind() {
        return EXTENSION_KIND;
    }

    public @NotNull String getExtensionName() {
        return type.getName();
    }

    @Override
    public @Nullable InsertStringExtensionResult handleNode (
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws IOException, PdmlException {

        type.parseValidateAndHandleObject (
            context.getPdmlParser(), null, true );
        return null;
    }
}
