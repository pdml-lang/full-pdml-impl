package dev.pp.pdml.core.parser;

import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.core.reader.CorePdmlReader;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.pdml.data.util.TextNode;
import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;

public class CorePdmlParser {

    private final @NotNull CorePdmlReader reader;
    // TODO? public @NotNull CorePdmlReader reader() { return reader; }
    private final boolean ignoreTextAfterRootNodeEnd;


    protected CorePdmlParser (
        @NotNull CorePdmlReader reader,
        @NotNull CorePdmlParserConfig config ) {

        this.reader = reader;
        this.ignoreTextAfterRootNodeEnd = config.getIgnoreTextAfterRootNodeEnd ();
    }

    public CorePdmlParser (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull CorePdmlParserConfig config ) throws IOException {

        this ( new CorePdmlReader ( reader, resource, lineOffset, columnOffset ),
            config );
    }

    public CorePdmlParser (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @NotNull CorePdmlParserConfig config ) throws IOException {

        this ( reader, resource, null, null, config );
    }

    public CorePdmlParser (
        @NotNull Reader reader,
        @NotNull CorePdmlParserConfig config ) throws IOException {

        this ( reader, null, config );
    }

    public CorePdmlParser (
        @NotNull Reader reader ) throws IOException {

        this ( reader, new CorePdmlParserConfig() );
    }


    // Root Node

    public @Nullable TaggedNode parseRootNode() throws IOException, PdmlException {

        skipWhitespaceBeforeRootNode();

        TaggedNode rootNode = parseTaggedNode ();
        // if ( rootNode == null )  return null;

        if ( ! ignoreTextAfterRootNodeEnd ) {
            requireDocumentEnd();
        }

        return rootNode;
    }

    public @NotNull TaggedNode requireRootNode() throws IOException, PdmlException {

        TaggedNode rootNode = parseRootNode();
        if ( rootNode != null ) {
            return rootNode;
        } else {
            throw errorAtCurrentLocation (
                "Root node required (e.g. \"[root ...]\")", "ROOT_NODE_REQUIRED" );
        }
    }

    public void skipWhitespaceBeforeRootNode() throws IOException {
        skipWhitespace ();
    }

    public void requireDocumentEnd() throws IOException, PdmlException {

        skipWhitespace ();
        if ( reader.isNotAtEnd() ) {
            throw errorAtCurrentLocation ( "No more text expected", "END_OF_DOCUMENT_EXPECTED" );
        }
    }


    // Tagged Node

    public @Nullable TaggedNode parseTaggedNode() throws IOException, PdmlException {

        @Nullable TaggedNode taggedNode = parseNodeStartAndTag ();
        if ( taggedNode == null ) {
            return null;
        }

        if ( parseNodeEnd ( taggedNode ) ) {
            // empty node
            return taggedNode;
        }

        taggedNode.setSeparator ( requireSeparator() );
        requireChildNodes ( taggedNode );
        requireNodeEnd ( taggedNode );

        return taggedNode;
    }

    public @NotNull TaggedNode requireTaggedNode() throws IOException, PdmlException {

        TaggedNode taggedNode = parseTaggedNode();
        if ( taggedNode != null ) {
            return taggedNode;
        } else {
            throw errorAtCurrentLocation (
                "Tagged node required.", "TAGGED_NODE_REQUIRED" );
        }
    }

    public @Nullable TaggedNode parseNodeStartAndTag() throws IOException, PdmlException {

        TextLocation startLocation = reader.currentLocation();
        if ( ! reader.readNodeStart() ) return null;
        NodeTag tag = requireTag();
        return new TaggedNode ( tag, startLocation );
    }

    public @NotNull TaggedNode requireNodeStartAndTagAndSeparator() throws IOException, PdmlException {

        @Nullable TaggedNode taggedNode = parseNodeStartAndTag ();
        if ( taggedNode == null ) {
            throw errorAtCurrentLocation ( "Node start required.", "NODE_START_REQUIRED" );
        }

        taggedNode.setSeparator ( requireSeparator() );

        return taggedNode;
    }


    // Tag

    public @Nullable NodeTag parseTag() throws IOException, PdmlException {

        TextLocation tagLocation = reader.currentLocation();
        String tag = reader.readTag ();
        if ( tag == null ) {
            return null;
        } else {
            return NodeTag.create ( tag, tagLocation );
        }
    }

    public @NotNull NodeTag requireTag() throws IOException, PdmlException {

        @Nullable NodeTag tag = parseTag();
        if ( tag != null ) {
            return tag;
        } else {
            throw errorAtCurrentLocation ( "Node tag required.", "NODE_TAG_REQUIRED" );
        }
    }


    // Separator

    public @NotNull String requireSeparator() throws IOException, PdmlException {

        @Nullable String separator = reader.readSeparator();
        if ( separator != null ) {
            return separator;
        } else {
            throw errorAtCurrentLocation (
                "A tag/value separator is required.", "SEPARATOR_REQUIRED" );
        }
    }


    // Child Nodes

    protected void parseChildNodes ( TaggedNode parentNode ) throws IOException, PdmlException {

        while ( reader.isNotAtEnd() ) {

            if ( reader.isAtNodeEnd() ) {
                return;

            } else if ( reader.isAtNodeStart() ) {
                TaggedNode childNode = requireTaggedNode ();
                parentNode.appendChild ( childNode );

            } else {
                TextLeaf textLeaf = requireTextLeaf ();
                parentNode.appendChild ( textLeaf );
            }
        }
    }

    protected void requireChildNodes ( TaggedNode parentNode ) throws IOException, PdmlException {

        parseChildNodes ( parentNode );
        if ( parentNode.isEmpty() ) {
            throw errorAtCurrentLocation ( "Child nodes required", "CHILD_NODES_REQUIRED" );
        }
    }


    // Node End

    public boolean parseNodeEnd ( @NotNull TaggedNode taggedNode ) throws IOException {

        TextLocation location = reader.currentLocation();
        if ( reader.readNodeEnd() ) {
            taggedNode.setEndLocation ( location );
            return true;
        } else {
            return false;
        }
    }

    public void requireNodeEnd ( @NotNull TaggedNode taggedNode ) throws IOException, MalformedPdmlException {

        if ( ! parseNodeEnd ( taggedNode ) ) {
            String message = "Expecting '" + CorePdmlConstants.NODE_END_CHAR + "' to close node '" + taggedNode.getTag () + "'";
            @Nullable TextLocation nodeStartLocation = taggedNode.getTag ().location();
            if ( nodeStartLocation != null ) {
                message = message + " at position: " + nodeStartLocation;
            }
            message = message + ".";

            throw errorAtCurrentLocation (
                message,
                "EXPECTING_NODE_END" );
        }
    }


    // Text Leaf

    public @Nullable TextLeaf parseTextLeaf() throws IOException, PdmlException {

        @Nullable TextLocation location = reader.currentLocation();
        @Nullable String text = reader.readText();
        return text == null ? null : new TextLeaf ( text, location );
    }

    public @NotNull TextLeaf requireTextLeaf() throws IOException, PdmlException {

        TextLeaf textLeaf = parseTextLeaf();
        if ( textLeaf != null ) {
            return textLeaf;
        } else {
            throw errorAtCurrentLocation ( "Text required", "TEXT_REQUIRED" );
        }
    }


    // Convenience Methods

    public @Nullable TextNode parseTextNode() throws IOException, PdmlException {

        @Nullable TaggedNode taggedNode = parseNodeStartAndTag ();
        if ( taggedNode == null ) {
            return null;
        }

        taggedNode.setSeparator ( requireSeparator() );

        @NotNull TextLeaf textLeaf = requireTextLeaf ();
        requireNodeEnd ( taggedNode );

        return new TextNode ( taggedNode.getTag (), textLeaf );
    }

    public @NotNull TextNode requireTextNode() throws IOException, PdmlException {

        @Nullable TextNode textNode = parseTextNode();
        if ( textNode != null ) {
            return textNode;
        } else {
            throw errorAtCurrentLocation ( "Text node required.", "TEXT_NODE_REQUIRED" );
        }
    }

    public @Nullable TaggedNode parseTaggedLeafNode() throws IOException, PdmlException {

        @Nullable TaggedNode taggedNode = parseNodeStartAndTag ();
        if ( taggedNode == null ) {
            return null;
        }

        requireNodeEnd ( taggedNode );

        return taggedNode;
    }

    public @NotNull TaggedNode requireTaggedLeafNode() throws IOException, PdmlException {

        @Nullable TaggedNode emptyNode = parseTaggedLeafNode ();
        if ( emptyNode != null ) {
            return emptyNode;
        } else {
            throw errorAtCurrentLocation ( "Empty node required.", "EMPTY_NODE_REQUIRED" );
        }
    }

    protected boolean skipWhitespace() throws IOException {
        return reader.skipWhitespace();
    }


    // Error Handling

    protected MalformedPdmlException errorAtCurrentLocation (
        @NotNull String message, @NotNull String id ) {

        return new MalformedPdmlException ( message, id, reader.currentCharToken () );
    }
}
