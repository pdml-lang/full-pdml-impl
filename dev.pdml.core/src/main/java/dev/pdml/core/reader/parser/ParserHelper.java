package dev.pdml.core.reader.parser;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.name.ASTRawNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaceGetter;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.data.node.name.NodeName;
import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pp.text.utilities.character.CharConstants;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.net.URI;
import java.net.URISyntaxException;

public class ParserHelper {

    // name

    public static @NotNull ASTNodeName requireNodeName (
        @NotNull PXMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler )
        throws PXMLResourceException, MalformedPXMLDocumentException {

        ASTNodeName name = parseNodeName ( reader, namespaceGetter, errorHandler );
        if ( name != null ) {
            return name;
        } else {
            throw cancelingErrorAtCurrentLocation (
                "INVALID_NAME",
                "Expecting a valid name. A name cannot start with '" + reader.currentChar () + "'.",
                reader, errorHandler );
        }
    }

    public static @Nullable ASTNodeName parseNodeName (
        @NotNull PXMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler )
        throws PXMLResourceException, MalformedPXMLDocumentException {

/*
        final TextLocation location = currentLocation ( reader );

        final String namespacePrefixOrLocalName = parseSingleName ( reader );
        if ( namespacePrefixOrLocalName == null ) return null;

        final boolean hasNameSpace = reader.acceptChar ( Constants.NAMESPACE_SEPARATOR );

        String localName;
        Namespace namespace;

        if ( ! hasNameSpace ) {
            localName = namespacePrefixOrLocalName;
            namespace = null;

        } else {
            localName = requireSingleName ( reader );
            namespace = getNamespace ( namespacePrefixOrLocalName, location, namespaceGetter, errorHandler );
        }

        return new NodeName ( localName, namespace, location );
*/
        ASTRawNodeName rawNodeName = parseRawNodeName ( reader, errorHandler );
        if ( rawNodeName == null ) return null;

        return rawNodeNameToASTNodeName ( rawNodeName, namespaceGetter, errorHandler );
    }

    public static @NotNull ASTNodeName rawNodeNameToASTNodeName (
        @NotNull ASTRawNodeName rawNodeName,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler ) {

        TextToken namespacePrefix = rawNodeName.getNamespacePrefix();
        ASTNamespace namespace = null;
        if ( namespacePrefix != null ) namespace = requireNamespace (
            namespacePrefix, rawNodeName.getLocation(), namespaceGetter, errorHandler );

        return new ASTNodeName ( rawNodeName.getLocalName(), namespace );
    }

    public static @NotNull ASTRawNodeName requireRawNodeName (
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws PXMLResourceException, MalformedPXMLDocumentException {

        ASTRawNodeName name = parseRawNodeName ( reader, errorHandler );
        if ( name != null ) {
            return name;
        } else {
            throw cancelingErrorAtCurrentLocation (
                "INVALID_NAME",
                "Expecting a valid name. A name cannot start with '" + reader.currentChar () + "'.",
                reader, errorHandler );
        }
    }

    public static @Nullable ASTRawNodeName parseRawNodeName (
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws PXMLResourceException, MalformedPXMLDocumentException {

        final TextToken namespacePrefixOrLocalName = parseSingleName ( reader );
        if ( namespacePrefixOrLocalName == null ) return null;

        if ( ! reader.skipChar ( PDMLConstants.NAMESPACE_PREFIX_NAME_SEPARATOR ) ) {
            return new ASTRawNodeName ( namespacePrefixOrLocalName, null );
        } else {
            TextToken localName = requireSingleName ( reader, errorHandler );
            return new ASTRawNodeName ( localName, namespacePrefixOrLocalName );
        }
    }

    public static TextToken requireSingleName ( @NotNull PXMLReader reader, @NotNull TextErrorHandler errorHandler )
        throws PXMLResourceException, MalformedPXMLDocumentException {

        final TextToken name = parseSingleName ( reader );
        if ( name != null ) {
            return name;
        } else {
            throw cancelingErrorAtCurrentLocation (
                "INVALID_NAME",
                "Expecting a valid name. A name cannot start with '" + reader.currentChar() + "'.",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseSingleName ( @NotNull PXMLReader reader ) throws PXMLResourceException {

        return reader.readNameToken();
    }


    // node start

    public static void requireNodeStart (
        @NotNull PXMLReader reader, @NotNull TextErrorHandler errorHandler ) throws TextReaderException {

        if ( ! parseNodeStart ( reader ) ) {
            throw cancelingErrorAtCurrentLocation (
                "EXPECTING_NODE_START",
                "Expecting '" + PDMLConstants.NODE_START + "' to start a new node.",
                reader, errorHandler );
        }
    }

    public static boolean parseNodeStart ( @NotNull PXMLReader reader ) throws TextReaderException {

        return reader.consumeNodeStart();
    }


    // node end

    public static void requireNodeEnd (
        @NotNull PXMLReader reader,
        @NotNull ASTNodeName nodeName,
        @NotNull TextErrorHandler errorHandler ) throws PXMLResourceException, MalformedPXMLDocumentException {

        if ( ! parseNodeEnd ( reader ) ) {
            throw cancelingErrorAtCurrentLocation (
                "EXPECTING_NODE_END",
                "Expecting '" + PDMLConstants.NODE_END + "' to close node '" + nodeName.fullName() + "'.", // TODO add location
                reader, errorHandler );
        }
    }

    public static boolean parseNodeEnd ( @NotNull PXMLReader reader ) throws PXMLResourceException {

        return reader.consumeNodeEnd();
    }

    /*
    public static void requireIsAtNodeEnd (
        @NotNull PXMLReader reader ) throws MalformedPXMLDocumentException {

        if ( reader.currentChar () != Constants.NODE_END ) {
            throw cancelingErrorAtCurrentLocation (
                "EXPECTING_NODE_END",
                "Expecting '" + Constants.NODE_END + "' to close the node.",
                reader );
        }
    }
    */


    // namespaces

    public static @Nullable
    ASTNamespaces parseNamespaces (
        @NotNull PXMLReader reader,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        if ( ! reader.hasChar() ) return null;

        TextLocation startLocation = reader.currentLocation();
        if ( ! reader.skipString ( PDMLConstants.NAMESPACE_DECLARATION_START ) ) return null;
        TextToken startToken = new TextToken ( String.valueOf ( PDMLConstants.NAMESPACE_DECLARATION_START ), startLocation );

        if ( ! reader.skipOneSpaceOrTabOrNewline () ) {
            nonCancelingErrorAtCurrentLocation (
                "EXPECTING_NAMESPACE_SEPARATOR",
                "'" + PDMLConstants.NAMESPACE_DECLARATION_START + "' must be followed by a space, tab, or new line",
                reader,
                errorHandler );
        }

        ASTNodeAttributes attributes = parseAttributesUntilEndParenthesis (
            reader, startToken, null, null, skipSpaceAfterClosingParenthesis, errorHandler );
        if ( attributes == null ) return null;

        return attributesToNamespaces ( attributes, errorHandler );
    }

    private static @NotNull ASTNamespaces attributesToNamespaces (
        @NotNull ASTNodeAttributes attributes,
        @NotNull TextErrorHandler errorHandler ) {

        ASTNamespaces namespaces = new ASTNamespaces();

        for ( ASTNodeAttribute attribute : attributes.getList() ) {

            if ( attribute.hasNamespace() ) {
                nonCancelingError (
                    "INVALID_PREFIX",
                    "A namespace prefix cannot be used in a namespace declaration",
                    attribute.getNamespacePrefix(),
                    errorHandler );
            };

            TextToken prefix = attribute.getLocalName();
            TextToken URIToken = attribute.getValue();
            TextLocation location = attribute.getName().getLocation();

            URI URI;
            try {
                URI = new URI ( URIToken.getText() );
            } catch ( URISyntaxException e ) {
                nonCancelingError (
                    "INVALID_URI",
                    "'" + URIToken.getText() + "' is an invalid URI. Reason: " + e.getMessage(),
                    URIToken,
                    errorHandler );
                URI = PDMLConstants.UNKNOWN_URI;
            }

            ASTNamespace namespace = new ASTNamespace ( prefix, URIToken, URI );

            namespaces.addOrReplace ( namespace );
        }

        return namespaces;
    }

    public static @NotNull ASTNamespace requireNamespace (
        @NotNull TextToken namespacePrefix,
        @Nullable TextLocation location,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler ) {

        ASTNamespace namespace = getNamespace ( namespacePrefix, namespaceGetter, errorHandler );
        if ( namespace == null ) return ASTNamespaces.getInvalidNamespace ( namespacePrefix, location );

        return namespace;
    }

    public static @Nullable ASTNamespace getNamespace (
        @NotNull TextToken namespacePrefix,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler ) {

        if ( namespaceGetter == null ) {
            nonCancelingError (
                "INVALID_NAMESPACE_PREFIX",
                "Namespace prefix '" + namespacePrefix + "' is invalid, because namespaces are not supported in this context.",
                namespacePrefix,
                errorHandler );
            return null;
        }

        ASTNamespace namespace = namespaceGetter.getByPrefix ( namespacePrefix.getText() );

        if ( namespace == null ) {
            nonCancelingError (
                "INVALID_NAMESPACE_PREFIX",
                "Namespace prefix '" + namespacePrefix + "' has not been declared.",
                namespacePrefix,
                errorHandler );
        }

        return namespace;
    }


    // attributes

    public static @Nullable ASTNodeAttributes parseAttributesWithOptionalParenthesis (
        @NotNull PXMLReader reader,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean allAttributesOnSameLine,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        // reader.skipWhitespaceAndComments();
        if ( reader.isAtChar ( PDMLConstants.ATTRIBUTES_START ) ) {
            return parseAttributesWithParenthesis (
                reader, defaultAttributeName, namespaceGetter, skipSpaceAfterClosingParenthesis, errorHandler );
        } else {
            return parseAttributesWithoutParenthesis (
                reader, defaultAttributeName, namespaceGetter, allAttributesOnSameLine, errorHandler );
        }
    }

    public static @Nullable ASTNodeAttributes parseAttributesWithOptionalParenthesis (
        @NotNull PXMLReader reader,
        @Nullable String defaultAttributeName,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        return parseAttributesWithOptionalParenthesis (
            reader,
            defaultAttributeName == null ? null : new NodeName ( defaultAttributeName ),
            null, false, false, errorHandler );
    }

    public static @Nullable ASTNodeAttributes parseAttributesWithParenthesis (
        @NotNull PXMLReader reader,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        TextLocation startLocation = reader.currentLocation();
        if ( ! reader.skipChar ( PDMLConstants.ATTRIBUTES_START ) ) return null;
        TextToken startToken = new TextToken ( String.valueOf ( PDMLConstants.ATTRIBUTES_START ), startLocation );

        return parseAttributesUntilEndParenthesis (
            reader, startToken, defaultAttributeName, namespaceGetter, skipSpaceAfterClosingParenthesis, errorHandler );
    }

    private static @Nullable ASTNodeAttributes parseAttributesUntilEndParenthesis (
        @NotNull PXMLReader reader,
        @Nullable TextToken startToken,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        ASTNodeAttributes attributes = new ASTNodeAttributes ( startToken );

        parseDefaultAttribute ( defaultAttributeName, reader, attributes, errorHandler );

        while ( true ) {

            reader.skipWhitespaceAndComments();

            if ( reader.skipChar ( PDMLConstants.ATTRIBUTES_END ) ) {
                if ( skipSpaceAfterClosingParenthesis ) reader.skipChar ( ' ' );
                break;
            }

            if ( ! reader.hasChar () ) throw cancelingErrorAtCurrentLocation (
                "MISSING_ATTRIBUTES_END",
                "Missing attributes end symbol '" + PDMLConstants.ATTRIBUTES_END + "'.",
                reader, errorHandler );

            ASTNodeAttribute attribute = requireOneAttribute ( reader, namespaceGetter, errorHandler );
            // TODO require 1 whitespace if not on )
            addAttribute ( attribute, attributes, errorHandler );
        }

        return attributes.isEmpty() ? null : attributes;
    }

    public static @Nullable ASTNodeAttributes parseAttributesWithoutParenthesis (
        @NotNull PXMLReader reader,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean allAttributesOnSameLine,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        TextToken startToken = new TextToken ( String.valueOf ( reader.currentChar() ), reader.currentLocation() );
        ASTNodeAttributes attributes = new ASTNodeAttributes ( startToken );

        parseDefaultAttribute ( defaultAttributeName, reader, attributes, errorHandler );

        while ( reader.hasChar() ) {

            if ( allAttributesOnSameLine ) {
                reader.readOptionalSpacesAndTabs();
                if ( isAtAttributesEnd ( reader ) ) break;
            } else {
                reader.skipWhitespaceAndComments();
                if ( reader.isAtChar ( PDMLConstants.NODE_END ) ) break;
            }

            ASTNodeAttribute attribute = requireOneAttribute ( reader, namespaceGetter, errorHandler );
            // TODO require 1 whitespace if not on ]
            addAttribute ( attribute, attributes, errorHandler );
        }

        return attributes.isEmpty() ? null : attributes;
    }

    private static boolean isAtAttributesEnd ( @NotNull PXMLReader reader ) {

        switch ( reader.currentChar() ) {
            case PDMLConstants.NODE_END:
            case PDMLConstants.NODE_START:
            case CharConstants.UNIX_NEW_LINE:
            case CharConstants.WINDOWS_NEW_LINE_START:
                return true;
            default:
                return false;
        }
    }

    public static void addAttribute (
        @NotNull ASTNodeAttribute attribute,
        @NotNull ASTNodeAttributes attributes,
        @NotNull TextErrorHandler errorHandler ) {

        if ( ! attributes.contains ( attribute ) ) {
            try {
                attributes.add ( attribute );
            } catch ( Exception e ) {
                throw new RuntimeException ( e );
            }
        } else {
            nonCancelingError (
                "ATTRIBUTE_NOT_UNIQUE",
                "Attribute '" + attribute.getName() + "' has already been declared.",
                attribute.getName().getToken(),
                errorHandler );
        }
    }

    public static void parseDefaultAttribute (
        @Nullable NodeName defaultAttributeName,
        @NotNull PXMLReader reader,
        @NotNull ASTNodeAttributes attributes,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        if ( defaultAttributeName == null ) return;

        reader.skipWhitespaceAndComments();

        if ( ! ( reader.isAtChar ( PDMLConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE ) ||
            reader.isAtChar ( PDMLConstants.ATTRIBUTE_VALUE_SINGLE_QUOTE ) ) ) return;

        TextToken value = reader.readQuotedAttributeValueToken();
        assert value != null;

        ASTNodeAttribute attribute = new ASTNodeAttribute (
            NodeName.NodeNameToASTNodeName ( defaultAttributeName ),
            value );

        addAttribute ( attribute, attributes, errorHandler );
    }

    public static @NotNull ASTNodeAttribute requireOneAttribute (
        @NotNull PXMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        ASTNodeName name = requireAttributeName ( reader, namespaceGetter, errorHandler );

        reader.skipOptionalSpacesAndTabsAndNewLines ();
        requireAttributeAssignSymbol ( reader, name, errorHandler );
        reader.skipOptionalSpacesAndTabsAndNewLines ();

        TextToken value = requireAttributeValue ( reader, errorHandler );

        return new ASTNodeAttribute ( name, value );
    }

    public static @NotNull ASTNodeName requireAttributeName (
        @NotNull PXMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        // @Nullable Set<String> allowedNames,
        @NotNull TextErrorHandler errorHandler )
        throws PXMLResourceException, MalformedPXMLDocumentException {

        return requireNodeName ( reader, namespaceGetter, errorHandler );
        /*
        ASTNodeName name = requireNodeName ( reader, namespaceGetter, errorHandler );

        if ( allowedNames == null ) return name;

        if ( ! allowedNames.contains ( name.fullName () ) ) {
            nonCancelingErrorAtLocation (
                "INVALID_ATTRIBUTE_NAME",
                "Attribute '" + name + "' is not allowed in this context.",
                name.getLocation(),
                errorHandler );
        }

        return name;
        */
    }

    public static void requireAttributeAssignSymbol (
        @NotNull PXMLReader reader,
        @NotNull ASTNodeName attributeName,
        @NotNull TextErrorHandler errorHandler ) throws PXMLResourceException, MalformedPXMLDocumentException {

        if ( ! reader.skipChar ( PDMLConstants.ATTRIBUTE_ASSIGN ) ) {
            throw cancelingErrorAtCurrentLocation (
                "EXPECTING_ATTRIBUTE_ASSIGN_SYMBOL",
                "Expecting character '" + PDMLConstants.ATTRIBUTE_ASSIGN + "' to assign a value to attribute '"
                    + attributeName + "', but found '" + reader.currentChar () + "'.",
                reader, errorHandler );
        }
    }

    public static @NotNull TextToken requireAttributeValue (
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws TextReaderException {

        TextToken value = reader.readAttributeValueToken();
        if ( value != null ) {
            return value;
        } else {
            throw cancelingErrorAtCurrentLocation(
                "EXPECTING_ATTRIBUTE_VALUE",
                "Expecting an attribute value. An attribute value cannot start with '" + reader.currentChar () + "'.",
                reader, errorHandler );
        }
    }

    public static @NotNull TextToken requireText ( @NotNull PXMLReader reader, @NotNull TextErrorHandler errorHandler )
        throws TextReaderException {

        TextToken text = parseText ( reader );
        if ( text != null ) {
            return text;
        } else {
            throw cancelingErrorAtCurrentLocation(
                "EXPECTING_TEXT",
                "Expecting text.",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseText ( @NotNull PXMLReader reader ) throws TextReaderException {

        return reader.readTextToken();
    }

    public static @NotNull TextToken requireComment (
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws TextReaderException {

        TextToken comment = parseComment ( reader );
        if ( comment != null ) {
            return comment;
        } else {
            throw cancelingErrorAtCurrentLocation(
                "EXPECTING_COMMENT",
                "Expecting a comment.",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseComment ( @NotNull PXMLReader reader ) throws TextReaderException {

        return reader.readCommentToken();
    }

    public static String stripStartAndEndFromComment ( String comment ) {

        return comment.substring ( 2, comment.length() - 2 );
    }

    public static void requireDocumentEnd (
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler )
        throws PXMLResourceException, MalformedPXMLDocumentException {

        reader.skipWhitespaceAndComments ();

        if ( reader.hasChar() ) nonCancelingErrorAtCurrentLocation (
            "EXPECTING_END_OF_DOCUMENT",
            "Expecting end of document. More text is not allowed.",
            reader,
            errorHandler );
    }

/*
    public static @Nullable String parseAttributeValue (
        @NotNull PXMLReader reader ) throws PXMLResourceException, MalformedPXMLDocumentException {

        return reader.readAttributeValue();
    }

    public static TextLocation currentLocation ( @NotNull PXMLReader reader ) {

        return reader.currentLocation ();
    }

    public static void skipWhitespaceAndComments (
        @NotNull PXMLReader reader ) throws PXMLResourceException, MalformedPXMLDocumentException {

        while ( reader.skipWhiteSpace() || reader.skipComment() ) {}
    }
*/


    // errors

    public static void nonCancelingErrorAtCurrentLocation (
        @NotNull String id,
        @NotNull String message,
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler ) {

        nonCancelingError ( id, message, reader.currentToken(), errorHandler );
    }

    public static void nonCancelingError (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @NotNull TextErrorHandler errorHandler ) {

        errorHandler.handleError ( id, message, token );
    }

    public static @NotNull MalformedPXMLDocumentException cancelingErrorAtCurrentLocation (
        @NotNull String id,
        @NotNull String message,
        @NotNull PXMLReader reader,
        @NotNull TextErrorHandler errorHandler ) {

        return cancelingError ( id, message, reader.currentToken(), errorHandler );
    }

    public static @NotNull MalformedPXMLDocumentException cancelingError (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @NotNull TextErrorHandler errorHandler ) {

        errorHandler.handleError ( id, message, token );
        return new MalformedPXMLDocumentException ( id, message, token );
    }

}
