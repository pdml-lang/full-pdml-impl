package dev.pdml.writer;

import dev.pdml.shared.utilities.PdmlAttributeUtils;
import dev.pdml.shared.utilities.PdmlEscaper;
import dev.pdml.shared.constants.CorePdmlConstants;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.utilities.text.BasicDocumentWriter;

import java.io.IOException;
import java.io.Writer;

public class PdmlWriter extends BasicDocumentWriter {


    public PdmlWriter ( @NotNull Writer writer, @NotNull String singleIndent ) {
        super ( writer, singleIndent );
    }

    public PdmlWriter ( @NotNull Writer writer ) {
        super ( writer );
    }


    // Basics

    @Override
    public PdmlWriter write ( @NotNull String string ) throws IOException {
        super.write ( string );
        return this;
    }

    @Override
    public PdmlWriter writeNullable ( @Nullable String string ) throws IOException {
        super.writeNullable ( string );
        return this;
    }

    @Override
    public PdmlWriter writeLine ( @NotNull String string ) throws IOException {
        super.writeLine ( string );
        return this;
    }

    @Override
    public PdmlWriter writeNewLine() throws IOException {
        super.writeNewLine();
        return this;
    }

    @Override
    public PdmlWriter flush() throws IOException {
        super.flush();
        return this;
    }

/*
    public void close() throws IOException {
        writer.close();
    }
*/


    // Text

    public @NotNull String escapeText ( @NotNull String text ) {
        return PdmlEscaper.escapeNodeText ( text );
    }

    @Override
    public PdmlWriter escapeAndWriteText ( @NotNull String text ) throws IOException {
        super.escapeAndWriteText ( text );
        return this;
    }

    @Override
    public PdmlWriter escapeAndWriteNullableText ( @Nullable String text ) throws IOException {
        super.escapeAndWriteNullableText ( text );
        return this;
    }

    @Override
    public PdmlWriter writeText ( @NotNull String text, boolean escapeText ) throws IOException {
        super.writeText ( text, escapeText );
        return this;
    }


    // Indent

    @Override
    public PdmlWriter increaseIndent() {
        super.increaseIndent();
        return this;
    }

    @Override
    public PdmlWriter decreaseIndent() {
        super.decreaseIndent();
        return this;
    }

    @Override
    public PdmlWriter writeIndent() throws IOException {
        super.writeIndent();
        return this;
    }


    // Nodes

    public PdmlWriter writeNodeStartSymbol() throws IOException {
        write ( CorePdmlConstants.NODE_START );
        return this;
    }

    public PdmlWriter writeNodeEndSymbol() throws IOException {
        write ( CorePdmlConstants.NODE_END );
        return this;
    }

    public PdmlWriter writeName ( @NotNull String name ) throws IOException {
        write ( name );
        return this;
    }

    public PdmlWriter writeName ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        if ( nameSpacePrefix != null && ! nameSpacePrefix.isEmpty() ) {
            write ( nameSpacePrefix );
            write ( PdmlExtensionsConstants.NAMESPACE_PREFIX_NAME_SEPARATOR );
        }

        write ( localName );

        return this;
    }

    public PdmlWriter writeNameValueSeparator() throws IOException {
        write ( CorePdmlConstants.NAME_VALUE_SEPARATOR );
        return this;
    }

    public PdmlWriter writeNonEmptyNodeStart ( @NotNull String name ) throws IOException {
        writeNodeStart ( null, name, true );
        return this;
    }

    public PdmlWriter writeNonEmptyNodeStart (
        @Nullable String nameSpacePrefix,
        @NotNull String localName ) throws IOException {

        writeNodeStart ( nameSpacePrefix, localName, true );
        return this;
    }

    public PdmlWriter writeEmptyNode ( @NotNull String name ) throws IOException {
        writeEmptyNode ( null, name );
        return this;
    }

    public PdmlWriter writeEmptyNode ( @Nullable String nameSpacePrefix, @NotNull String localName ) throws IOException {

        writeNodeStart ( nameSpacePrefix, localName, false );
        writeNodeEndSymbol();
        return this;
    }

    public PdmlWriter writeTextNode ( @NotNull String name, @NotNull String text ) throws IOException {
        writeTextNode ( null, name, text );
        return this;
    }

    public PdmlWriter writeTextNode ( @Nullable String nameSpacePrefix, @NotNull String localName, @NotNull String text )
        throws IOException {

        writeNonEmptyNodeStart( nameSpacePrefix, localName );
        escapeAndWriteText ( text );
        writeNodeEndSymbol();

        return this;
    }

    public PdmlWriter writeIndentedBlockNodeStart ( @NotNull String name ) throws IOException {
        writeIndentedBlockNodeStart ( null, name );
        return this;
    }

    public PdmlWriter writeIndentedBlockNodeStart ( @Nullable String nameSpacePrefix, @NotNull String localName )
        throws IOException {

        writeIndent();
        writeNodeStart ( nameSpacePrefix, localName, false );
        writeNewLine();
        increaseIndent();

        return this;
    }

    public PdmlWriter writeIndentedBlockNodeEnd() throws IOException {

        decreaseIndent();
        writeIndent();
        writeNodeEndSymbol();
        writeNewLine();

        return this;
    }


    // Attributes

    public PdmlWriter writeAttributesStart() throws IOException {

        write ( PdmlExtensionsConstants.ATTRIBUTES_START );
        return this;
    }

    public PdmlWriter writeAttributesEnd() throws IOException {

        write ( PdmlExtensionsConstants.ATTRIBUTES_END );
        return this;
    }

    public PdmlWriter writeAttribute (
        @NotNull String name,
        @Nullable String value,
        boolean unquotedValueIfPossible ) throws IOException {

        writeAttribute ( null, name, value, unquotedValueIfPossible );
        return this;
    }

    public PdmlWriter writeAttribute (
        @Nullable String nameSpacePrefix,
        @NotNull String localName,
        @Nullable String value,
        boolean unquotedValueIfPossible ) throws IOException {

        writeName ( nameSpacePrefix, localName );
        write ( PdmlExtensionsConstants.ATTRIBUTE_ASSIGN );
        writeAttributeValue ( value, unquotedValueIfPossible );

        return this;
    }

    public PdmlWriter writeAttributeValue ( @Nullable String value ) throws IOException {
        return writeDoubleQuotedAttributeValue ( value );
    }

    public PdmlWriter writeAttributeValue ( @Nullable String value, boolean unquotedIfPossible ) throws IOException {

        if ( unquotedIfPossible && PdmlAttributeUtils.canValueBeUnquoted ( value ) ) {
            return writeUnquotedAttributeValue ( value );
        } else {
            return writeDoubleQuotedAttributeValue ( value );
        }
    }

    public PdmlWriter writeDoubleQuotedAttributeValue ( @Nullable String value ) throws IOException {

        write ( PdmlExtensionsConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE );
        if ( value != null ) {
            PdmlEscaper.writeDoubleQuotedAttributeValue ( value, writer );
        }
        write ( PdmlExtensionsConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE );

        return this;
    }

    public PdmlWriter writeUnquotedAttributeValue ( @NotNull String value ) throws IOException {
        write ( value );
        return this;
    }


    // Comment

    public PdmlWriter writeComment ( @NotNull String text ) throws IOException {

        write ( PdmlExtensionsConstants.COMMENT_START );
        // writeText ( text );
        write ( text );
        write ( PdmlExtensionsConstants.COMMENT_END );

        return this;
    }


    // Namespaces

    public PdmlWriter writeNamespacesStart() throws IOException {

        write ( PdmlExtensionsConstants.NAMESPACE_DECLARATION_START );
        return this;
    }

    public PdmlWriter writeNamespacesEnd() throws IOException {

        write ( PdmlExtensionsConstants.NAMESPACE_DECLARATION_SEPARATOR );
        write ( PdmlExtensionsConstants.NAMESPACE_DECLARATION_END );
        write ( PdmlExtensionsConstants.NAMESPACE_DECLARATION_SEPARATOR );

        return this;
    }

    public PdmlWriter writeNamespace (
        @NotNull String nameSpacePrefix,
        @NotNull String URI ) throws IOException {

        writeAttribute ( null, nameSpacePrefix, URI, false );
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
