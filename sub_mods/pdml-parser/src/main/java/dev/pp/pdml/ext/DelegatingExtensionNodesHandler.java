package dev.pp.pdml.ext;

import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.types.PdmlTypes;
import dev.pp.pdml.ext.utils.UtilityHandlerConstants;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.scriptingbase.env.ScriptingEnvironment;

import java.io.IOException;

public class DelegatingExtensionNodesHandler implements ExtensionNodesHandler {


    private final @NotNull ExtensionNodeHandlers delegates;

    private final @Nullable ScriptingEnvironment scriptingEnvironment;

    private @Nullable ExtensionNodeHandlerContext cachedContext;
    private @NotNull ExtensionNodeHandlerContext requireContext (
        @NotNull PdmlReader pdmlReader,
        @NotNull PdmlParser pdmlParser ) {

        if ( cachedContext == null ) {
            cachedContext = new ExtensionNodeHandlerContext (
                pdmlReader, pdmlParser, scriptingEnvironment );
        }
        return cachedContext;
    }


    public DelegatingExtensionNodesHandler (
        @NotNull ExtensionNodeHandlers delegates,
        @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.delegates = delegates;
        this.scriptingEnvironment = scriptingEnvironment;
    }


    @Override
    // public void handleExtensionNode (
    public @Nullable InsertStringExtensionResult handleExtensionNode (
        @NotNull PdmlReader pdmlReader,
        @NotNull PdmlParser pdmlParser ) throws IOException, PdmlException {

        assert pdmlReader.readExtensionStart();

        String extensionKind = pdmlReader.readExtensionKindLetters();
        if ( extensionKind == null ) {
            extensionKind = UtilityHandlerConstants.EXTENSION_KIND;
        }

        if ( ! pdmlReader.readNodeStart() ) {
            throw new MalformedPdmlException (
                "Expecting '" + CorePdmlConstants.NODE_START_CHAR + "'.",
                "EXTENSION_NODE_START_REQUIRED",
                pdmlReader.currentToken() );
        }

        return handleExtension ( pdmlReader, pdmlParser, extensionKind );
    }

    // private void handleExtension (
    private @Nullable InsertStringExtensionResult handleExtension (
        @NotNull PdmlReader pdmlReader,
        @NotNull PdmlParser pdmlParser,
        @NotNull String extensionKind ) throws IOException, PdmlException {

        NodeTag nodeName = pdmlParser.requireTag ();
        pdmlParser.requireSeparator();

        String identifier = ExtensionNodeHandler.createIdentifier (
            extensionKind, nodeName.toString() );
        ExtensionNodeHandler handler = delegates.getOrNull ( identifier );
        if ( handler == null ) {
            throw new PdmlException (
                "Extension node '" + identifier + "' doesn't exist.",
                "INVALID_EXTENSION_NAME",
                nodeName.qualifiedTagToken () );
        }

        ExtensionNodeHandlerContext context = requireContext ( pdmlReader, pdmlParser );
        return handler.handleNode ( context, nodeName );
    }

    public @Nullable PdmlTypes getTypes() {
        return delegates.getTypes();
    }
}
