package dev.pdml.ext.utilities.writer;

import java.io.IOException;
import java.io.Writer;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.ext.extensions.utilities.PDMLEscaper;

public class PDMLWriterImpl implements PDMLWriter {

    private final @NotNull Writer writer;
    private final @NotNull StringBuilder currentIndent;
    private final @NotNull String singleIndent;



    public PDMLWriterImpl ( @NotNull Writer writer ) {
        this.writer = writer;
        this.currentIndent = new StringBuilder();
        this.singleIndent = "    ";
    }


    // Basics

    public PDMLWriter write ( @NotNull String string ) throws IOException {
        writer.write ( string );
        return this;
    }

    public PDMLWriter writeLine ( @NotNull String string ) throws IOException {
        write ( string );
        writeNewLine();
        return this;
    }

    public PDMLWriter writeNewLine() throws IOException {
        write ( StringConstants.OS_NEW_LINE );
        return this;
    }

    public PDMLWriter flush() throws IOException {
        writer.flush();
        return this;
    }

    public void close() throws IOException {
        writer.close();
    }


    /*
    // Document

    public void startDocument() {
        // do nothing
    }

    public void endDocument() throws IOException {

        writeNewLine();
        // writer.close();
    }
    */


    // Text

    public PDMLWriter writeText ( @NotNull String text ) throws IOException {

        PDMLEscaper.writeNodeText ( text, writer );
        return this;
    }


    // Indent

    public PDMLWriter increaseIndent() {
        currentIndent.append ( singleIndent );
        return this;
    }

    public PDMLWriter decreaseIndent() {
        if ( currentIndent.isEmpty() )
            throw new RuntimeException ( "Illegal to call 'decreaseIndent', because the indent is zero already." );

        int length = currentIndent.length();
        currentIndent.delete ( length - singleIndent.length(), length );
        return this;
    }

    public PDMLWriter writeIndent() throws IOException{
        if ( ! currentIndent.isEmpty() )
            write ( currentIndent.toString() );
        return this;
    }


    // Nodes

    public PDMLWriter writeNodeStartSymbol() throws IOException {
        writer.write ( PDMLConstants.NODE_START );
        return this;
    }

    public PDMLWriter writeNodeEndSymbol() throws IOException {
        writer.write ( PDMLConstants.NODE_END );
        return this;
    }

    public PDMLWriter writeName ( @NotNull String name ) throws IOException {
        writer.write ( name );
        return this;
    }

    public PDMLWriter writeName ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        if ( nameSpacePrefix != null && ! nameSpacePrefix.isEmpty() ) {
            writer.write ( nameSpacePrefix );
            writer.write ( PDMLConstants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        writer.write ( localName );

        return this;
    }

    public PDMLWriter writeNameValueSeparator() throws IOException {
        writer.write ( PDMLConstants.NAME_VALUE_SEPARATOR );
        return this;
    }

    public PDMLWriter writeNonEmptyNodeStart ( @NotNull String name ) throws IOException {
        writeNodeStart ( null, name, true );
        return this;
    }

    public PDMLWriter writeNonEmptyNodeStart (
        @Nullable String nameSpacePrefix,
        @NotNull String localName ) throws IOException {

        writeNodeStart ( nameSpacePrefix, localName, true );
        return this;
    }

    /*
    public PDMLWriter writeNodeEndTag ( @NotNull String name ) throws IOException {
    }

    public PDMLWriter writeNodeEndTag ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        writeNodeStartSymbol();
        writer.write ( PDMLConstants.NODE_END_TAG_SYMBOL );
        writeName ( nameSpacePrefix, localName );
        writeNodeEndSymbol();
    }
    */

    public PDMLWriter writeEmptyNode ( @NotNull String name ) throws IOException {
        writeEmptyNode ( null, name );
        return this;
    }

    public PDMLWriter writeEmptyNode ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        writeNodeStart ( nameSpacePrefix, localName, false );
        writeNodeEndSymbol();
        return this;
    }

    public PDMLWriter writeTextNode ( @NotNull String name, @NotNull String text ) throws IOException {
        writeTextNode ( null, name, text );
        return this;
    }

    public PDMLWriter writeTextNode ( @Nullable String nameSpacePrefix, @NotNull String localName, @NotNull String text )
        throws IOException {

        writeNonEmptyNodeStart( nameSpacePrefix, localName );
        writeText ( text );
        writeNodeEndSymbol();

        return this;
    }

    public PDMLWriter writeIndentedBlockNodeStart ( @NotNull String name ) throws IOException {
        writeIndentedBlockNodeStart ( null, name );
        return this;
    }

    public PDMLWriter writeIndentedBlockNodeStart ( @Nullable String nameSpacePrefix, @NotNull String localName )
        throws IOException {

        writeIndent();
        writeNodeStart ( nameSpacePrefix, localName, false );
        writeNewLine();
        increaseIndent();

        return this;
    }

    public PDMLWriter writeIndentedBlockNodeEnd() throws IOException {

        decreaseIndent();
        writeIndent();
        writeNodeEndSymbol();
        writeNewLine();

        return this;
    }


    // Attributes

    public PDMLWriter writeAttributesStart() throws IOException {

        writer.write ( PDMLConstants.ATTRIBUTES_START );
        // writer.write ( ' ' );
        return this;
    }

    public PDMLWriter writeAttributesEnd() throws IOException {

        // TODO? should be enabled/disabled by a parameter of this writer's config
        writer.write ( PDMLConstants.ATTRIBUTES_SEPARATOR );
        writer.write ( PDMLConstants.ATTRIBUTES_END );
        writer.write ( PDMLConstants.ATTRIBUTES_SEPARATOR );
        return this;
    }

    public PDMLWriter writeAttribute ( @NotNull ASTNodeAttribute attribute ) throws IOException {
        writeAttribute ( attribute.getNamespacePrefixText(), attribute.getLocalNameText(), attribute.getValueText() );
        return this;
    }

    public PDMLWriter writeAttribute ( @NotNull String name, @Nullable String value ) throws IOException {
        writeAttribute ( null, name, value );
        return this;
    }

    public PDMLWriter writeAttribute ( @Nullable String nameSpacePrefix, @NotNull String localName, @Nullable String value )
        throws IOException {

        writer.write ( PDMLConstants.ATTRIBUTES_SEPARATOR );

        writeName ( nameSpacePrefix, localName );

        writer.write ( PDMLConstants.ATTRIBUTE_ASSIGN );

        writeDoubleQuotedAttributeValue ( value );

/*
        if  ( appendAttributesSeparator ) {
            writer.write ( Constants.ATTRIBUTES_SEPARATOR );
        }
*/
        return this;
    }

    public PDMLWriter writeDoubleQuotedAttributeValue ( @Nullable String value ) throws IOException {

        writer.write ( PDMLConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE );

        if ( value != null ) {
            PDMLEscaper.writeDoubleQuotedAttributeValue ( value, writer );
        }

        writer.write ( PDMLConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE );
        return this;
    }


    // Comment

    public PDMLWriter writeComment ( @NotNull String text ) throws IOException {

        writer.write ( PDMLConstants.COMMENT_START );
        // writeText ( text );
        writer.write ( text );
        writer.write ( PDMLConstants.COMMENT_END );

        return this;
    }


    // Namespaces

    public PDMLWriter writeNamespacesStart() throws IOException {

        writer.write ( PDMLConstants.NAMESPACE_DECLARATION_START );
        return this;
    }

    public PDMLWriter writeNamespacesEnd() throws IOException {

        writer.write ( PDMLConstants.NAMESPACE_DECLARATION_SEPARATOR );
        writer.write ( PDMLConstants.NAMESPACE_DECLARATION_END );
        writer.write ( PDMLConstants.NAMESPACE_DECLARATION_SEPARATOR );

        return this;
    }

    public PDMLWriter writeNamespace ( @NotNull ASTNamespace nameSpace ) throws IOException {

        writeNamespace ( nameSpace.getPrefixText(), nameSpace.getURIToken ().toString() );
        return this;
    }

    public PDMLWriter writeNamespace (
        @NotNull String nameSpacePrefix,
        @NotNull String URI ) throws IOException {

        writeAttribute ( null, nameSpacePrefix, URI );
        return this;
    }


    // Private helpers

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
}
