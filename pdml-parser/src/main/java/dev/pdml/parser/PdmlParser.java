package dev.pdml.parser;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.node.NodeName;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.parser.eventhandler.*;
import dev.pdml.parser.nodespec.PdmlNodeSpec;
import dev.pdml.parser.nodespec.PdmlNodeSpecs;
import dev.pdml.parser.nodespec.PdmlType;
import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.constants.CorePdmlConstants;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.util.Collection;

public class PdmlParser<N, R> {


    private static final int DEFAULT_LOOKAHEAD = 500;


    private @NotNull PdmlReader reader;

    // config
    private @NotNull TextInspectionMessageHandler errorHandler;
    private @NotNull PdmlParserEventHandler<N, R> eventHandler;
    private @Nullable PdmlNodeSpecs<?> pdmlNodeSpecs;
    private boolean ignoreTextAfterEndOfRootNode;
    private boolean allowStandardAttributesStartSyntax;
    private boolean allowAlternativeAttributesStartSyntax;

    private MutableNodeNamespaces currentNamespaces;


    public PdmlParser() {}


    // TODO public boolean parsePDMLReader (
    // return false if an error occurred
    public void parsePDMLReader (
        @NotNull PdmlReader PdmlReader,
        @NotNull PdmlParserOptions<N, R> options ) throws IOException, TextErrorException {

        this.reader = PdmlReader;

        this.errorHandler = options.errorHandler();
        this.eventHandler = options.eventHandler();
        this.pdmlNodeSpecs = options.nodeSpecs ();
        this.ignoreTextAfterEndOfRootNode = options.ignoreTextAfterEndOfRootNode();
        this.allowStandardAttributesStartSyntax = options.allowStandardAttributesStartSyntax();
        this.allowAlternativeAttributesStartSyntax = options.allowAlternativeAttributesStartSyntax();

        this.currentNamespaces = new MutableNodeNamespaces();

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
            eventHandler.onEnd();
        } catch ( Exception e ) {
            handleEventHandlerException ( e, currentToken() );
        }
    }


    // Private methods

    private void requireRootNode()
        throws IOException, TextErrorException {

        skipWhitespaceAndComments ();
        requireNode ( null, true );
        if ( ! ignoreTextAfterEndOfRootNode ) {
            skipWhitespaceAndComments ();
            PdmlParserHelper.requireDocumentEnd ( reader, errorHandler );
        }
    }

    private void requireNode ( N handlerParentNode, boolean isRootNode )
        throws IOException, TextErrorException {

        PdmlParserHelper.requireNodeStart ( reader, errorHandler );

        NodeName name = PdmlParserHelper.requireNodeName ( reader, errorHandler );
        checkNameSeparator ( name );
        boolean isEmptyNode = isAtChar ( CorePdmlConstants.NODE_END );

        @Nullable PdmlNodeSpec<?> nodeSpec = getNodeSpec ( name );

        MutableNodeNamespaces declaredNamespaces = null;
        MutableNodeAttributes attributes = null;
        if ( ! isEmptyNode ) {
            declaredNamespaces = parseDeclaredNamespaces();
            attributes = parseAttributes ( nodeSpec );
        }

        NodeStartEvent startEvent = new NodeStartEvent ( name, declaredNamespaces, attributes, isEmptyNode, nodeSpec );
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

        if ( nodeSpec != null ) {
            PdmlType<?> type = nodeSpec.getType();
            if ( type != null ) {
                type.readValidateAndInsertPDMLObject ( reader, name );
            }
        }

        parseChildNodes ( startEvent, handlerNode );

        requireNodeEnd ( isRootNode, startEvent, handlerNode );

        removeNamespaces ( declaredNamespaces );
    }

    private void checkNameSeparator ( @NotNull NodeName name ) throws IOException {

        boolean hasNameValueSeparator =
            acceptChar ( CorePdmlConstants.NAME_VALUE_SEPARATOR )
            || acceptChar ( PdmlExtensionsConstants.TAB )
            || isAtChar ( CorePdmlConstants.NODE_END )
            || isAtChar ( CorePdmlConstants.NODE_START )
            || (allowAlternativeAttributesStartSyntax && isAtChar ( PdmlExtensionsConstants.ALTERNATIVE_ATTRIBUTES_START ))
            || isAtEndOfLine();

        if ( ! hasNameValueSeparator ) {
            nonCancelingErrorAtCurrentLocation (
                "Character '" + currentChar () + "' is not allowed in a name. If it is part of text of node '" + name + "' then there must be a whitespace character (e.g. a space) after the name.",
                "ILLEGAL_CHARACTER" );
        }
    }

    private MutableNodeNamespaces parseDeclaredNamespaces() throws IOException, TextErrorException {

        if ( peekFirstCharAfterOptionalSpacesTabsAndNewLines ( DEFAULT_LOOKAHEAD )
            != PdmlExtensionsConstants.NAMESPACE_DECLARATION_START_CHAR ) return null;

        skipSpacesAndTabsAndNewLines (); // TODO? skipWhitespaceAndComments();
        MutableNodeNamespaces declaredNamespaces = PdmlParserHelper.parseNamespaces ( reader, true, errorHandler );
        if ( declaredNamespaces != null ) addNewNamespaces ( declaredNamespaces );
        return declaredNamespaces;
    }

    private void addNewNamespaces ( @NotNull MutableNodeNamespaces newNamespaces ) {

        Collection<NodeNamespace> list = newNamespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace newNamespace : list ) {
            String prefix = newNamespace.namePrefix ();

            if ( ! currentNamespaces.containsNamespace ( newNamespace ) ) {
                currentNamespaces.add ( newNamespace );

            } else {
                NodeNamespace existingNamespace = currentNamespaces.getByPrefix ( prefix );

                String message = "Namespace '" +  prefix + "' has already been declared";
                TextLocation location = existingNamespace.namePrefixLocation ();
                if ( location != null ) message = message + " at" +
                    StringConstants.OS_NEW_LINE + location;
                message = message + ".";

                nonCancelingError (
                    message,
                    "NAMESPACE_NOT_UNIQUE",
                    newNamespace.URIToken() );
            }
        }
    }

    private void removeNamespaces ( @Nullable MutableNodeNamespaces namespaces ) {

        Collection<NodeNamespace> list = namespaces == null ? null : namespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace namespace : list ) {
            if ( currentNamespaces.containsNamespace ( namespace ) ) {
                currentNamespaces.remove ( namespace );
            }
        }
    }

    private @Nullable PdmlNodeSpec<?> getNodeSpec ( NodeName name ) {

        if ( pdmlNodeSpecs == null ) {
            return null;
        } else {
            return pdmlNodeSpecs.getOrNull ( name );
        }
    }

    private MutableNodeAttributes parseAttributes ( @Nullable PdmlNodeSpec<?> nodeSpec )
        throws IOException, TextErrorException {

        if ( nodeSpec != null ) {

            if ( nodeSpec.hasOnlyAttributes() ) {
                reader.skipWhitespaceAndComments();
                return PdmlParserHelper.parseAttributesWithOptionalParenthesis (
                    reader, false, true,
                    allowStandardAttributesStartSyntax, allowAlternativeAttributesStartSyntax, errorHandler );

            } else if ( nodeSpec.hasAllAttributesOnTagLine() ) {
                reader.skipOptionalSpacesAndTabs();
                return PdmlParserHelper.parseAttributesWithOptionalParenthesis (
                    reader, true, true,
                    allowStandardAttributesStartSyntax, allowAlternativeAttributesStartSyntax, errorHandler );
            }
        }

        reader.setMark ( DEFAULT_LOOKAHEAD );
        reader.skipOptionalSpacesAndTabsAndNewLines();
        TextToken attributesStartToken = reader.currentToken();

        @Nullable Character attributesEndSymbol = PdmlParserHelper.readAttributesStart (
            reader, allowStandardAttributesStartSyntax, allowAlternativeAttributesStartSyntax );

        if ( attributesEndSymbol == null ) { // there are no attributes
            reader.goBackToMark(); // keep whitespace to parse it as text
            return null;
        } else {
            return PdmlParserHelper.parseAttributesUntilEndSymbol (
                reader, attributesEndSymbol, attributesStartToken, true, errorHandler );
        }
    }

    private void parseChildNodes ( @NotNull NodeStartEvent startEvent, N handlerParentNode )
        throws IOException, TextErrorException {

        while ( hasChar() ) {

            if ( reader.isAtNodeEnd() ) {
                return;
            } else if ( reader.isAtNodeStart() ) {
                requireNode ( handlerParentNode, false );
            } else if ( reader.isAtStartOfComment() ) {
                requireComment ( startEvent, handlerParentNode );
            } else {
                requireText ( startEvent, handlerParentNode );
            }
        }
    }

    private void requireText ( @NotNull NodeStartEvent startEvent, N handlerParentNode ) throws IOException, TextErrorException {

        TextToken text = PdmlParserHelper.requireText ( reader, errorHandler );

        try {
            eventHandler.onText (
                new NodeTextEvent ( text.getText(), text.getLocation(), startEvent ),
                handlerParentNode );
        } catch ( Exception e ) {
            handleEventHandlerException ( e, text );
        }
    }

    private void requireComment ( @NotNull NodeStartEvent startEvent, N handlerParentNode ) throws IOException, TextErrorException {

        TextToken comment = PdmlParserHelper.requireComment ( reader, errorHandler );

        try {
            eventHandler.onComment (
                new NodeCommentEvent ( comment.getText(), comment.getLocation(), startEvent ),
                handlerParentNode );
        } catch ( Exception e ) {
            handleEventHandlerException ( e, comment );
        }
    }

    private void requireNodeEnd (
        boolean isRootNode,
        @NotNull NodeStartEvent startEvent,
        N handlerParentNode ) throws IOException, TextErrorException {

        TextLocation location = currentLocation();
        PdmlParserHelper.requireNodeEnd (
            reader, startEvent.name(), errorHandler );

        NodeEndEvent event = new NodeEndEvent ( location, startEvent );
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



    // Reader wrappers

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

    private char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws IOException {
        return reader.peekFirstCharAfterOptionalSpacesTabsAndNewLines ( lookAhead );
    }

    private void skipSpacesAndTabsAndNewLines () throws IOException {
        reader.skipOptionalSpacesAndTabsAndNewLines ();
    }

    private void skipWhitespaceAndComments() throws IOException, PdmlDocumentSyntaxException {

        reader.skipWhitespaceAndComments();
    }

    private boolean isAtEndOfLine() {
        return isAtChar ( '\n' ) || isAtChar ( '\r' );
    }


    // Errors

    private void handleEventHandlerException (
        @NotNull Exception eventHandlerException,
        @Nullable TextToken texttoken ) throws IOException, TextErrorException {

        if ( eventHandlerException instanceof TextErrorException textErrorException ) {
            throw textErrorException;
        } else if ( eventHandlerException instanceof IOException ioException ) {
            throw ioException;
        } else {
            throw new PdmlParserEventsHandlerException ( eventHandlerException, texttoken );
        }
    }

    private void nonCancelingErrorAtCurrentLocation ( @NotNull String message, @NotNull String id ) {
        PdmlParserHelper.nonAbortingErrorAtCurrentLocation ( message, id, reader, errorHandler );
    }

    private void nonCancelingError ( @NotNull String message, @NotNull String id, @NotNull TextToken token ) {
        PdmlParserHelper.nonAbortingError ( message, id, token, errorHandler );
    }
}
