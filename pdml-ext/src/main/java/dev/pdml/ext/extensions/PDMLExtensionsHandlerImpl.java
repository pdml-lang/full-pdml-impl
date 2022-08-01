package dev.pdml.ext.extensions;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.core.reader.extensions.PDMLExtensionsHandler;
import dev.pp.basics.utilities.DebugUtils;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pp.text.error.TextErrorException;

import java.io.IOException;
import java.util.Map;

public class PDMLExtensionsHandlerImpl implements PDMLExtensionsHandler {


    private final @NotNull Map<String, PDMLExtensionNodeHandler> standardExtensionNodeHandlers;
    private final @Nullable Map<String, PDMLExtensionNodeHandler> customizedExtensionNodeHandlers;


    public PDMLExtensionsHandlerImpl (
        @NotNull Map<String, PDMLExtensionNodeHandler> standardExtensionNodeHandlers,
        @Nullable Map<String, PDMLExtensionNodeHandler> customizedExtensionNodeHandlers ) {

        this.standardExtensionNodeHandlers = standardExtensionNodeHandlers;
        this.customizedExtensionNodeHandlers = customizedExtensionNodeHandlers;

/*
        Map<String, PXMLExtensionHandler> customizedHandlers = new HashMap<>();
        customizedHandlers.put ( "code", new RawTextBlock_ExtensionHandler() );
        customizedHandlers.put ( "html", new RawTextBlock_ExtensionHandler() );
        this.customizedExtensionNodeHandlers = customizedHandlers;
*/
    }

    public PDMLExtensionsHandlerImpl (
        @Nullable Map<String, PDMLExtensionNodeHandler> customizedExtensionNodeHandlers ) {

        this ( StandardExtensionsHandlers.MAP, customizedExtensionNodeHandlers );
    }

    public PDMLExtensionsHandlerImpl () {
        this ( StandardExtensionsHandlers.MAP, null );
    }

/*
    public boolean handleExtension ( @NotNull PDMLExtensionsContext context )
        throws IOException, InvalidTextException {

        PDMLReader reader = context.getPXMLReader();

        if ( ! reader.isAtChar ( PDMLConstants.NODE_START ) ) return false;

        if ( reader.isNextChar ( StandardExtensionsConstants.START_SYMBOL ) ) {
            handleStandardExtension ( context );
            return true;
        } else {
            if ( ! reader.isNextChar ( PDMLConstants.COMMENT_SYMBOL ) ) {
                return handleCustomizedExtension ( context );
            } else {
                return false;
            }
        }
    }
*/

    public boolean handleExtension ( @NotNull PDMLExtensionsContext context )
        throws IOException, TextErrorException {

        PDMLReader reader = context.getPDMLReader ();

        if ( ! reader.isAtChar ( PDMLConstants.NODE_START ) ) return false;

        @Nullable String next2Chars = reader.peekNextMaxNChars ( 2 );
        if ( next2Chars == null ) return false;

        char firstChar = next2Chars.charAt ( 0 );
        if ( firstChar == PDMLConstants.COMMENT_SYMBOL ) return false;

        char secondChar = next2Chars.length () > 1 ? next2Chars.charAt ( 1 ) : 0;

        if ( secondChar == PDMLConstants.NAMESPACE_PREFIX_NAME_SEPARATOR ) {
            // it's a namespace prefix composed of one character
            handleStandardExtension ( context );
            return true;
        } else if ( firstChar == StandardExtensionsConstants.START_SYMBOL_DEPRECATED ) {
            handleStandardExtensionDeprecated ( context );
            return true;
        } else {
            return handleCustomizedExtension ( context );
        }
    }


    private void handleStandardExtension ( @NotNull PDMLExtensionsContext context )
        throws IOException, TextErrorException {

        PDMLReader reader = context.getPDMLReader ();
        reader.advanceChar(); // consume [

        ASTNodeName nodeName = PDMLParserHelper.requireNodeName (
            reader, ExtensionsNamespaces.EXTENSION_NAMESPACE_GETTER, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name

        PDMLExtensionNodeHandler handler = standardExtensionNodeHandlers.get ( nodeName.qualifiedName() );
        if ( handler == null ) {
            throw PDMLParserHelper.abortingSyntaxError (
                "INVALID_EXTENSION_NAME",
                "There is no extension with name '" + nodeName + "'.",
                nodeName.getToken(), context.getErrorHandler() );
        }
        handler.handleNode ( context, nodeName );
    }

    @Deprecated
    private void handleStandardExtensionDeprecated ( @NotNull PDMLExtensionsContext context )
        throws IOException, TextErrorException {

        PDMLReader reader = context.getPDMLReader ();
        reader.advanceChar(); // consume [
        reader.advanceChar(); // consume !

        // TextLocation location = reader.currentLocation ();
        // String nodeName = ParserHelper.requireSingleName ( reader );
        ASTNodeName nodeName = PDMLParserHelper.requireNodeName ( reader, null, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name
        // TODO? check no namespace
        PDMLExtensionNodeHandler handler = standardExtensionNodeHandlers.get ( nodeName.getLocalNameText() );
        if ( handler == null ) {
            throw PDMLParserHelper.abortingSyntaxError (
                "INVALID_EXTENSION_NAME",
                "There is no extension with name '" + nodeName + "'.",
                nodeName.getToken(), context.getErrorHandler() );
        }
        handler.handleNode ( context, nodeName );
    }

    private boolean handleCustomizedExtension ( @NotNull PDMLExtensionsContext context )
        throws IOException, TextErrorException {

        if ( customizedExtensionNodeHandlers == null ) return false;

        PDMLReader reader = context.getPDMLReader ();
        reader.setMark ( 100 );
        reader.advanceChar (); // consume [

        TextLocation location = reader.currentLocation ();
        // String nodeName = ParserHelper.requireSingleName ( reader );
        ASTNodeName nodeName = PDMLParserHelper.requireNodeName ( reader, null, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name
        // TODO? check no namespace
        PDMLExtensionNodeHandler handler = customizedExtensionNodeHandlers.get ( nodeName.getLocalNameText() );
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
