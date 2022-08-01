package dev.pdml.core.parser;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.name.ASTRawNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaceGetter;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.data.formalNode.FormalPDMLNode;
import dev.pdml.core.data.formalNode.FormalPDMLNodes;
import dev.pdml.core.data.formalNode.PDMLType;
import dev.pdml.core.data.node.name.NodeName;
import dev.pdml.core.exception.PDMLDocumentSyntaxException;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pdml.core.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.core.parser.eventHandler.PDMLParserEventsHandlerException;
import dev.pdml.core.reader.PDMLReader;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PDMLParser<N, R> {


    private static final int DEFAULT_LOOKAHEAD = 500;


    private @NotNull PDMLReader reader;
    // public @NotNull PDMLReader getPDMLReader() { return reader; }

    // config
    private @NotNull TextErrorHandler errorHandler;
    private @NotNull PDMLParserEventHandler<N, R> eventHandler;
    /* TODO?
    private @NotNull PDMLParserEventHandler<N, R> eventHandler;
    public void changeEventHandler ( @NotNull PDMLParserEventHandler<N, R> eventHandler ) {
        this.eventHandler = eventHandler;
    }
    */
    private @Nullable FormalPDMLNodes<?> formalNodes;
    private boolean ignoreTextAfterEndOfRootNode;

    private ASTNamespaces currentNamespaces;
    private ASTNamespaceGetter namespaceGetter;


    public PDMLParser() {}


    public void parsePDMLReader ( @NotNull PDMLReader PDMLReader, @NotNull PDMLParserOptions<N, R> options )
        throws IOException, TextErrorException {

        this.reader = PDMLReader;

        this.errorHandler = options.errorHandler();
        this.eventHandler = options.eventHandler();
        this.formalNodes = options.formalNodes();
        this.ignoreTextAfterEndOfRootNode = options.ignoreTextAfterEndOfRootNode();

        this.currentNamespaces = new ASTNamespaces();
        this.namespaceGetter = currentNamespaces::get;

        parse();
    }


    private void parse()
        throws IOException, TextErrorException {

        try {
            eventHandler.onStart();
        } catch ( Exception e ) {
            handleEventHandlerException ( e, currentToken() );
        }

        requireRootNode();

        try {
            eventHandler.onEnd ();
        } catch ( Exception e ) {
            handleEventHandlerException ( e, currentToken() );
        }
    }

    /*
    public @NotNull CharReader getCurrentCharReader() {
        return reader.currentCharReader();
    }
    */



    // private methods

    private void requireRootNode()
        throws IOException, TextErrorException {

        skipWhitespaceAndComments ();
        requireNode ( null, true );
        if ( ! ignoreTextAfterEndOfRootNode ) {
            skipWhitespaceAndComments ();
            PDMLParserHelper.requireDocumentEnd ( reader, errorHandler );
        }
    }

    private void requireNode ( N handlerParentNode, boolean isRootNode )
        throws IOException, TextErrorException {

        PDMLParserHelper.requireNodeStart ( reader, errorHandler );

        ASTRawNodeName rawName = PDMLParserHelper.requireRawNodeName ( reader, errorHandler );
        checkNameSeparator ( rawName );
        boolean isEmptyNode = isAtChar ( PDMLConstants.NODE_END );

        // TODO? also allow XML-like namespace declarations with attributes, e.g.  (xmlns:ch="http://www.example.com")

        ASTNamespaces declaredNamespaces = parseDeclaredNamespaces();

        ASTNodeName name = PDMLParserHelper.rawNodeNameToASTNodeName ( rawName, namespaceGetter, errorHandler );

        @Nullable FormalPDMLNode<?> formalNode = getFormalNode ( name );

        // TODO add formalNode;
        NodeStartEvent startEvent = new NodeStartEvent ( name, declaredNamespaces, isEmptyNode );
        N handlerNode = null;
        try {
            if ( isRootNode ) {
                handlerNode = eventHandler.onRootNodeStart ( startEvent );
            } else {
                handlerNode = eventHandler.onNodeStart ( startEvent, handlerParentNode );
            }
        } catch ( Exception e ) {
            handleEventHandlerException ( e, currentToken() );
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

    private void checkNameSeparator ( @NotNull ASTRawNodeName name ) throws IOException {

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

    private ASTNamespaces parseDeclaredNamespaces() throws IOException, TextErrorException {

        if ( peekFirstCharAfterOptionalSpacesTabsAndNewLines ( DEFAULT_LOOKAHEAD )
            != PDMLConstants.NAMESPACE_DECLARATION_START_CHAR ) return null;

        skipSpacesAndTabsAndNewLines (); // TODO? skipWhitespaceAndComments();
        ASTNamespaces declaredNamespaces = PDMLParserHelper.parseNamespaces ( reader, true, errorHandler );
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
                    StringConstants.OS_NEW_LINE + location;
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

    private @Nullable FormalPDMLNode<?> getFormalNode ( ASTNodeName name ) {

        if ( formalNodes == null ) {
            return null;
        } else {
            return formalNodes.getOrNull ( NodeName.ASTNodeNameToNodeName ( name ) );
        }
    }

    private void parseAttributes ( N handlerNode, @Nullable FormalPDMLNode<?> formalNode )
        throws IOException, TextErrorException {

        ASTNodeAttributes attributes = null;
        boolean lenientParsing = false;
        @Nullable NodeName defaultAttributeName = null;

        if ( formalNode != null ) {

            // defaultAttributeName = formalNode.getDefaultAttributeName();
            @Nullable String defaultAttributeNameString = formalNode.getFirstPositionalAttributeNameOrNull ();
            // TODO should consider namespace and possibly create a QualifiedNodeName
            defaultAttributeName = defaultAttributeNameString != null ? new NodeName ( defaultAttributeNameString ) : null;

            if ( formalNode.hasOnlyAttributes() ) {
                lenientParsing = true;
                reader.skipWhitespaceAndComments();
                attributes = PDMLParserHelper.parseAttributesWithOptionalParenthesis (
                    reader, defaultAttributeName, namespaceGetter, false, true, errorHandler );

            } else if ( formalNode.hasAllAttributesOnTagLine() ) {
                lenientParsing = true;
                reader.skipOptionalSpacesAndTabs();
                attributes = PDMLParserHelper.parseAttributesWithOptionalParenthesis (
                    reader, defaultAttributeName, namespaceGetter, true, true, errorHandler );
            }
        }

        if ( ! lenientParsing ) {

            if ( peekFirstCharAfterOptionalSpacesTabsAndNewLines ( DEFAULT_LOOKAHEAD ) == PDMLConstants.ATTRIBUTES_START ) {
                skipSpacesAndTabsAndNewLines (); // TODO? skipWhitespaceAndComments();
                attributes = PDMLParserHelper.parseAttributesWithParenthesis (
                    reader, defaultAttributeName, namespaceGetter, true, errorHandler );
            }
        }

        // if ( attributes != null ) {
            try {
                eventHandler.onAttributes ( attributes, handlerNode );
            } catch ( Exception e ) {
                handleEventHandlerException ( e, currentToken() );
            }
        // }
    }

    private void parseChildNodes ( N handlerParentNode )
        throws IOException, TextErrorException {

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

    private void requireText ( N handlerParentNode ) throws IOException, TextErrorException {

        TextToken text = PDMLParserHelper.requireText ( reader, errorHandler );

        try {
            eventHandler.onText ( text, handlerParentNode );
        } catch ( Exception e ) {
            handleEventHandlerException ( e, text );
        }
    }

    private void requireComment ( N handlerParentNode ) throws IOException, TextErrorException {

        TextToken comment = PDMLParserHelper.requireComment ( reader, errorHandler );

        try {
            eventHandler.onComment ( comment, handlerParentNode );
        } catch ( Exception e ) {
            handleEventHandlerException ( e, comment );
        }
    }

    private void requireNodeEnd (
        @NotNull ASTNodeName name,
        boolean isEmptyNode,
        boolean isRootNode,
        N handlerParentNode ) throws IOException, TextErrorException {

        TextLocation location = currentLocation();
        PDMLParserHelper.requireNodeEnd ( reader, name, errorHandler );

        NodeEndEvent event = new NodeEndEvent ( name, isEmptyNode, location );
        try {
            if ( isRootNode ) {
                eventHandler.onRootNodeEnd ( event, handlerParentNode );
            } else {
                eventHandler.onNodeEnd ( event, handlerParentNode );
            }
        } catch ( Exception e ) {
            handleEventHandlerException ( e, currentToken() );
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

    private boolean acceptChar ( char c ) throws IOException {
        return reader.skipChar ( c );
    }

    /*
    private boolean isNextChar ( char c ) throws IOException {
        return reader.isNextChar ( c );
    }
    */

    private char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws IOException {
        return reader.peekFirstCharAfterOptionalSpacesTabsAndNewLines ( lookAhead );
    }

    // private boolean isAtSpaceOrTabOrNewLine() { return reader.isAtSpaceOrTabOrNewLine (); }

    private void skipSpacesAndTabsAndNewLines () throws IOException {
        reader.skipOptionalSpacesAndTabsAndNewLines ();
    }

    private void skipWhitespaceAndComments() throws IOException, PDMLDocumentSyntaxException {

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

    private void handleEventHandlerException (
        @NotNull Exception eventHandlerException,
        @Nullable TextToken texttoken ) throws IOException, TextErrorException {

        if ( eventHandlerException instanceof TextErrorException textErrorException ) {
            throw textErrorException;
        } else if ( eventHandlerException instanceof IOException ioException ) {
            throw ioException;
        } else {
            throw new PDMLParserEventsHandlerException ( eventHandlerException, texttoken );
        }
    }

    private void nonCancelingErrorAtCurrentLocation ( @NotNull String id, @NotNull String message ) {

        PDMLParserHelper.nonAbortingErrorAtCurrentLocation ( id, message, reader, errorHandler );
    }

    private void nonCancelingError ( @NotNull String id, @NotNull String message, @NotNull TextToken token ) {

        PDMLParserHelper.nonAbortingError ( id, message, token, errorHandler );
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
