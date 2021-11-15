package dev.pdml.core.writer;

import dev.pdml.core.Constants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.core.utilities.PXMLEscaper;

import java.io.IOException;
import java.io.Writer;

public class DefaultPXMLWriter implements PXMLWriter {

    private final @NotNull Writer writer;


    public DefaultPXMLWriter ( @NotNull Writer writer ) {
        this.writer = writer;
    }


    public void write ( @NotNull String string ) throws IOException {
        writer.write ( string );
    }

    public void flush() throws IOException {
        writer.flush();
    }


    // Document

    public void startDocument() {
        // do nothing
    }

    public void endDocument() throws IOException {

        // write new line at end of document
        write ( Constants.NEW_LINE );
        writer.close();
    }


    // Nodes

    public void writeEmptyNode ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        writeNodeStart( nameSpacePrefix, localName, false );
        writeNodeEndSymbol();
    }

    public void writeTextNode (
        @Nullable String nameSpacePrefix, @NotNull String localName, @NotNull String text ) throws IOException {

        writeNonEmptyNodeStart( nameSpacePrefix, localName );
        escapeAndWriteText ( text );
        writeNodeEndSymbol();
    }

    public void writeNonEmptyNodeStart (
        @Nullable String nameSpacePrefix,
        @NotNull String localName ) throws IOException {

        writeNodeStart ( nameSpacePrefix, localName, true );
    }

    private void writeNodeStart (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        boolean appendNameValueSeparator ) throws IOException {

        writeNodeStartSymbol();
        writeName ( nameSpacePrefix, localName );
        if ( appendNameValueSeparator ) {
            writeNameValueSeparator();
        }
    }

    public void writeNodeEndSymbol() throws IOException {

        writer.write ( Constants.NODE_END );
    }

    public void writeNodeEndTag ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        writeNodeStartSymbol();
        writer.write ( Constants.NODE_END_TAG_SYMBOL );
        writeName ( nameSpacePrefix, localName );
        writeNodeEndSymbol();
    }


    // Namespaces

    public void writeNamespacesStart() throws IOException {

        writer.write ( Constants.NAMESPACE_DECLARATION_START );
    }

    public void writeNamespacesEnd() throws IOException {

        writer.write ( Constants.NAMESPACE_DECLARATION_SEPARATOR );
        writer.write ( Constants.NAMESPACE_DECLARATION_END );
        writer.write ( Constants.NAMESPACE_DECLARATION_SEPARATOR );
    }

    public void writeNamespace ( @NotNull ASTNamespace nameSpace ) throws IOException {

        writeNamespace ( nameSpace.getPrefixText(), nameSpace.getURIToken ().toString() );
    }

    public void writeNamespace (
        @NotNull String nameSpacePrefix,
        @NotNull String URI ) throws IOException {

        writeAttribute ( null, nameSpacePrefix, URI );
    }


    // Attributes

    public void writeAttributesStart() throws IOException {

        writer.write ( Constants.ATTRIBUTES_START );
        // writer.write ( ' ' );
    }

    public void writeAttributesEnd() throws IOException {

        // TODO? should be enabled/disabled by a parameter of this writer's config
        writer.write ( Constants.ATTRIBUTES_SEPARATOR );
        writer.write ( Constants.ATTRIBUTES_END );
        writer.write ( Constants.ATTRIBUTES_SEPARATOR );
    }

    public void writeAttribute ( @NotNull ASTNodeAttribute attribute ) throws IOException {

        writeAttribute ( attribute.getNamespacePrefixText(), attribute.getLocalNameText(), attribute.getValueText() );
    }

    public void writeAttribute (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        @Nullable String value ) throws IOException {

        writer.write ( Constants.ATTRIBUTES_SEPARATOR );

        writeName ( nameSpacePrefix, localName );

        writer.write ( Constants.ATTRIBUTE_ASSIGN );

        writeDoubleQuotedAttributeValue ( value );

/*
        if  ( appendAttributesSeparator ) {
            writer.write ( Constants.ATTRIBUTES_SEPARATOR );
        }
*/
    }

    public void writeDoubleQuotedAttributeValue ( @Nullable String value ) throws IOException {

        writer.write ( Constants.ATTRIBUTE_VALUE_DOUBLE_QUOTE );

        if ( value != null ) {
            PXMLEscaper.writeDoubleQuotedAttributeValue ( value, writer );
        }

        writer.write ( Constants.ATTRIBUTE_VALUE_DOUBLE_QUOTE );
    }


    // Text

    public void escapeAndWriteText ( @NotNull String text ) throws IOException {

        PXMLEscaper.writeNodeText ( text, writer );
    }


    // Comment

    public void writeComment ( @NotNull String text ) throws IOException {

        writer.write ( Constants.COMMENT_START );
        escapeAndWriteText ( text );
        writer.write ( Constants.COMMENT_END );
    }


    private void writeNodeStartSymbol() throws IOException {

        writer.write ( Constants.NODE_START );
    }

    private void writeName ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        if ( nameSpacePrefix != null && ! nameSpacePrefix.isEmpty() ) {
            writer.write ( nameSpacePrefix );
            writer.write ( Constants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        writer.write ( localName );
    }

    private void writeNameValueSeparator() throws IOException {

        writer.write ( Constants.NAME_VALUE_SEPARATOR );
    }
}
