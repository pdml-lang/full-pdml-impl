package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.types.PdmlType;
import dev.pp.pdml.ext.types.instances.TrimmedTextOrStringLiteralType;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.scriptingbase.env.ScriptingException;

public class ScriptingHandlerUtil {

    public static final @NotNull String EXTENSION_KIND = "s";

    protected static final @NotNull PdmlType<String> CODE_TYPE = TrimmedTextOrStringLiteralType.NULLABLE_INSTANCE;

    protected static void scriptingError (
        @NotNull String message,
        @NotNull String id,
        @NotNull NodeTag nodeName,
        @NotNull ScriptingException scriptingException,
        @NotNull ExtensionNodeHandlerContext context ) throws PdmlException {

        // TODO explore 'scrptingException' to provide a better error message and precise error position
        throw context.error ( message, id, nodeName.qualifiedTagToken () );
    }
}
