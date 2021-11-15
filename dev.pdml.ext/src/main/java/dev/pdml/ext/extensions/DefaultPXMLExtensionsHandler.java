package dev.pdml.ext.extensions;

import dev.pdml.core.Constants;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.ParserHelper;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pdml.core.reader.reader.extensions.PXMLExtensionsHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.ext.extensions.node.PXMLExtensionHandler;
import dev.pdml.ext.extensions.node.standard.StandardExtensionsConstants;
import dev.pdml.ext.extensions.node.standard.StandardExtensionsHandlers;

import java.util.Map;

public class DefaultPXMLExtensionsHandler implements PXMLExtensionsHandler {


    private final @NotNull Map<String, PXMLExtensionHandler> standardExtensionNodeHandlers;
    private final @Nullable Map<String, PXMLExtensionHandler> customizedExtensionNodeHandlers;


    public DefaultPXMLExtensionsHandler(
        @NotNull Map<String, PXMLExtensionHandler> standardExtensionNodeHandlers,
        @Nullable Map<String, PXMLExtensionHandler> customizedExtensionNodeHandlers ) {

        this.standardExtensionNodeHandlers = standardExtensionNodeHandlers;
        this.customizedExtensionNodeHandlers = customizedExtensionNodeHandlers;

/*
        Map<String, PXMLExtensionHandler> customizedHandlers = new HashMap<>();
        customizedHandlers.put ( "code", new RawTextBlock_ExtensionHandler() );
        customizedHandlers.put ( "html", new RawTextBlock_ExtensionHandler() );
        this.customizedExtensionNodeHandlers = customizedHandlers;
*/
    }

    public DefaultPXMLExtensionsHandler(
        @Nullable Map<String, PXMLExtensionHandler> customizedExtensionNodeHandlers ) {

        this ( StandardExtensionsHandlers.map, customizedExtensionNodeHandlers );
    }

    public DefaultPXMLExtensionsHandler() {
        this ( StandardExtensionsHandlers.map, null );
    }


    @Override
    public boolean handleExtension ( @NotNull ExtensionsContext context ) throws TextReaderException {

        PXMLReader reader = context.getPXMLReader();

        if ( ! reader.isAtChar ( Constants.NODE_START ) ) return false;

        if ( reader.isNextChar ( StandardExtensionsConstants.START_SYMBOL ) ) {
            handleStandardExtension ( context );
            return true;
        } else {
            if ( ! reader.isNextChar ( Constants.COMMENT_SYMBOL ) ) {
                return handleCustomizedExtension ( context );
            } else {
                return false;
            }
        }
    }


    private void handleStandardExtension ( @NotNull ExtensionsContext context ) throws TextReaderException {

        PXMLReader reader = context.getPXMLReader();
        reader.advanceChar(); // consume [
        reader.advanceChar(); // consume !

        TextLocation location = reader.currentLocation ();
        // String nodeName = ParserHelper.requireSingleName ( reader );
        ASTNodeName nodeName = ParserHelper.requireNodeName ( reader, null, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name
        // TODO? check no namespace
        PXMLExtensionHandler handler = standardExtensionNodeHandlers.get ( nodeName.getLocalNameText() );
        if ( handler == null ) {
            throw ParserHelper.cancelingError (
                "INVALID_EXTENSION_NAME",
                "There is no extension with name '" + nodeName + "'.",
                nodeName.getToken(),
                context.getErrorHandler() );
        }
        handler.handleNode ( context, nodeName );
    }

    private boolean handleCustomizedExtension ( @NotNull ExtensionsContext context ) throws TextReaderException {

        if ( customizedExtensionNodeHandlers == null ) return false;

        PXMLReader reader = context.getPXMLReader();
        reader.setMark ( 100 );
        reader.advanceChar (); // consume [

        TextLocation location = reader.currentLocation ();
        // String nodeName = ParserHelper.requireSingleName ( reader );
        ASTNodeName nodeName = ParserHelper.requireNodeName ( reader, null, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name
        // TODO? check no namespace
        PXMLExtensionHandler handler = customizedExtensionNodeHandlers.get ( nodeName.getLocalNameText() );
        if ( handler == null ) {
            reader.goBackToMark();
            return false;
        }
        handler.handleNode ( context, nodeName );
        return true;
    }
/*
    private void handleExtension (
        @Nullable Map<String, PXMLExtensionHandler> map,
        boolean isStandardExtensions,
        @NotNull ExtensionsContext context ) throws PXMLReaderException {

        if ( map == null ) return;

        for ( Map.Entry<String, PXMLExtensionHandler> entry : map.entrySet() ) {

            final String nodeName = entry.getKey();

            String token = String.valueOf ( Constants.NODE_START );
            if ( isStandardExtensions ) token = token + StandardExtensionsConstants.START_SYMBOL;
            token = token + nodeName + " ";

            if ( context.getPXMLReader().acceptString ( token ) ) {
                entry.getValue().handleNode ( context );
                return;
            }
        }
    }
*/
}
