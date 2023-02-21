package dev.pdml.ext;

import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pdml.reader.extensions.PdmlExtensionsHandler;
import dev.pdml.shared.constants.CorePdmlConstants;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.location.TextLocation;

import java.io.IOException;
import java.util.Map;

public class PdmlExtensionsHandlerImpl implements PdmlExtensionsHandler {


    private final @NotNull Map<String, PdmlExtensionNodeHandler> standardExtensionNodeHandlers;
    private final @Nullable Map<String, PdmlExtensionNodeHandler> customizedExtensionNodeHandlers;


    public PdmlExtensionsHandlerImpl (
        @NotNull Map<String, PdmlExtensionNodeHandler> standardExtensionNodeHandlers,
        @Nullable Map<String, PdmlExtensionNodeHandler> customizedExtensionNodeHandlers ) {

        this.standardExtensionNodeHandlers = standardExtensionNodeHandlers;
        this.customizedExtensionNodeHandlers = customizedExtensionNodeHandlers;

/*
        Map<String, PXMLExtensionHandler> customizedHandlers = new HashMap<>();
        customizedHandlers.put ( "code", new RawTextBlock_ExtensionHandler() );
        customizedHandlers.put ( "html", new RawTextBlock_ExtensionHandler() );
        this.customizedExtensionNodeHandlers = customizedHandlers;
*/
    }

    public PdmlExtensionsHandlerImpl (
        @Nullable Map<String, PdmlExtensionNodeHandler> customizedExtensionNodeHandlers ) {

        this ( StandardExtensionsHandlers.MAP, customizedExtensionNodeHandlers );
    }

    public PdmlExtensionsHandlerImpl () {
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

    public boolean handleExtension ( @NotNull PdmlExtensionsContext context )
        throws IOException, TextErrorException {

        PdmlReader reader = context.getPdmlReader();

        if ( ! reader.isAtChar ( CorePdmlConstants.NODE_START ) ) return false;

        @Nullable String next2Chars = reader.peekNextMaxNChars ( 2 );
        if ( next2Chars == null ) return false;

        char firstChar = next2Chars.charAt ( 0 );
        if ( firstChar == PdmlExtensionsConstants.COMMENT_SYMBOL ) return false;

        char secondChar = next2Chars.length () > 1 ? next2Chars.charAt ( 1 ) : 0;

        if ( secondChar == PdmlExtensionsConstants.NAMESPACE_PREFIX_NAME_SEPARATOR ) {
            // it's a namespace prefix composed of one character
            handleStandardExtension ( context );
            return true;
        } else {
            return handleCustomizedExtension ( context );
        }
    }


    private void handleStandardExtension ( @NotNull PdmlExtensionsContext context )
        throws IOException, TextErrorException {

        PdmlReader reader = context.getPdmlReader();
        reader.advanceChar(); // consume [

        NodeName nodeName = PdmlParserHelper.requireNodeName ( reader, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name

        PdmlExtensionNodeHandler handler = standardExtensionNodeHandlers.get ( nodeName.qualifiedName() );
        if ( handler == null ) {
            throw PdmlParserHelper.abortingSyntaxError (
                "There is no extension with name '" + nodeName + "'.",
                "INVALID_EXTENSION_NAME",
                nodeName.localNameToken(), context.getErrorHandler() );
        }
        handler.handleNode ( context, nodeName );
    }

    private boolean handleCustomizedExtension ( @NotNull PdmlExtensionsContext context )
        throws IOException, TextErrorException {

        if ( customizedExtensionNodeHandlers == null ) return false;

        PdmlReader reader = context.getPdmlReader();
        reader.setMark ( 100 );
        reader.advanceChar (); // consume [

        TextLocation location = reader.currentLocation ();
        // String nodeName = ParserHelper.requireSingleName ( reader );
        NodeName nodeName = PdmlParserHelper.requireNodeName ( reader, context.getErrorHandler() );
        reader.skipOneSpaceOrTab(); // skip space after node name
        // TODO? check no namespace
        PdmlExtensionNodeHandler handler = customizedExtensionNodeHandlers.get ( nodeName.localName() );
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
