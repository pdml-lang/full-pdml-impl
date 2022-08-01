package dev.pdml.core.parser;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.name.ASTRawNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaceGetter;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.data.node.name.NodeName;
import dev.pdml.core.exception.PDMLDocumentSyntaxException;
import dev.pp.basics.utilities.DebugUtils;
import dev.pp.basics.utilities.character.CharConstants;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.text.error.TextError;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pdml.core.reader.PDMLReader;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PDMLParserHelper {

    // name

    public static @NotNull ASTNodeName requireNodeName (
        @NotNull PDMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, PDMLDocumentSyntaxException {

        ASTNodeName name = parseNodeName ( reader, namespaceGetter, errorHandler );
        checkNameNotNull ( name, reader, errorHandler );
        return name;
    }

    private static void checkNameNotNull (
        @Nullable Object name,
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws PDMLDocumentSyntaxException {

        if ( name == null ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "INVALID_NAME",
                "Expecting a valid name. A name cannot start with '" + reader.currentChar() + "'.",
                reader, errorHandler );
        }
    }

    public static @Nullable ASTNodeName parseNodeName (
        @NotNull PDMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, PDMLDocumentSyntaxException {

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

        return new ASTNodeName ( rawNodeName.getLocalName(), namespacePrefix, namespace );
    }

    public static @NotNull ASTRawNodeName requireRawNodeName (
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws IOException, PDMLDocumentSyntaxException {

        ASTRawNodeName name = parseRawNodeName ( reader, errorHandler );
        checkNameNotNull ( name, reader, errorHandler );
        return name;
    }

    public static @Nullable ASTRawNodeName parseRawNodeName (
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws IOException, PDMLDocumentSyntaxException {

        final TextToken namespacePrefixOrLocalName = parseSingleName ( reader );
        if ( namespacePrefixOrLocalName == null ) return null;

        if ( ! reader.skipChar ( PDMLConstants.NAMESPACE_PREFIX_NAME_SEPARATOR ) ) {
            return new ASTRawNodeName ( namespacePrefixOrLocalName, null );
        } else {
            TextToken localName = requireSingleName ( reader, errorHandler );
            return new ASTRawNodeName ( localName, namespacePrefixOrLocalName );
        }
    }

    public static TextToken requireSingleName ( @NotNull PDMLReader reader, @NotNull TextErrorHandler errorHandler )
        throws IOException, PDMLDocumentSyntaxException {

        final TextToken name = parseSingleName ( reader );
        checkNameNotNull ( name, reader, errorHandler );
        return name;
    }

    public static @Nullable TextToken parseSingleName ( @NotNull PDMLReader reader ) throws IOException {

        return reader.readNameToken();
    }


    // node start

    public static void requireNodeStart (
        @NotNull PDMLReader reader, @NotNull TextErrorHandler errorHandler ) throws IOException, TextErrorException {

        if ( ! parseNodeStart ( reader ) ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_NODE_START",
                "Expecting '" + PDMLConstants.NODE_START + "' to start a new node.",
                reader, errorHandler );
        }
    }

    public static boolean parseNodeStart ( @NotNull PDMLReader reader ) throws IOException, TextErrorException {

        return reader.consumeNodeStart();
    }


    // node end

    public static void requireNodeEnd (
        @NotNull PDMLReader reader,
        @NotNull ASTNodeName nodeName,
        @NotNull TextErrorHandler errorHandler ) throws IOException, PDMLDocumentSyntaxException {

        if ( ! parseNodeEnd ( reader ) ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_NODE_END",
                "Expecting '" + PDMLConstants.NODE_END + "' to close node '" + nodeName.qualifiedName () + "'.", // TODO add location
                reader, errorHandler );
        }
    }

    public static boolean parseNodeEnd ( @NotNull PDMLReader reader ) throws IOException {

        return reader.consumeNodeEnd();
    }


    // namespaces

    public static @Nullable
    ASTNamespaces parseNamespaces (
        @NotNull PDMLReader reader,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

        if ( ! reader.hasChar() ) return null;

        TextLocation startLocation = reader.currentLocation();
        if ( ! reader.skipString ( PDMLConstants.NAMESPACE_DECLARATION_START ) ) return null;
        TextToken startToken = new TextToken ( String.valueOf ( PDMLConstants.NAMESPACE_DECLARATION_START ), startLocation );

        if ( ! reader.skipOneSpaceOrTabOrNewline () ) {
            nonAbortingErrorAtCurrentLocation (
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
                nonAbortingError (
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
                nonAbortingError (
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
            nonAbortingError (
                "INVALID_NAMESPACE_PREFIX",
                "Namespace prefix '" + namespacePrefix.getText() + "' is invalid, because namespaces are not supported in this context.",
                namespacePrefix,
                errorHandler );
            return null;
        }

        ASTNamespace namespace = namespaceGetter.getByPrefix ( namespacePrefix.getText() );

        if ( namespace == null ) {
            nonAbortingError (
                "INVALID_NAMESPACE_PREFIX",
                "Namespace prefix '" + namespacePrefix.getText() + "' has not been declared.",
                namespacePrefix,
                errorHandler );
        }

        return namespace;
    }


    // attributes

    public static Parameters parseParametersWithOptionalParenthesis (
        @NotNull PDMLReader reader,
        @Nullable FormalParameters formalParameters,
        @NotNull TextErrorHandler errorHandler,
        boolean throwIfNonAbortingTextErrors ) throws IOException, TextErrorException {

        // reader.skipWhitespaceAndComments();

        TextToken startToken = reader.currentToken();

        @Nullable TextError initialLastError = errorHandler.lastError();

        @Nullable String defaultAttributeName =
            formalParameters == null ? null : formalParameters.getFirstPositionalParameterNameOrNull();
        @Nullable ASTNodeAttributes attributes = parseAttributesWithOptionalParenthesis (
            reader, defaultAttributeName, errorHandler );

        Parameters parameters = ASTNodeAttributes.attributesToParameters ( attributes, startToken, formalParameters, errorHandler );

        if ( throwIfNonAbortingTextErrors )
            errorHandler.throwIfNewErrors ( initialLastError );

        // reader.skipWhitespaceAndComments();

        return parameters;
    }

    public static @Nullable ASTNodeAttributes parseAttributesWithOptionalParenthesis (
        @NotNull PDMLReader reader,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean allAttributesOnSameLine,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

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
        @NotNull PDMLReader reader,
        @Nullable String defaultAttributeName,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

        return parseAttributesWithOptionalParenthesis (
            reader,
            defaultAttributeName == null ? null : new NodeName ( defaultAttributeName ),
            null, false, false, errorHandler );
    }

    public static @Nullable ASTNodeAttributes parseAttributesWithParenthesis (
        @NotNull PDMLReader reader,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

        TextLocation startLocation = reader.currentLocation();
        if ( ! reader.skipChar ( PDMLConstants.ATTRIBUTES_START ) ) return null;
        TextToken startToken = new TextToken ( String.valueOf ( PDMLConstants.ATTRIBUTES_START ), startLocation );

        return parseAttributesUntilEndParenthesis (
            reader, startToken, defaultAttributeName, namespaceGetter, skipSpaceAfterClosingParenthesis, errorHandler );
    }

    private static @Nullable ASTNodeAttributes parseAttributesUntilEndParenthesis (
        @NotNull PDMLReader reader,
        @Nullable TextToken startToken,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

        ASTNodeAttributes attributes = new ASTNodeAttributes ( startToken );

        parseDefaultAttribute ( defaultAttributeName, reader, attributes, errorHandler );

        while ( true ) {

            reader.skipWhitespaceAndComments();

            if ( reader.skipChar ( PDMLConstants.ATTRIBUTES_END ) ) {
                if ( skipSpaceAfterClosingParenthesis ) reader.skipChar ( ' ' );
                break;
            }

            if ( ! reader.hasChar () ) throw abortingSyntaxErrorAtCurrentLocation (
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
        @NotNull PDMLReader reader,
        @Nullable NodeName defaultAttributeName,
//        boolean defaultAttributeIsRequired,
        @Nullable ASTNamespaceGetter namespaceGetter,
        boolean allAttributesOnSameLine,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

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

    private static boolean isAtAttributesEnd ( @NotNull PDMLReader reader ) {

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
            attributes.add ( attribute );
        } else {
            nonAbortingError (
                "ATTRIBUTE_NOT_UNIQUE",
                "Attribute '" + attribute.getName() + "' has already been declared.",
                attribute.getName().getToken(),
                errorHandler );
        }
    }

    public static void parseDefaultAttribute (
        @Nullable NodeName defaultAttributeName,
        @NotNull PDMLReader reader,
        @NotNull ASTNodeAttributes attributes,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

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
        @NotNull PDMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

        ASTNodeName name = requireAttributeName ( reader, namespaceGetter, errorHandler );

        reader.skipOptionalSpacesAndTabsAndNewLines ();
        requireAttributeAssignSymbol ( reader, name, errorHandler );
        reader.skipOptionalSpacesAndTabsAndNewLines ();

        TextToken value = requireAttributeValue ( reader, errorHandler );

        return new ASTNodeAttribute ( name, value );
    }

    public static @NotNull ASTNodeName requireAttributeName (
        @NotNull PDMLReader reader,
        @Nullable ASTNamespaceGetter namespaceGetter,
        // @Nullable Set<String> allowedNames,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, PDMLDocumentSyntaxException {

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
        @NotNull PDMLReader reader,
        @NotNull ASTNodeName attributeName,
        @NotNull TextErrorHandler errorHandler ) throws IOException, PDMLDocumentSyntaxException {

        if ( ! reader.skipChar ( PDMLConstants.ATTRIBUTE_ASSIGN ) ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_ATTRIBUTE_ASSIGN_SYMBOL",
                "Expecting character '" + PDMLConstants.ATTRIBUTE_ASSIGN + "' to assign a value to attribute '"
                    + attributeName + "', but found '" + reader.currentChar () + "'.",
                reader, errorHandler );
        }
    }

    public static @NotNull TextToken requireAttributeValue (
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws IOException, TextErrorException {

        TextToken value = reader.readAttributeValueToken();
        if ( value != null ) {
            return value;
        } else {
            throw abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_ATTRIBUTE_VALUE",
                "Expecting an attribute value. An attribute value cannot start with '" + reader.currentChar() + "'.",
                reader, errorHandler );
        }
    }

    public static @NotNull TextToken requireText ( @NotNull PDMLReader reader, @NotNull TextErrorHandler errorHandler )
        throws IOException, TextErrorException {

        TextToken text = parseText ( reader );
        if ( text != null ) {
            return text;
        } else {
            throw abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_TEXT",
                "Expecting text.",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseText ( @NotNull PDMLReader reader )
        throws IOException, TextErrorException {

        return reader.readTextToken();
    }

    public static @NotNull TextToken requireComment (
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) throws IOException, PDMLDocumentSyntaxException {

        TextToken comment = parseComment ( reader );
        if ( comment != null ) {
            return comment;
        } else {
            throw abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_COMMENT",
                "Expecting a comment.",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseComment ( @NotNull PDMLReader reader )
        throws IOException, PDMLDocumentSyntaxException {

        return reader.readCommentToken();
    }

    public static String stripStartAndEndFromComment ( String comment ) {

        return comment.substring ( 2, comment.length() - 2 );
    }

    public static void requireDocumentEnd (
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler )
        throws IOException, PDMLDocumentSyntaxException {

        reader.skipWhitespaceAndComments ();

        // if ( reader.hasChar() ) nonCancelingErrorAtCurrentLocation (
        if ( reader.hasChar() ) throw abortingSyntaxErrorAtCurrentLocation (
            "EXPECTING_END_OF_DOCUMENT",
            "Expecting end of document. More text is not allowed.",
            reader, errorHandler );
    }


    // errors

    public static void nonAbortingErrorAtCurrentLocation (
        @NotNull String id,
        @NotNull String message,
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) {

        nonAbortingError ( id, message, reader.currentToken(), errorHandler );
    }

    public static void nonAbortingError (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @NotNull TextErrorHandler errorHandler ) {

        errorHandler.handleNonAbortingError ( id, message, token );
    }

    public static @NotNull PDMLDocumentSyntaxException abortingSyntaxErrorAtCurrentLocation (
        @NotNull String id,
        @NotNull String message,
        @NotNull PDMLReader reader,
        @NotNull TextErrorHandler errorHandler ) {

        return abortingSyntaxError ( id, message, reader.currentToken(), errorHandler );
    }

    public static @NotNull PDMLDocumentSyntaxException abortingSyntaxError (
        @NotNull String id,
        @NotNull String message,
        @Nullable TextToken token,
        @NotNull TextErrorHandler errorHandler ) {

        TextError error = TextError.createAbortingError ( id, message, token );
        errorHandler.handleError ( error );
        return new PDMLDocumentSyntaxException ( error );
    }
}
