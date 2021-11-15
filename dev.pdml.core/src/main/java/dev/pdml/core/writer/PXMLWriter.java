package dev.pdml.core.writer;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.io.IOException;

public interface PXMLWriter {

    // string is not escaped
    void write ( @NotNull String string ) throws IOException;

    void flush() throws IOException;
    // void close() throws IOException;


    // Document

    void startDocument() throws IOException;
    void endDocument() throws IOException;


    // Nodes

    void writeEmptyNode ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;

    void writeTextNode (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        @NotNull String text ) throws IOException;

    void writeNonEmptyNodeStart ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;

/*
    void writeNodeStart (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        boolean appendNameValueSeparator ) throws IOException;
*/

    void writeNodeEndSymbol() throws IOException;

    void writeNodeEndTag ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;


    // Namespaces

    void writeNamespacesStart() throws IOException;

    void writeNamespacesEnd() throws IOException;

    void writeNamespace ( @NotNull ASTNamespace nameSpace ) throws IOException;

    void writeNamespace (
        @NotNull String nameSpacePrefix,
        @NotNull String URI ) throws IOException;


    // Attributes

    void writeAttributesStart() throws IOException;

    void writeAttributesEnd() throws IOException;

    void writeAttribute ( @NotNull ASTNodeAttribute attribute ) throws IOException;

    void writeAttribute (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        @Nullable String value ) throws IOException;
        // boolean appendAttributesSeparator ) throws IOException;


    // Text

    void escapeAndWriteText ( @NotNull String text ) throws IOException;


    // Comment

    void writeComment ( @NotNull String text ) throws IOException;
}
