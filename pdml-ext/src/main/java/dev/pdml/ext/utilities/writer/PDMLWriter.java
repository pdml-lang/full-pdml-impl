package dev.pdml.ext.utilities.writer;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.IOException;

public interface PDMLWriter {

    // Basics

    // string is not escaped
    PDMLWriter write ( @NotNull String string ) throws IOException;

    PDMLWriter writeLine ( @NotNull String string ) throws IOException;

    PDMLWriter writeNewLine() throws IOException;

    PDMLWriter flush() throws IOException;

    void close() throws IOException;


    // Document

    // void startDocument() throws IOException;

    // void endDocument() throws IOException;


    // Text

    PDMLWriter writeText ( @NotNull String text ) throws IOException;


    // Indent

    PDMLWriter increaseIndent();

    PDMLWriter decreaseIndent();

    PDMLWriter writeIndent() throws IOException;


    // Nodes

    PDMLWriter writeNodeStartSymbol() throws IOException;

    PDMLWriter writeNodeEndSymbol() throws IOException;

    PDMLWriter writeName ( @NotNull String name ) throws IOException;

    PDMLWriter writeName ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;

    PDMLWriter writeNameValueSeparator() throws IOException;

    PDMLWriter writeNonEmptyNodeStart ( @NotNull String name ) throws IOException;

    PDMLWriter writeNonEmptyNodeStart ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;

/*
    void writeNodeStart (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        boolean appendNameValueSeparator ) throws IOException;
*/

    /* TODO
    void writeNodeEndTag ( @NotNull String name ) throws IOException;

    void writeNodeEndTag ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;
    */

    PDMLWriter writeEmptyNode ( @NotNull String name ) throws IOException;

    PDMLWriter writeEmptyNode ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;

    PDMLWriter writeTextNode ( @NotNull String name, @NotNull String text ) throws IOException;

    PDMLWriter writeTextNode ( @Nullable String nameSpacePrefix, @NotNull String localName, @NotNull String text )
        throws IOException;

    PDMLWriter writeIndentedBlockNodeStart ( @NotNull String name ) throws IOException;

    PDMLWriter writeIndentedBlockNodeStart ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException;

    PDMLWriter writeIndentedBlockNodeEnd() throws IOException;

    /* TODO
    PDMLWriter writeDelimitedRawTextNode ( @NotNull String name, @NotNull String text )
        throws IOException;

    PDMLWriter writeDelimitedRawTextNode ( @Nullable String nameSpacePrefix, @NotNull String localName, @NotNull String text )
        throws IOException;
    */


    // Attributes

    PDMLWriter writeAttributesStart() throws IOException;

    PDMLWriter writeAttributesEnd() throws IOException;

    PDMLWriter writeAttribute ( @NotNull ASTNodeAttribute attribute ) throws IOException;

    PDMLWriter writeAttribute ( @NotNull String name, @Nullable String value ) throws IOException;

    PDMLWriter writeAttribute ( @Nullable String nameSpacePrefix, @NotNull String localName, @Nullable String value )
        throws IOException;

    // TODO boolean writeAttributesSeparator ) throws IOException;


    // Comment

    PDMLWriter writeComment ( @NotNull String text ) throws IOException;


    // Namespaces

    PDMLWriter writeNamespacesStart() throws IOException;

    PDMLWriter writeNamespacesEnd() throws IOException;

    PDMLWriter writeNamespace ( @NotNull ASTNamespace nameSpace ) throws IOException;

    PDMLWriter writeNamespace (
        @NotNull String nameSpacePrefix,
        @NotNull String URI ) throws IOException;
}
