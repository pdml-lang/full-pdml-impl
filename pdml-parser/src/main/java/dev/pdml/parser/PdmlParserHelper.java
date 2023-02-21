package dev.pdml.parser;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.attribute.NodeAttribute;
import dev.pdml.data.node.NodeName;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.PdmlReaderErrorHelper;
import dev.pdml.shared.constants.CorePdmlConstants;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.character.CharConstants;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.parameters.parameters.Parameters;
import dev.pp.parameters.parameters.ParametersCreator;
import dev.pp.parameters.parameterspecs.MutableOrImmutableParameterSpecs;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.util.List;

public class PdmlParserHelper {

    // Name

    public static @NotNull NodeName requireNodeName (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, PdmlDocumentSyntaxException {

        NodeName name = parseNodeName ( reader, errorHandler );
        checkNameNotNull ( name, reader, errorHandler );
        return name;
    }

    private static void checkNameNotNull (
        @Nullable Object name,
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) throws PdmlDocumentSyntaxException {

        if ( name == null ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "Expecting a valid name. A name cannot start with '" + reader.currentChar() + "'.",
                "INVALID_NAME",
                reader, errorHandler );
        }
    }

    public static @Nullable NodeName parseNodeName (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) throws IOException, PdmlDocumentSyntaxException {

        final TextToken namespacePrefixOrLocalName = parseSingleName ( reader );
        if ( namespacePrefixOrLocalName == null ) return null;

        if ( ! reader.skipChar ( PdmlExtensionsConstants.NAMESPACE_PREFIX_NAME_SEPARATOR ) ) {
            return new NodeName ( namespacePrefixOrLocalName );
        } else {
            TextToken localName = requireSingleName ( reader, errorHandler );
            return new NodeName ( localName, namespacePrefixOrLocalName );
        }
    }

    public static TextToken requireSingleName ( @NotNull PdmlReader reader, @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, PdmlDocumentSyntaxException {

        final TextToken name = parseSingleName ( reader );
        checkNameNotNull ( name, reader, errorHandler );
        return name;
    }

    public static @Nullable TextToken parseSingleName ( @NotNull PdmlReader reader ) throws IOException {

        return reader.readNameToken();
    }


    // Node start

    public static void requireNodeStart (
        @NotNull PdmlReader reader, @NotNull TextInspectionMessageHandler errorHandler ) throws IOException, TextErrorException {

        if ( ! parseNodeStart ( reader ) ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "Expecting '" + CorePdmlConstants.NODE_START + "' to start a new node.",
                "EXPECTING_NODE_START",
                reader, errorHandler );
        }
    }

    public static boolean parseNodeStart ( @NotNull PdmlReader reader ) throws IOException, TextErrorException {

        return reader.consumeNodeStart();
    }


    // Node end

    public static void requireNodeEnd (
        @NotNull PdmlReader reader,
        @NotNull NodeName nodeName,
        // @Nullable TextLocation nodeStartLocation,
        @NotNull TextInspectionMessageHandler errorHandler ) throws IOException, PdmlDocumentSyntaxException {

        if ( ! parseNodeEnd ( reader ) ) {

            String message = "Expecting '" + CorePdmlConstants.NODE_END + "' to close node '" + nodeName + "'";
            @Nullable TextLocation nodeStartLocation = nodeName.localNameLocation();
            if ( nodeStartLocation != null ) {
                message = message + " at position: " + nodeStartLocation;
            }
            message = message + ".";

            throw abortingSyntaxErrorAtCurrentLocation (
                message,
                "EXPECTING_NODE_END",
                reader, errorHandler );
        }
    }

    public static boolean parseNodeEnd ( @NotNull PdmlReader reader ) throws IOException {
        return reader.consumeNodeEnd();
    }

    public static void requireDocumentEnd (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, PdmlDocumentSyntaxException {

        reader.skipWhitespaceAndComments ();

        // if ( reader.hasChar() ) nonCancelingErrorAtCurrentLocation (
        if ( reader.hasChar() ) throw abortingSyntaxErrorAtCurrentLocation (
            "Expecting end of document. More text is not allowed.",
            "EXPECTING_END_OF_DOCUMENT",
            reader, errorHandler );
    }


    // Namespaces

    public static @Nullable MutableNodeNamespaces parseNamespaces (
        @NotNull PdmlReader reader,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, TextErrorException {

        if ( ! reader.hasChar() ) return null;

        TextLocation startLocation = reader.currentLocation();
        if ( ! reader.skipString ( PdmlExtensionsConstants.NAMESPACE_DECLARATION_START ) ) return null;
        TextToken startToken = new TextToken ( String.valueOf ( PdmlExtensionsConstants.NAMESPACE_DECLARATION_START ), startLocation );

        if ( ! reader.skipOneSpaceOrTabOrNewline () ) {
            nonAbortingErrorAtCurrentLocation (
                "'" + PdmlExtensionsConstants.NAMESPACE_DECLARATION_START + "' must be followed by a space, tab, or new line",
                "EXPECTING_NAMESPACE_SEPARATOR",
                reader,
                errorHandler );
        }

        @NotNull MutableNodeAttributes attributes = parseAttributesUntilEndSymbol (
            reader, PdmlExtensionsConstants.ATTRIBUTES_END, startToken, skipSpaceAfterClosingParenthesis, errorHandler );
        // if ( attributes == null ) return null;

        return attributesToNamespaces ( attributes, errorHandler );
    }

    private static @NotNull MutableNodeNamespaces attributesToNamespaces (
        @NotNull MutableNodeAttributes attributes,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        MutableNodeNamespaces namespaces = new MutableNodeNamespaces();

        List<Parameter<String>> list = attributes.list();
        // if ( list == null ) return null;
        if ( list == null ) return namespaces;

        for ( Parameter<String> attribute : list ) {
            namespaces.add ( new NodeNamespace ( attribute.nameToken(), attribute.valueToken() ) );
        }

        return namespaces;
    }


    // Attributes

    public static @Nullable Character readAttributesStart (
        @NotNull PdmlReader reader,
        boolean allowStandardStartSyntax,
        boolean allowAlternativeStartSyntax ) throws IOException {

        if ( allowStandardStartSyntax &&
            reader.skipChar ( PdmlExtensionsConstants.ATTRIBUTES_START_CHAR ) &&
            reader.skipChar ( PdmlExtensionsConstants.ATTRIBUTES_SYMBOL ) ) {
            return PdmlExtensionsConstants.ATTRIBUTES_END;

        } else if ( allowAlternativeStartSyntax &&
            reader.skipChar ( PdmlExtensionsConstants.ALTERNATIVE_ATTRIBUTES_START ) ) {
            return PdmlExtensionsConstants.ALTERNATIVE_ATTRIBUTES_END;

        } else {
            return null;
        }
    }

    public static <V> Parameters<V> parseParametersWithOptionalParenthesis (
        @NotNull PdmlReader reader,
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs,
        @NotNull TextInspectionMessageHandler errorHandler,
        boolean throwIfNonAbortingTextErrors ) throws IOException, TextErrorException {

        TextToken startToken = reader.currentToken();

        @Nullable TextError initialLastError = errorHandler.lastError();

        @Nullable MutableNodeAttributes attributes = parseAttributesWithOptionalParenthesis (
            reader, false, false, true, true, errorHandler );

        Parameters<V> parameters = ParametersCreator.createFromStringParameters ( attributes, startToken, parameterSpecs, errorHandler );

        if ( throwIfNonAbortingTextErrors ) errorHandler.throwIfNewErrors ( initialLastError );

        return parameters;
    }

    public static @Nullable MutableNodeAttributes parseAttributesWithOptionalParenthesis (
        @NotNull PdmlReader reader,
        boolean allAttributesOnSameLine,
        boolean skipSpaceAfterClosingParenthesis,
        boolean allowStandardStartSyntax,
        boolean allowAlternativeStartSyntax,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, TextErrorException {

        @NotNull TextToken startToken = reader.currentToken();
        @Nullable Character endSymbol = readAttributesStart (
            reader, allowStandardStartSyntax, allowAlternativeStartSyntax );

        if ( endSymbol != null ) {
            return parseAttributesUntilEndSymbol (
                reader, endSymbol, startToken, skipSpaceAfterClosingParenthesis, errorHandler );
        } else {
            return parseAttributesWithoutParenthesis (
                reader, allAttributesOnSameLine, errorHandler );
        }
    }

    public static @NotNull MutableNodeAttributes parseAttributesUntilEndSymbol (
        @NotNull PdmlReader reader,
        char endSymbol,
        @Nullable TextToken startToken,
        boolean skipSpaceAfterClosingParenthesis,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, TextErrorException {

        MutableNodeAttributes attributes = new MutableNodeAttributes ( startToken );

        while ( true ) {

            reader.skipWhitespaceAndComments();

            if ( reader.skipChar ( endSymbol ) ) {
                if ( skipSpaceAfterClosingParenthesis ) reader.skipChar ( ' ' );
                break;
            }

            if ( ! reader.hasChar () ) throw abortingSyntaxErrorAtCurrentLocation (
                "Missing attributes end symbol '" + endSymbol + "'.",
                "MISSING_ATTRIBUTES_END",
                reader, errorHandler );

            NodeAttribute attribute = requireOneAttribute ( reader, errorHandler );
            // TODO require 1 whitespace if not on endSymbol
            addAttribute ( attribute, attributes, errorHandler );
        }

        // An empty list of attributes ([@ ]) should not be returned as null
        // return attributes.isEmpty() ? null : attributes;
        return attributes;
    }

    private static @Nullable MutableNodeAttributes parseAttributesWithoutParenthesis (
        @NotNull PdmlReader reader,
        boolean allAttributesOnSameLine,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, TextErrorException {

        TextToken startToken = new TextToken ( String.valueOf ( reader.currentChar() ), reader.currentLocation() );
        MutableNodeAttributes attributes = new MutableNodeAttributes ( startToken );

        while ( reader.hasChar() ) {

            if ( allAttributesOnSameLine ) {
                reader.readOptionalSpacesAndTabs();
                if ( isAtSameLineAttributesEnd ( reader ) ) break;
            } else {
                reader.skipWhitespaceAndComments();
                if ( reader.isAtChar ( CorePdmlConstants.NODE_END ) ) break;
            }

            NodeAttribute attribute = requireOneAttribute ( reader, errorHandler );
            // TODO require 1 whitespace if not on ]
            addAttribute ( attribute, attributes, errorHandler );
        }

        return attributes.isEmpty() ? null : attributes;
    }

    private static boolean isAtSameLineAttributesEnd ( @NotNull PdmlReader reader ) {

        return switch ( reader.currentChar() ) {
            case CorePdmlConstants.NODE_END, CorePdmlConstants.NODE_START,
                CharConstants.UNIX_NEW_LINE, CharConstants.WINDOWS_NEW_LINE_START -> true;
            default -> false;
        };
    }

    private static void addAttribute (
        @NotNull NodeAttribute attribute,
        @NotNull MutableNodeAttributes attributes,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        if ( ! attributes.containsName ( attribute.getName() ) ) {
            attributes.add ( attribute );
        } else {
            nonAbortingError (
                "Attribute '" + attribute.getName() + "' has already been declared.",
                "ATTRIBUTE_NOT_UNIQUE",
                attribute.nameToken(),
                errorHandler );
        }
    }

    private static @NotNull NodeAttribute requireOneAttribute (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, TextErrorException {

        TextToken name = requireAttributeName ( reader, errorHandler );

        reader.skipOptionalSpacesAndTabsAndNewLines ();
        requireAttributeAssignSymbol ( reader, name.getText(), errorHandler );
        reader.skipOptionalSpacesAndTabsAndNewLines ();

        TextToken value = requireAttributeValue ( reader, errorHandler );

        return new NodeAttribute ( name, value );
    }

    private static @NotNull TextToken requireAttributeName (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, PdmlDocumentSyntaxException {

        return requireSingleName ( reader, errorHandler );
    }

    private static void requireAttributeAssignSymbol (
        @NotNull PdmlReader reader,
        @NotNull String attributeName,
        @NotNull TextInspectionMessageHandler errorHandler ) throws IOException, PdmlDocumentSyntaxException {

        if ( ! reader.skipChar ( PdmlExtensionsConstants.ATTRIBUTE_ASSIGN ) ) {
            throw abortingSyntaxErrorAtCurrentLocation (
                "Expecting character '" + PdmlExtensionsConstants.ATTRIBUTE_ASSIGN + "' to assign a value to attribute '"
                    + attributeName + "', but found '" + reader.currentChar () + "'.",
                "EXPECTING_ATTRIBUTE_ASSIGN_SYMBOL",
                reader, errorHandler );
        }
    }

    private static @NotNull TextToken requireAttributeValue (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) throws IOException, TextErrorException {

        TextToken value = reader.readAttributeValueToken();
        if ( value != null ) {
            return value;
        } else {
            throw abortingSyntaxErrorAtCurrentLocation (
                "Expecting an attribute value. An attribute value cannot start with '" + reader.currentChar() + "'.",
                "EXPECTING_ATTRIBUTE_VALUE",
                reader, errorHandler );
        }
    }


    // Text

    public static @NotNull TextToken requireText ( @NotNull PdmlReader reader, @NotNull TextInspectionMessageHandler errorHandler )
        throws IOException, TextErrorException {

        TextToken text = parseText ( reader );
        if ( text != null ) {
            return text;
        } else {
            throw abortingSyntaxErrorAtCurrentLocation (
                "Expecting text.",
                "EXPECTING_TEXT",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseText ( @NotNull PdmlReader reader )
        throws IOException, TextErrorException {

        return reader.readTextToken();
    }


    // Comment

    public static @NotNull TextToken requireComment (
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) throws IOException, PdmlDocumentSyntaxException {

        TextToken comment = parseComment ( reader );
        if ( comment != null ) {
            return comment;
        } else {
            throw abortingSyntaxErrorAtCurrentLocation (
                "Expecting a comment.",
                "EXPECTING_COMMENT",
                reader, errorHandler );
        }
    }

    public static @Nullable TextToken parseComment ( @NotNull PdmlReader reader )
        throws IOException, PdmlDocumentSyntaxException {

        return reader.readCommentToken();
    }

    public static String stripStartAndEndFromComment ( String comment ) {

        return comment.substring ( 2, comment.length() - 2 );
    }


    // Errors

    public static void nonAbortingErrorAtCurrentLocation (
        @NotNull String message,
        @NotNull String id,
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        PdmlReaderErrorHelper.nonAbortingErrorAtCurrentLocation ( message, id, reader, errorHandler );
    }

    public static void nonAbortingError (
        @NotNull String message,
        @NotNull String id,
        @Nullable TextToken token,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        PdmlReaderErrorHelper.nonAbortingError ( message, id, token, errorHandler );
    }

    public static @NotNull PdmlDocumentSyntaxException abortingSyntaxErrorAtCurrentLocation (
        @NotNull String message,
        @NotNull String id,
        @NotNull PdmlReader reader,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        return PdmlReaderErrorHelper.abortingSyntaxErrorAtCurrentLocation ( message, id, reader, errorHandler );
    }

    public static @NotNull PdmlDocumentSyntaxException abortingSyntaxError (
        @NotNull String message,
        @NotNull String id,
        @Nullable TextToken token,
        @NotNull TextInspectionMessageHandler errorHandler ) {

        return PdmlReaderErrorHelper.abortingSyntaxError ( message, id, token, errorHandler );
    }
}
