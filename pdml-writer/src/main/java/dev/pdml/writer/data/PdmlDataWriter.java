package dev.pdml.writer.data;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.data.node.root.MutableRootNode;
import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pdml.data.node.branch.MutableBranchNode;
import dev.pdml.data.node.branch.MutableChildNode;
import dev.pdml.data.node.branch.MutableChildNodes;
import dev.pdml.writer.PdmlWriter;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.parameters.parameters.MutableOrImmutableParameters;

import dev.pdml.data.node.leaf.MutableCommentNode;
import dev.pdml.data.node.leaf.MutableTextNode;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PdmlDataWriter {

    private final @NotNull PdmlWriter writer;
    @NotNull public PdmlWriter getWriter () { return writer; }

    private final @NotNull PdmlDataWriterConfig config;
    @NotNull public PdmlDataWriterConfig getConfig () { return config; }



    public PdmlDataWriter (
        @NotNull PdmlWriter writer,
        @NotNull PdmlDataWriterConfig config ) {

        this.writer = writer;
        this.config = config;
    }

    public PdmlDataWriter (
        @NotNull Writer writer,
        @NotNull PdmlDataWriterConfig config ) {

        this ( new PdmlWriter ( writer ), config );
    }

    public PdmlDataWriter ( @NotNull Writer writer ) {

        this ( writer, new PdmlDataWriterConfig() );
    }


    public @NotNull PdmlDataWriter writeRootNode ( @NotNull MutableRootNode rootNode ) throws IOException {
        return writeRootOrBranchNode ( rootNode );
    }

    public @NotNull PdmlDataWriter writeRootOrBranchNode ( @NotNull MutableRootOrBranchNode node ) throws IOException {

        writer.writeNodeStartSymbol();

        writer.writeName ( node.getName().namespacePrefix(), node.getName().localName() );
        if ( node.isNotEmpty() ) writer.writeNameValueSeparator();

        writeNodeNamespaces ( node );
        writeNodeAttributes ( node );

        MutableChildNodes childNodes = node.getChildren();
        if ( childNodes != null ) {
            Iterator<MutableChildNode> it = childNodes.iterator();
            while ( it.hasNext() ) {
                MutableChildNode childNode = it.next();
                writeChildNode ( childNode );
            }
        }

        writer.writeNodeEndSymbol();

        return this;
    }

    public @NotNull PdmlDataWriter writeNodeStart ( @NotNull MutableRootOrBranchNode node ) throws IOException {
        return writeNodeStart ( node.getName(), node.getNamespaceDefinitions(), node.getAttributes(), node.isEmpty() );
    }

    public @NotNull PdmlDataWriter writeNodeStart (
        @NotNull NodeName name,
        @Nullable MutableNodeNamespaces namespaces,
        @Nullable MutableNodeAttributes attributes,
        boolean isEmptyNode ) throws IOException {

        writer.writeNodeStartSymbol();

        writer.writeName ( name.namespacePrefix(), name.localName() );
        if ( ! isEmptyNode ) writer.writeNameValueSeparator();

        if ( namespaces != null ) writeNamespaces ( namespaces );
        if ( attributes != null ) writeAttributes ( attributes );

        return this;
    }

    public @NotNull PdmlDataWriter writeChildNode ( @NotNull MutableChildNode childNode ) throws IOException {

        /* Use this code when this is no more a preview feature in Java
        switch ( childNode ) {
            case MutableBranchNode branchNode -> writeRootOrBranchNode ( branchNode );
            case MutableTextNode textNode -> writeTextNode ( textNode );
            case MutableCommentNode commentNode -> writeCommentNode ( commentNode );
            default -> throw new IllegalStateException ( "Unexpected value: " + childNode );
        }
         */

        if ( childNode instanceof MutableBranchNode branchNode ) {
            writeRootOrBranchNode ( branchNode );
        } else if ( childNode instanceof MutableTextNode textNode ) {
            writeTextNode ( textNode );
        } else if ( childNode instanceof MutableCommentNode commentNode ) {
            writeCommentNode ( commentNode );
        } else {
            throw new IllegalStateException ( "Unexpected value: " + childNode );
        }

        return this;
    }

    public @NotNull PdmlDataWriter writeTextNode ( @NotNull MutableTextNode textNode ) throws IOException {
        return writeText ( textNode.getText() );
    }

    public @NotNull PdmlDataWriter writeText ( @NotNull String text ) throws IOException {
        writer.writeText ( text, true );
        return this;
    }

    public @NotNull PdmlDataWriter writeCommentNode ( @NotNull MutableCommentNode commentNode ) throws IOException {
        return writeComment ( commentNode.getText() );
    }

    public @NotNull PdmlDataWriter writeComment ( @NotNull String text ) throws IOException {
        writer.writeComment ( text );
        return this;
    }


    // Namespaces

    public @NotNull PdmlDataWriter writeNodeNamespaces ( @NotNull MutableRootOrBranchNode node ) throws IOException {

        var namespaces = node.getNamespaceDefinitions();
        if ( namespaces == null ) return this;
        writeNamespaces ( namespaces );
        if ( node.hasAttributes() || node.hasChildNodes() ) writer.write ( " " );

        return this;
    }

    public @NotNull PdmlDataWriter writeNamespaces ( @NotNull MutableNodeNamespaces namespaces ) throws IOException {

        Collection<NodeNamespace> list = namespaces.list();
        if ( list != null ) writeNamespaces ( list );

        return this;
    }

    public @NotNull PdmlDataWriter writeNamespaces ( @NotNull Collection<NodeNamespace> namespaces ) throws IOException {

        writer.writeNamespacesStart();
        for ( NodeNamespace namespace : namespaces ) {
            writer.write ( " " );
            writeNamespace ( namespace );
        }
        writer.writeNamespacesEnd();

        return this;
    }

    public @NotNull PdmlDataWriter writeNamespace ( @NotNull NodeNamespace namespace ) throws IOException {

        writer.writeNamespace ( namespace.namePrefix(), namespace.URI() );
        return this;
    }


    // Attributes

    public @NotNull PdmlDataWriter writeNodeAttributes ( @NotNull MutableRootOrBranchNode node ) throws IOException {

        var attributes = node.getAttributes();
        if ( attributes == null ) return this;
        writeAttributes ( attributes );
        if ( node.hasChildNodes() ) writer.write ( " " );

        return this;
    }

    public @NotNull PdmlDataWriter writeAttributes ( @NotNull MutableOrImmutableParameters<String> attributes ) throws IOException {

        var list = attributes.list();
        if ( list == null ) return this;
        return writeAttributes ( list );
    }

    public @NotNull PdmlDataWriter writeAttributes ( @NotNull List<Parameter<String>> attributes ) throws IOException {

        writer.writeAttributesStart();
        for ( Parameter<String> attribute : attributes ) {
            writer.write ( " " );
            writeAttribute ( attribute );
        }
        writer.writeAttributesEnd();

        return this;
    }

    public @NotNull PdmlDataWriter writeAttribute ( @NotNull Parameter<String> attribute ) throws IOException {

        writer.writeAttribute (
            attribute.getName(), attribute.getValue(), config.writeUnquotedAttributeValuesIfPossible() );
        return this;
    }
}
