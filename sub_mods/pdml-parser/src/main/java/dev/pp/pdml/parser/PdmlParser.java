package dev.pp.pdml.parser;

import dev.pp.pdml.core.parser.CorePdmlParser;
import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.attribute.NodeAttributes;
import dev.pp.pdml.data.attribute.NodeAttribute;
import dev.pp.pdml.data.exception.InvalidPdmlDataException;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.namespace.NodeNamespaces;
import dev.pp.pdml.data.namespace.NodeNamespace;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.leaf.CommentLeaf;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.pdml.ext.DelegatingExtensionNodesHandler;
import dev.pp.pdml.ext.ExtensionNodeHandlers;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.pdml.ext.InsertStringFormat;
import dev.pp.pdml.ext.scripting.context.DocScriptingContext;
import dev.pp.pdml.ext.scripting.context.PdmlScriptingContext;
import dev.pp.pdml.ext.types.PdmlType;
import dev.pp.pdml.data.nodespec.PdmlNodeSpec;
import dev.pp.pdml.data.nodespec.PdmlNodeSpecs;
import dev.pp.pdml.ext.types.PdmlTypes;
import dev.pp.pdml.data.PdmlExtensionsConstants;
import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.parameters.parameter.Parameter;
import dev.pp.pjse.PjseConfig;
import dev.pp.pjse.util.scriptingenv.JavaScriptingEnvironmentWithFixedContext;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.reader.CharReader;
import dev.pp.core.text.reader.CharReaderImpl;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.token.TextToken;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static dev.pp.pdml.data.PdmlExtensionsConstants.*;

public class PdmlParser extends CorePdmlParser {


    private enum ExtensionInitiatorKind {
        TEXT, NODE_TAG, ATTRIBUTE_NAME, ATTRIBUTE_VALUE, STRING_LITERAL
    }

    private static class PendingsChars {

        private final @NotNull StringBuilder chars;
        private @NotNull TextLocation startLocation;


        PendingsChars ( @NotNull TextLocation startLocation ) {
            this.chars = new StringBuilder();
            this.startLocation = startLocation;
        }


        boolean isEmpty() {
            return chars.isEmpty();
        }

        @Nullable String getString() {
            return chars.isEmpty() ? null : chars.toString();
        }

        void append ( @NotNull String string ) {
            chars.append ( string );
        }

        void reset ( @NotNull TextLocation startLocation ) {
            chars.delete ( 0, chars.length() );
            this.startLocation = startLocation;
        }
    }


    private static final int DEFAULT_LOOKAHEAD = 500;


    private final @NotNull PdmlReader reader;
    public @NotNull PdmlReader getPdmlReader() { return reader; }

    private final @NotNull PdmlParserConfig config;
    public @NotNull PdmlParserConfig getConfig() { return config; }

    private final @NotNull DelegatingExtensionNodesHandler extensionNodesHandler;
    public @NotNull DelegatingExtensionNodesHandler getExtensionNodesHandler () {
        return extensionNodesHandler;
    }

    private final @Nullable PdmlNodeSpecs nodeSpecs;

    private final @Nullable PdmlTypes types;

    private final boolean allowAttributesWithoutCaret;

    private final @NotNull NodeNamespaces currentNamespacesInScope;

    private boolean optimizeTypedNodes = true;
    public void setOptimizeTypedNodes ( boolean optimizeTypedNodes ) {
        this.optimizeTypedNodes = optimizeTypedNodes;
    }


    private PdmlParser (
        @NotNull PdmlReader reader,
        @NotNull PdmlParserConfig config,
        @NotNull DelegatingExtensionNodesHandler extensionNodesHandler ) {

        super ( reader, config );

        this.reader = reader;
        this.config = config;
        this.extensionNodesHandler = extensionNodesHandler;
        this.nodeSpecs = config.getNodeSpecs();
        this.types = config.getTypes();
        this.allowAttributesWithoutCaret = config.getAllowAttributesWithoutCaret();
        this.currentNamespacesInScope = new NodeNamespaces ( null );
    }

    public static @NotNull PdmlParser create (
        @NotNull CharReader charReader,
        @NotNull PdmlParserConfig config ) throws IOException {

        DocScriptingContext docScriptingContext = new DocScriptingContext();
        PdmlScriptingContext scriptingContext = new PdmlScriptingContext ( docScriptingContext );
        JavaScriptingEnvironmentWithFixedContext<PdmlScriptingContext> scriptingEnvironment =
            new JavaScriptingEnvironmentWithFixedContext<> (
                scriptingContext, "ctx", PjseConfig.DEFAULT_CONFIG );
        DelegatingExtensionNodesHandler delegatingExtensionNodeHandler =
            new DelegatingExtensionNodesHandler (
                ExtensionNodeHandlers.STANDARD_HANDLERS, scriptingEnvironment );

        PdmlReader pdmlReader = new PdmlReader ( charReader );
        docScriptingContext.setPdmlReader ( pdmlReader );

        return new PdmlParser ( pdmlReader, config, delegatingExtensionNodeHandler );
    }

    public static @NotNull PdmlParser create (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull PdmlParserConfig config ) throws IOException {

        return create (
            CharReaderImpl.createAndAdvance ( reader, resource, lineOffset, columnOffset ),
            config );
    }

    public static @NotNull PdmlParser create (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @NotNull PdmlParserConfig config ) throws IOException {

        return create ( reader, resource, null, null, config );
    }

    public static @NotNull PdmlParser create (
        @NotNull Reader reader,
        @NotNull PdmlParserConfig config ) throws IOException {

        return create ( reader, null, config );
    }

    public static @NotNull PdmlParser create (
        @NotNull TextResourceReader textResourceReader,
        @NotNull PdmlParserConfig config ) throws IOException {

        return create (
            textResourceReader.getReader(), textResourceReader.getTextResource(), config );
    }


    @Override
    public @Nullable TaggedNode parseTaggedNode() throws IOException, PdmlException {

        TaggedNode taggedNode = parseTaggedNodeStartUntilAttributes ();
        if ( taggedNode == null ) {
            return null;
        }

        if ( parseNodeEnd ( taggedNode ) ) {
            // empty node
            return taggedNode;
        }

        parseChildNodes ( taggedNode );
        requireNodeEnd ( taggedNode );

        return taggedNode;
    }

    public void requireNodeEnd ( @NotNull TaggedNode taggedNode ) throws IOException, MalformedPdmlException {

        super.requireNodeEnd ( taggedNode );
        removeNamespacesInScope ( taggedNode.getNamespaceDefinitions() );
    }


    private @Nullable TaggedNode parseTaggedNodeStartUntilAttributes () throws IOException, PdmlException {

        @Nullable TaggedNode taggedNode = parseNodeStartAndTag ();
        if ( taggedNode == null ) {
            return null;
        }

        boolean isEmptyNode = reader.isAtNodeEnd();
        if ( ! isEmptyNode ) {
            taggedNode.setSeparator ( requireSeparator() );
        }

        if ( ! isEmptyNode && reader.isAtNodeEnd() ) {
            // return taggedNode ;
            throw new InvalidPdmlDataException (
                "A node cannot be closed after a separator. A separator must be followed by node content (e.g. text or child nodes). Note: an empty node cannot have a separator.",
                "NODE_CONTENT_REQUIRED",
                reader.currentToken() );
        }

        PdmlNodeSpec nodeSpec = nodeSpecs == null ? null : nodeSpecs.getOrNull ( taggedNode.getTag () );
        taggedNode.setSpec ( nodeSpec );
        String typeName = nodeSpec == null ? null : nodeSpec.getTypeName();

        if ( typeName == null && isEmptyNode ) {
            return taggedNode;
        }

        if ( ! isEmptyNode ) {

            NodeNamespaces namespaces = parseNamespaces();
            if ( namespaces != null ) {
                taggedNode.setNamespaceDefinitions ( namespaces );
            }

            NodeAttributes attributes = parseAttributesForNodeSpec ( nodeSpec );
            if ( attributes != null ) {
                taggedNode.setStringAttributes ( attributes );
            }
        }

        if ( typeName != null ) {
            handleType ( typeName, taggedNode );
        }

        return taggedNode;
    }

    private void handleType (
        @NotNull String typeName,
        @NotNull TaggedNode taggedNode ) throws IOException, PdmlException {

        PdmlType<?> type = types == null ? null : types.getOrNull ( typeName );
        if ( type == null ) {
            throw new InvalidPdmlDataException (
                "Type '" + typeName + "' doesn't exist, but is assigned to node '" + taggedNode.getTag () + "'.",
                "INVALID_TYPE",
                taggedNode.qualifiedTagToken () );
        }

        // type.parseValidateAndHandleObject ( this, taggedNode,false );
        type.parseValidateAndHandleObject (
            this,
            optimizeTypedNodes ? taggedNode : null,
            false );
    }

    public @NotNull TaggedNode requireTaggedNodeStartUntilAttributes () throws IOException, PdmlException {

        @Nullable TaggedNode taggedNode = parseTaggedNodeStartUntilAttributes ();
        if ( taggedNode != null ) {
            return taggedNode;
        } else {
            throw errorAtCurrentLocation (
                "Node required.", "NODE_REQUIRED" );
        }
    }

    @Override
    public @Nullable NodeTag parseTag() throws IOException, PdmlException {

        TextLocation tagOrNamespaceLocation = reader.currentLocation();
        String tagOrNamespacePrefix = parseStringLiteralOrNull ( ExtensionInitiatorKind.NODE_TAG );
        if ( tagOrNamespacePrefix == null ) {
            return null;
        }

        if ( ! reader.skipNamespaceSeparator() ) {
            return new NodeTag ( tagOrNamespacePrefix, tagOrNamespaceLocation, null, null );
        }

        TextLocation tagLocation = reader.currentLocation();
        String tag = parseStringLiteralOrNull ( ExtensionInitiatorKind.NODE_TAG );
        if ( tag != null ) {
            return new NodeTag ( tag, tagLocation, tagOrNamespacePrefix, tagOrNamespaceLocation );
        } else {
            throw errorAtCurrentLocation (
                "Tag required after namespace prefix '" + tagOrNamespacePrefix + "'.",
                "TAG_REQUIRED" );
        }
    }

    @Override
    public void parseChildNodes ( @NotNull TaggedNode parentNode ) throws IOException, PdmlException {

        while ( reader.isNotAtEnd() ) {

            if ( reader.isAtNodeEnd() ) {
                return;

            } else if ( reader.isAtNodeStart() ) {
                TaggedNode childNode = requireTaggedNode ();
                parentNode.appendChild ( childNode );

            } else {
                parseTextsCommentsAndExtensions (
                    ( text, location ) -> {
                        parentNode.appendChild ( new TextLeaf ( text, location ) );
                    },
                    ( comment, location ) -> {
                        parentNode.appendChild ( new CommentLeaf ( comment, location ) );
                    } );
            }
        }
    }

    public @Nullable String parseTextAndIgnoreComments() throws IOException, PdmlException {

        return parseCharsAndExtensionsAndIgnoreComments (
            ExtensionInitiatorKind.TEXT,
            CorePdmlConstants.INVALID_TEXT_CHARS,
            CorePdmlConstants.INVALID_TEXT_CHARS,
            CorePdmlConstants.TEXT_ESCAPE_CHARS );
    }

    private @Nullable String parseCharsAndExtensionsAndIgnoreComments (
        @NotNull ExtensionInitiatorKind initiatorKind,
        @NotNull Set<Character> endChars,
        @Nullable Set<Character> invalidChars,
        @NotNull Map<Character,Character> charEscapeMap ) throws IOException, PdmlException {

        StringBuilder result = new StringBuilder();
        parseCharsCommentsAndExtensions (
            ( text, location ) -> result.append ( text ),
            ( comment, location ) -> {},
            initiatorKind, endChars, invalidChars, charEscapeMap );
        return result.isEmpty() ? null : result.toString();
    }

    public void parseTextsCommentsAndExtensions (
        @NotNull BiConsumer<String, TextLocation> textConsumer,
        @NotNull BiConsumer<String, TextLocation> commentConsumer ) throws IOException, PdmlException {

        parseCharsCommentsAndExtensions (
            textConsumer,
            commentConsumer,
            ExtensionInitiatorKind.TEXT,
            CorePdmlConstants.INVALID_TEXT_CHARS,
            CorePdmlConstants.INVALID_TEXT_CHARS,
            CorePdmlConstants.TEXT_ESCAPE_CHARS );
    }

    private void parseCharsCommentsAndExtensions (
        @NotNull BiConsumer<String, TextLocation> charsConsumer,
        @NotNull BiConsumer<String, TextLocation> commentConsumer,
        @NotNull ExtensionInitiatorKind initiatorKind,
        @NotNull Set<Character> endChars,
        @Nullable Set<Character> invalidChars,
        @NotNull Map<Character,Character> charEscapeMap ) throws IOException, PdmlException {

        PendingsChars pendingChars = new PendingsChars ( reader.currentLocation() );

        while ( true ) {
            if ( reader.isAtExtensionStart() ) {
                handleExtension (
                    charsConsumer, commentConsumer, pendingChars, initiatorKind );
            } else {
                boolean charsAppended = reader.appendChars (
                    pendingChars.chars, endChars, invalidChars, charEscapeMap );
                if ( ! charsAppended ) {
                    break;
                }
            }
        }

        if ( ! pendingChars.isEmpty() ) {
            charsConsumer.accept ( pendingChars.getString(), pendingChars.startLocation );
            // charsSb.delete ( 0, charsSb.length() );
        }
    }

    private boolean handleExtension (
        @NotNull BiConsumer<String, TextLocation> charsConsumer,
        @NotNull BiConsumer<String, TextLocation> commentConsumer,
        @NotNull PendingsChars pendingsChars,
        @NotNull ExtensionInitiatorKind initiatorKind ) throws IOException, PdmlException {

        // Loop because there might be several extension nodes concatenated, e.g.:
        // ^u{set c=v}^{get c}
        while ( reader.currentChar() == PdmlExtensionsConstants.EXTENSION_START_CHAR ) {

            @Nullable Character nextChar = reader.peekNextChar();
            if ( nextChar == null ) {
                throw errorAtCurrentLocation (
                    "Expecting more characters to specify the extension.",
                    "INCOMPLETE_EXTENSION_SYNTAX" );
            }

            switch ( nextChar ) {

                case SINGLE_OR_MULTI_LINE_COMMENT_START_CHAR -> {
                    if ( config.isIgnoreComments() ) {
                        parseComment ( commentConsumer, initiatorKind );
                    } else {
                        if ( ! pendingsChars.isEmpty() ) {
                            charsConsumer.accept ( pendingsChars.getString(), pendingsChars.startLocation );
                        }
                        parseComment ( commentConsumer, initiatorKind );
                        pendingsChars.reset ( reader.currentLocation() );
                    }
                }

                case QUOTED_STRING_LITERAL_DELIMITER_CHAR, RAW_STRING_LITERAL_START_CHAR -> {
                    String string = parseStringLiteralExtension ( initiatorKind );
                    if ( string != null ) {
                        pendingsChars.append ( string );
                    }
                }

                default -> {
                    @Nullable InsertStringExtensionResult result = extensionNodesHandler.handleExtensionNode (
                        reader, this );
                    if ( result != null ) {
                        @Nullable String string = result.string();
                        if ( string != null ) {
                            if ( result.format() == InsertStringFormat.AS_IS ) {
                                reader.insertStringToRead ( string );
                            } else {
                                // charsSb.append ( PdmlEscapeUtil.escapeNodeText ( string ) );
                                pendingsChars.append ( string );
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void parseComment (
        @NotNull BiConsumer<String, TextLocation> commentConsumer,
        @NotNull ExtensionInitiatorKind initiatorKind ) throws IOException, PdmlException {

        if ( initiatorKind != ExtensionInitiatorKind.TEXT ) {
            throw errorAtCurrentLocation (
                "Comments are not allowed in this context.",
                "INVALID_COMMENT" );
        }

        if ( config.isIgnoreComments() ) {
            assert reader.skipSingleOrMultilineComment();
        } else {
            TextLocation location = reader.currentLocation();
            String comment = reader.readSingleOrMultilineComment();
            assert comment != null;
            commentConsumer.accept ( comment, location );
        }
    }

    private @Nullable String parseStringLiteralExtension (
        @NotNull ExtensionInitiatorKind initiatorKind ) throws IOException, PdmlException {

        if ( initiatorKind != ExtensionInitiatorKind.TEXT ) {
            throw errorAtCurrentLocation (
                "String literal extensions are not allowed in this context.",
                "INVALID_COMMENT" );
        }

        assert reader.readExtensionStart();

        String string;
        if ( reader.isAtChar ( QUOTED_STRING_LITERAL_DELIMITER_CHAR ) ) {
            if ( reader.isAtString ( PdmlExtensionsConstants.MULTILINE_STRING_LITERAL_DELIMITER ) ) {
                string = reader.readMultilineStringLiteral();
            } else {
                string = parseQuotedStringLiteral ( ExtensionInitiatorKind.STRING_LITERAL );
            }
        } else if ( reader.isAtChar ( RAW_STRING_LITERAL_START_CHAR ) ) {
            string = reader.readRawStringLiteral();
        } else {
            throw new IllegalStateException ( "Unexpected string literal extension" );
        }

        return string.isEmpty() ? null : string;
    }

    public @Nullable String parseStringLiteralOrNull() throws IOException, PdmlException {
        return parseStringLiteralOrNull ( ExtensionInitiatorKind.STRING_LITERAL );
    }

    private @Nullable String parseStringLiteralOrNull (
        @NotNull ExtensionInitiatorKind extensionInitiatorKind ) throws IOException, PdmlException {

        String result = parseEmptyableStringLiteral ( extensionInitiatorKind );
        if ( result != null && result.isEmpty() ) {
            return null;
        } else {
            return result;
        }
    }

    private @Nullable String parseEmptyableStringLiteral (
        @NotNull ExtensionInitiatorKind extensionInitiatorKind ) throws IOException, PdmlException {

        if ( reader.isAtChar ( QUOTED_STRING_LITERAL_DELIMITER_CHAR ) ) {
            if ( reader.isAtString ( MULTILINE_STRING_LITERAL_DELIMITER ) ) {
                return reader.readMultilineStringLiteral();
            } else {
                return parseQuotedStringLiteral ( extensionInitiatorKind );
            }
        } else if ( reader.isAtChar ( RAW_STRING_LITERAL_START_CHAR ) ) {
            return reader.readRawStringLiteral();
        } else {
            // unquoted string literal
            return parseCharsAndExtensionsAndIgnoreComments (
                extensionInitiatorKind,
                CorePdmlConstants.INVALID_TAG_CHARS,
                null,
                CorePdmlConstants.TAG_ESCAPE_CHARS );
        }
    }

    private @NotNull String parseQuotedStringLiteral (
        @NotNull ExtensionInitiatorKind extensionInitiatorKind ) throws IOException, PdmlException {

        final char delimiter = QUOTED_STRING_LITERAL_DELIMITER_CHAR;

        TextLocation startLocation = reader.currentLocation();
        reader.advanceChar(); // skip "

        // "" is used to denote a 'null' value
        if ( reader.skipChar ( delimiter ) ) {
            return "";
        }

        @Nullable String result = parseCharsAndExtensionsAndIgnoreComments (
            extensionInitiatorKind,
            QUOTED_STRING_LITERAL_END_CHARS,
            QUOTED_STRING_LITERAL_INVALID_CHARS,
            QUOTED_STRING_LITERAL_ESCAPE_MAP );

        if ( ! reader.skipChar ( delimiter ) ) {
            throw new MalformedPdmlException (
                "Missing closing " + delimiter + " to end the text. Text quoted with " + delimiter + " must be closed with a subsequent " + delimiter + ".",
                "CLOSING_QUOTE_REQUIRED",
                new TextToken ( QUOTED_STRING_LITERAL_DELIMITER_CHAR, startLocation ) );
        }

        return result == null ? "" : result;
    }


    // Namespaces

    public @Nullable NodeNamespaces parseNamespaces()
        throws IOException, PdmlException {

        reader.setMark ( DEFAULT_LOOKAHEAD );
        reader.skipSpacesAndTabsAndLineBreaks();

        TextToken startToken = reader.currentToken();
        // Note: don't use reader.isAtString because setMark can't ba called twice in a row
        boolean hasNamespaces = reader.skipAllWhileCharsMatch ( NAMESPACE_DECLARATIONS_EXTENSION_START );

        if ( hasNamespaces ) {
            reader.removeMark();
            char endChar = PdmlExtensionsConstants.NAMESPACE_DECLARATIONS_END;
            @NotNull NodeAttributes attributes = parseAttributesBetweenDelimiters ( startToken, endChar );
            reader.skipChar ( endChar );
            reader.skipChar ( ' ' );

            NodeNamespaces namespaces = attributesToNamespaces ( attributes );
            addNamespacesInScope ( namespaces );
            return namespaces;

        } else {
            reader.goBackToMark();
            return null;
        }
    }

    private void addNamespacesInScope ( @NotNull NodeNamespaces newNamespaces )
        throws InvalidPdmlDataException {

        Collection<NodeNamespace> list = newNamespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace newNamespace : list ) {
            String prefix = newNamespace.namePrefix();

            if ( ! currentNamespacesInScope.containsNamespace ( newNamespace ) ) {
                currentNamespacesInScope.add ( newNamespace );

            } else {
                NodeNamespace existingNamespace = currentNamespacesInScope.getByPrefix ( prefix );

                String message = "Namespace '" +  prefix + "' has already been declared";
                TextLocation location = existingNamespace.namePrefixLocation();
                if ( location != null ) message = message + " at" +
                    StringConstants.OS_LINE_BREAK + location;
                message = message + ".";

                throw new InvalidPdmlDataException (
                    message,
                    "NAMESPACE_NOT_UNIQUE",
                    newNamespace.URIToken() );
            }
        }
    }

    private void removeNamespacesInScope ( @Nullable NodeNamespaces namespaces ) {

        Collection<NodeNamespace> list = namespaces == null ? null : namespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace namespace : list ) {
            if ( currentNamespacesInScope.containsNamespace ( namespace ) ) {
                currentNamespacesInScope.remove ( namespace );
            }
        }
    }

    private static @NotNull NodeNamespaces attributesToNamespaces (
        @NotNull NodeAttributes attributes ) {

        NodeNamespaces namespaces = new NodeNamespaces ( attributes.getStartToken() );

        List<Parameter<String>> list = attributes.list();
        if ( list == null ) return namespaces;

        for ( Parameter<String> attribute : list ) {
            namespaces.add ( new NodeNamespace (
                attribute.getName(), attribute.getValue(),
                attribute.getNameLocation(), attribute.getValueLocation() ) );
        }

        return namespaces;
    }


    // Attributes

    private NodeAttributes parseAttributesForNodeSpec ( @Nullable PdmlNodeSpec nodeSpec )
        throws IOException, PdmlException {

        if ( nodeSpec != null && nodeSpec.hasOnlyAttributes() ) {
            // reader.skipSpacesAndTabsAndNewLinesAndComments();
            reader.skipSpacesAndTabsAndLineBreaks();
            return parseAttributesWithOptionalParenthesis();

        } else {

            reader.setMark ( DEFAULT_LOOKAHEAD );
            reader.skipSpacesAndTabsAndLineBreaks();

            TextToken startToken = reader.currentToken();
            // Note: don't use reader.isAtString because setMark can't ba called twice in a row
            boolean hasAttributes = reader.skipAllWhileCharsMatch ( ATTRIBUTES_EXTENSION_START );
            if ( ! hasAttributes && allowAttributesWithoutCaret ) {
                hasAttributes = reader.skipChar ( ATTRIBUTES_START_CHAR );
            }

            if ( hasAttributes ) {
                reader.removeMark();
                NodeAttributes attributes = parseAttributesBetweenDelimiters ( startToken, ATTRIBUTES_END_CHAR );
                requireAttributesEnd();
                return attributes;
            } else {
                reader.goBackToMark();
                return null;
            }
        }
    }

    public @Nullable NodeAttributes parseAttributes()
        throws IOException, PdmlException {

        return parseAttributes ( true );
    }

    public @Nullable NodeAttributes parseAttributesWithOptionalParenthesis()
        throws IOException, PdmlException {

        return parseAttributes ( false );
    }

    private @Nullable NodeAttributes parseAttributes (
        boolean requireParenthesis ) throws IOException, PdmlException {

        TextToken startToken = reader.readAttributesExtensionStartToken();
        if ( startToken == null && allowAttributesWithoutCaret ) {
            startToken = reader.readAttributesStartToken();
        }
        boolean hasParenthesis = startToken != null;
        if ( ! hasParenthesis && requireParenthesis ) {
            return null;
        }

        NodeAttributes attributes = parseAttributesBetweenDelimiters (
            startToken,
            hasParenthesis ? PdmlExtensionsConstants.ATTRIBUTES_END_CHAR : CorePdmlConstants.NODE_END_CHAR );
        if ( hasParenthesis ) {
            requireAttributesEnd();
        }

        if ( hasParenthesis ) {
            return attributes;
        } else {
            return attributes.isEmpty() ? null : attributes;
        }
    }

    private @NotNull NodeAttributes parseAttributesBetweenDelimiters (
        @Nullable TextToken startToken,
        char endChar )
        throws IOException, PdmlException {

        NodeAttributes attributes = new NodeAttributes ( startToken );

        boolean isFirstAttribute = true;
        while ( true ) {
            if ( reader.isAtChar ( endChar ) ) break;
            skipAttributesSeparator ( isFirstAttribute );
            if ( reader.isAtChar ( endChar ) ) break;

            if ( reader.isAtEnd() ) {
                throw errorAtCurrentLocation (
                    "Missing attributes end symbol '" + endChar + "'.",
                    "MISSING_ATTRIBUTES_END" );
            }

            NodeAttribute attribute = requireAttribute();
            addAttribute ( attribute, attributes );

            isFirstAttribute = false;
        }

        return attributes;
    }

    private void requireAttributesEnd() throws IOException {

        assert reader.readAttributesEnd ();
        reader.skipChar ( ' ' );
    }

    /*
    public @Nullable NodeAttributes parseAttributesWithoutDelimitersUntilEndOfInput() throws IOException, PdmlException {

        TextToken startToken = reader.currentToken();
        return parseAttributesUntilEndOfInput ( startToken );
    }

    private @Nullable NodeAttributes parseAttributesUntilEndOfInput (
        @Nullable TextToken startToken ) throws IOException, PdmlException {

        NodeAttributes attributes = new NodeAttributes ( startToken );

        boolean isFirstAttribute = true;
        while ( true ) {
            if ( reader.isAtEnd() ) break;
            skipAttributesSeparator ( isFirstAttribute );
            if ( reader.isAtEnd() ) break;
            NodeAttribute attribute = requireAttribute();
            addAttribute ( attribute, attributes );

            isFirstAttribute = false;
        }

        return attributes.isEmpty() ? null : attributes;
    }
     */

    private void skipAttributesSeparator ( boolean isFirstAttribute ) throws IOException, MalformedPdmlException {

        boolean skipped = reader.skipSpacesAndTabsAndLineBreaksAndComments();
        if ( ! isFirstAttribute && ! skipped ) {
            throw errorAtCurrentLocation ( "Attributes separator required", "ATTRIBUTES_SEPARATOR_REQUIRED" );
        }
    }

    private void addAttribute (
        @NotNull NodeAttribute attribute,
        @NotNull NodeAttributes attributes ) throws InvalidPdmlDataException {

        if ( ! attributes.containsName ( attribute.getName() ) ) {
            attributes.add ( attribute );
        } else {
            throw new InvalidPdmlDataException (
                "Attribute '" + attribute.getName() + "' has already been declared.",
                "ATTRIBUTE_NOT_UNIQUE",
                attribute.nameToken() );
        }
    }

    public @Nullable NodeAttribute parseAttribute()
        throws IOException, PdmlException {

        TextLocation nameLocation = reader.currentLocation();
        // @Nullable String tag = reader.readAttributeName();
        @Nullable String name = parseStringLiteralOrNull ( ExtensionInitiatorKind.ATTRIBUTE_NAME );
        if ( name == null ) {
            return null;
        }

        requireAttributeAssignment ( name );

        TextLocation valueLocation = reader.currentLocation();
        String value = requireAttributeValue();
        // TODO?
        if ( value.isEmpty() ) {
            value = null;
        }

        return new NodeAttribute ( name, value, nameLocation, valueLocation );
    }

    public @NotNull NodeAttribute requireAttribute()
        throws IOException, PdmlException {

        @Nullable NodeAttribute attribute = parseAttribute();
        if ( attribute != null ) {
            return attribute;
        } else {
            throw errorAtCurrentLocation (
                "Attribute required.",
                "ATTRIBUTE_REQUIRED" );
        }
    }

    private void requireAttributeAssignment ( @NotNull String attributeName )
        throws IOException, MalformedPdmlException {

        skipSpacesAndTabsAndLineBreaks ();
        if ( ! reader.readAttributeAssignSymbol() ) {
            throw errorAtCurrentLocation (
                "Expecting character '" + PdmlExtensionsConstants.ATTRIBUTE_ASSIGN_CHAR + "' to assign a value to attribute '"
                    + attributeName + "', but found '" + reader.currentChar() + "'.",
                "ATTRIBUTE_ASSIGN_SYMBOL_REQUIRED" );
        }
        skipSpacesAndTabsAndLineBreaks ();
    }

    private @NotNull String requireAttributeValue() throws IOException, PdmlException {

        String value = parseEmptyableStringLiteral ( ExtensionInitiatorKind.ATTRIBUTE_VALUE );
        if ( value != null ) {
            return value;
        } else {
            throw errorAtCurrentLocation (
                "Expecting an attribute value. An attribute value cannot start with '" + reader.currentChar() + "'.",
                "EXPECTING_ATTRIBUTE_VALUE" );
        }
    }
}
