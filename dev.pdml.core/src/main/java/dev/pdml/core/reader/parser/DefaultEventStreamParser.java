package dev.pdml.core.reader.parser;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.name.ASTRawNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaceGetter;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.data.formalNode.FormalNode;
import dev.pdml.core.data.formalNode.FormalNodes;
import dev.pdml.core.data.formalNode.types.PDMLType;
import dev.pdml.core.data.node.name.NodeName;
import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.reader.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pdml.core.reader.parser.eventHandler.ParserEventsHandlerException;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pp.text.token.TextToken;

public class DefaultEventStreamParser<N, R> implements EventStreamParser<N, R> {


    private static final int DEFAULT_LOOKAHEAD = 500;


    private PXMLReader reader;

    // config
    private TextErrorHandler errorHandler;
    private ParserEventHandler<N, R> eventHandler;
    private @Nullable FormalNodes formalNodes;
    private ExtensionsContext extensionsContext;

    private ASTNamespaces currentNamespaces;
    private ASTNamespaceGetter namespaceGetter;


    public DefaultEventStreamParser() {}


    public void parse (
        @NotNull PXMLReader reader,
        @NotNull EventStreamParserConfig<N, R> config ) throws TextReaderException {

        this.reader = reader;

        this.errorHandler = config.getErrorHandler();
        this.eventHandler = config.getEventHandler();
        this.formalNodes = config.getFormalNodes();
        this.extensionsContext = new ExtensionsContext ( reader, errorHandler );

        this.currentNamespaces = new ASTNamespaces ();
        this.namespaceGetter = new ASTNamespaceGetter () {
            public @Nullable
            ASTNamespace getByPrefix ( @NotNull String prefix ) {
                return currentNamespaces.get ( prefix );
            }
        };

        // errorHandler.start();
        startParsing();
        // errorHandler.stop();

        /*
        TextError firstError = errorHandler.firstError();
        if ( firstError != null ) {
            MalformedPXMLDocumentException exception = new MalformedPXMLDocumentException (
                firstError.getId(), firstError.getMessage(), firstError.getLocation() );
            throw new StringReaderException ( "ABORTED", "The operation has been aborted due to errors", null, exception );
        }
        */
    }


    // private methods

    private void startParsing() throws TextReaderException {

        try {
            eventHandler.onStart();
        } catch ( Exception e ) {
            throw new ParserEventsHandlerException ( e, currentToken() );
        }

        requireRootNode();

        try {
            eventHandler.onEnd ();
        } catch ( Exception e ) {
            throw new ParserEventsHandlerException ( e, currentToken() );
        }
    }

    private void requireRootNode() throws TextReaderException {

        skipWhitespaceAndComments();
        requireNode ( null, true );
        skipWhitespaceAndComments();
        ParserHelper.requireDocumentEnd ( reader, errorHandler );
    }

    private void requireNode ( N handlerParentNode, boolean isRootNode ) throws TextReaderException {

        ParserHelper.requireNodeStart ( reader, errorHandler );

        ASTRawNodeName rawName = ParserHelper.requireRawNodeName ( reader, errorHandler );
        checkNameSeparator ( rawName );
        boolean isEmptyNode = isAtChar ( PDMLConstants.NODE_END );

        // TODO? also allow XML-like namespace declarations with attributes, e.g.  (xmlns:ch="http://www.example.com")

        ASTNamespaces declaredNamespaces = parseDeclaredNamespaces();

        ASTNodeName name = ParserHelper.rawNodeNameToASTNodeName ( rawName, namespaceGetter, errorHandler );

        @Nullable FormalNode formalNode = getFormalNode ( name );

        // TODO add formalNode;
        NodeStartEvent startEvent = new NodeStartEvent ( name, declaredNamespaces, isEmptyNode );
        N handlerNode;
        try {
            if ( isRootNode ) {
                handlerNode = eventHandler.onRootNodeStart ( startEvent );
            } else {
                handlerNode = eventHandler.onNodeStart ( startEvent, handlerParentNode );
            }
        } catch ( Exception e ) {
            throw new ParserEventsHandlerException ( e, currentToken() );
        }

/*
        if ( peekFirstCharAfterOptionalSpacesTabsAndNewLines ( DEFAULT_LOOKAHEAD ) == Constants.ATTRIBUTES_START ) {
            skipSpacesAndTabsAndNewLines (); // TODO? skipWhitespaceAndComments();
            parseAttributes ( handlerNode, formalNode );
        }
*/
        parseAttributes ( handlerNode, formalNode );

        if ( formalNode != null ) {
            PDMLType<?> type = formalNode.getType();
            if ( type != null ) {
                type.readValidateAndInsertPDMLObject ( reader, name );
            }
        }

        parseChildNodes ( handlerNode );

        requireNodeEnd ( name, isEmptyNode, isRootNode, handlerNode );

        removeNamespaces ( declaredNamespaces );
    }

    private void checkNameSeparator ( @NotNull ASTRawNodeName name ) throws PXMLResourceException {

        boolean hasNameValueSeparator =
            acceptChar ( PDMLConstants.NAME_VALUE_SEPARATOR )
            || acceptChar ( PDMLConstants.TAB )
            || isAtChar ( PDMLConstants.ATTRIBUTES_START )
            || isAtChar ( PDMLConstants.NODE_END )
            || isAtChar ( PDMLConstants.NODE_START )
            || isAtEndOfLine();

        if ( ! hasNameValueSeparator ) {
            nonCancelingErrorAtCurrentLocation (
                "ILLEGAL_CHARACTER",
                "Character '" + currentChar () + "' is not allowed in a name. If it is part of text of node '" + name + "' then there must be a whitespace character (e.g. a space) after the name." );
        }
    }

    private ASTNamespaces parseDeclaredNamespaces() throws PXMLResourceException, TextReaderException {

        if ( peekFirstCharAfterOptionalSpacesTabsAndNewLines ( DEFAULT_LOOKAHEAD )
            != PDMLConstants.NAMESPACE_DECLARATION_START_CHAR ) return null;

        skipSpacesAndTabsAndNewLines (); // TODO? skipWhitespaceAndComments();
        ASTNamespaces declaredNamespaces = ParserHelper.parseNamespaces ( reader, true, errorHandler );
        if ( declaredNamespaces != null ) addNewNamespaces ( declaredNamespaces );
        return declaredNamespaces;
    }

    private void addNewNamespaces ( @NotNull ASTNamespaces newNamespaces ) {

        for ( ASTNamespace newNamespace : newNamespaces.getList() ) {
            String prefix = newNamespace.getPrefixText();

            if ( ! currentNamespaces.containsPrefix ( prefix ) ) {
                currentNamespaces.addOrReplace ( newNamespace );

            } else {
                ASTNamespace existingNamespace = currentNamespaces.get ( prefix );
                assert existingNamespace != null;

                String message = "Namespace '" +  prefix + "' has already been declared";
                TextLocation location = existingNamespace.getLocation ();
                if ( location != null ) message = message + " at" +
                    PDMLConstants.NEW_LINE + location;
                message = message + ".";

                nonCancelingError (
                    "NAMESPACE_NOT_UNIQUE",
                    message,
                    newNamespace.getPrefix() );
            }
        }
    }

    private void removeNamespaces ( @Nullable ASTNamespaces namespaces ) {

        if ( namespaces == null ) return;

        for ( ASTNamespace namespace : namespaces.getList() ) {
            String prefix = namespace.getPrefixText();

            if ( currentNamespaces.containsPrefix ( prefix ) ) {
                currentNamespaces.remove ( namespace );
            }
        }
    }

    private @Nullable FormalNode getFormalNode ( ASTNodeName name ) {

        if ( formalNodes == null ) {
            return null;
        } else {
            return formalNodes.getOrNull ( NodeName.ASTNodeNameToNodeName ( name ) );
        }
    }

    private void parseAttributes ( N handlerNode, @Nullable FormalNode formalNode )
        throws TextReaderException, ParserEventsHandlerException {

        ASTNodeAttributes attributes = null;
        boolean lenientParsing = false;
        @Nullable NodeName defaultAttributeName = null;

        if ( formalNode != null ) {

            defaultAttributeName = formalNode.getDefaultAttributeName();

            if ( formalNode.hasOnlyAttributes() ) {
                lenientParsing = true;
                reader.skipWhitespaceAndComments();
                attributes = ParserHelper.parseAttributesWithOptionalParenthesis (
                    reader, defaultAttributeName, namespaceGetter, false, true, errorHandler );

            } else if ( formalNode.hasAllAttributesOnTagLine() ) {
                lenientParsing = true;
                reader.skipOptionalSpacesAndTabs();
                attributes = ParserHelper.parseAttributesWithOptionalParenthesis (
                    reader, defaultAttributeName, namespaceGetter, true, true, errorHandler );
            }
        }

        if ( ! lenientParsing ) {

            if ( peekFirstCharAfterOptionalSpacesTabsAndNewLines ( DEFAULT_LOOKAHEAD ) == PDMLConstants.ATTRIBUTES_START ) {
                skipSpacesAndTabsAndNewLines (); // TODO? skipWhitespaceAndComments();
                attributes = ParserHelper.parseAttributesWithParenthesis (
                    reader, defaultAttributeName, namespaceGetter, true, errorHandler );
            }
        }

        if ( attributes != null ) {
            try {
                eventHandler.onAttributes ( attributes, handlerNode );
            } catch ( Exception e ) {
                throw new ParserEventsHandlerException ( e, currentToken() );
            }
        }
    }

    private void parseChildNodes ( N handlerParentNode ) throws TextReaderException {

        while ( hasChar() ) {

            if ( reader.isAtNodeEnd() ) {
                return;
            } else if ( reader.isAtNodeStart() ) {
                requireNode ( handlerParentNode, false );
            } else if ( reader.isAtStartOfComment() ) {
                requireComment ( handlerParentNode );
            } else {
                requireText ( handlerParentNode );
            }
        }
    }

    private void requireText ( N handlerParentNode ) throws TextReaderException {

        TextToken text = ParserHelper.requireText ( reader, errorHandler );

        try {
            eventHandler.onText ( text, handlerParentNode );
        } catch ( Exception e ) {
            throw new ParserEventsHandlerException ( e, text );
        }
    }

    private void requireComment ( N handlerParentNode ) throws TextReaderException {

        TextToken comment = ParserHelper.requireComment ( reader, errorHandler );

        try {
            eventHandler.onComment ( comment, handlerParentNode );
        } catch ( Exception e ) {
            throw new ParserEventsHandlerException ( e, comment );
        }
    }

    private void requireNodeEnd (
        @NotNull ASTNodeName name,
        boolean isEmptyNode,
        boolean isRootNode,
        N handlerParentNode ) throws TextReaderException {

        TextLocation location = currentLocation();
        ParserHelper.requireNodeEnd ( reader, name, errorHandler );

        NodeEndEvent event = new NodeEndEvent ( name, isEmptyNode, location );
        try {
            if ( isRootNode ) {
                eventHandler.onRootNodeEnd ( event, handlerParentNode );
            } else {
                eventHandler.onNodeEnd ( event, handlerParentNode );
            }
        } catch ( Exception e ) {
            throw new ParserEventsHandlerException ( e, currentToken() );
        }
    }



    // reader wrappers

    private TextLocation currentLocation() {
        return reader.currentLocation();
    }

    private TextToken currentToken() {
        return reader.currentToken();
    }

    private boolean hasChar() {
        return reader.hasChar();
    }

    private char currentChar() {
        return reader.currentChar();
    }

    private boolean isAtChar ( char c ) {
        return reader.isAtChar ( c );
    }

    private boolean acceptChar ( char c ) throws PXMLResourceException {
        return reader.skipChar ( c );
    }

    private boolean isNextChar ( char c ) throws PXMLResourceException {
        return reader.isNextChar ( c );
    }

    private char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws PXMLResourceException {
        return reader.peekFirstCharAfterOptionalSpacesTabsAndNewLines ( lookAhead );
    }

    // private boolean isAtSpaceOrTabOrNewLine() { return reader.isAtSpaceOrTabOrNewLine (); }

    private void skipSpacesAndTabsAndNewLines () throws PXMLResourceException {
        reader.skipOptionalSpacesAndTabsAndNewLines ();
    }

    private void skipWhitespaceAndComments() throws PXMLResourceException, MalformedPXMLDocumentException {

        reader.skipWhitespaceAndComments();
    }

    private boolean isAtEndOfLine() {
        return isAtChar ( '\n' ) || isAtChar ( '\r' );
    }

/*
    private void skipSpacesAndTabs() throws IOException {
        reader.skipSpacesAndTabs();
    }
*/


    // errors

    private void nonCancelingErrorAtCurrentLocation ( @NotNull String id, @NotNull String message ) {

        ParserHelper.nonCancelingErrorAtCurrentLocation ( id, message, reader, errorHandler );
    }

    private void nonCancelingError ( @NotNull String id, @NotNull String message, @NotNull TextToken token ) {

        ParserHelper.nonCancelingError ( id, message, token, errorHandler );
    }

    /*
    private MalformedPXMLDocumentException cancelingErrorAtCurrentLocation (
        @NotNull String id, @NotNull String message ) {

        return ParserHelper.cancelingErrorAtCurrentLocation ( id, message, reader );
    }

    private MalformedPXMLDocumentException cancelingError (
        @NotNull String id, @NotNull String message, @NotNull TextToken token ) {

        return ParserHelper.cancelingError ( id, message, token );
    }
    */



/*
    private URI requireNamespaceURI ( String namespacePrefix, TextLocation location )
        throws PXMLResourceException, MalformedPXMLDocumentException {

        String URIString = XMLUtilities.getOfficialNamespaceURI ( namespacePrefix );

        if ( URIString == null ) {
            URIString = namespacePrefixURIMap.get ( namespacePrefix );
        }

        if ( URIString == null ) {
            // hack to find the namespace URI in the upcoming code
            // (remove when only new syntax for namespaces is allowed)
            String nextCode = reader.peekCurrentNChars ( 500 );
            // Pattern nodePattern = Pattern.compile ( "\\[#?xmlns:" + namespacePrefix + "\\s+([^\\]]+)\\s*\\]" );
            // Pattern attributePattern = Pattern.compile ( "xmlns:" + namespacePrefix + "\\s*=\\s*([^ )]+)" );
            Pattern attributePattern = Pattern.compile ( XMLConstants.XMLNS_ATTRIBUTE + ":" + namespacePrefix + "\\s*=\\s*([^ )]+)" );
            Matcher matcher = attributePattern.matcher ( nextCode );
            if ( matcher.find() ) {
                URIString = matcher.group ( 1 );
                if ( URIString.charAt ( 0 ) == Constants.ATTRIBUTE_VALUE_DOUBLE_QUOTE ||
                    URIString.charAt ( 0 ) == Constants.ATTRIBUTE_VALUE_SINGLE_QUOTE ) {
                    // remove quotes
                    URIString = URIString.substring ( 1, URIString.length() - 1 );
                }
            } else {
                nonCancelingErrorAtLocation (
                "INVALID_NAMESPACE_PREFIX",
            "The namespace declaration for the namespace prefix '" + namespacePrefix + "' could not be found.",
                    location );
            }
        }

        // System.out.println ( "URIString: [" + URIString + "] " + location.toString() );
        try {
            return new URI( URIString );
        } catch ( URISyntaxException e ) {
            throw cancelingErrorAtLocation ("INVALID_URI", e.getMessage(), location );
        }
    }
*/

}
