package dev.pdml.core.utilities;

import dev.pp.text.annotations.NotNull;
import dev.pdml.core.PDMLConstants;

import java.io.IOException;
import java.io.Writer;

public class PXMLEscaper {

    // Node text

    public static boolean mustEscapeInNodeText ( char c ) {

        switch ( c ) {
            case PDMLConstants.NODE_START:
            case PDMLConstants.NODE_END:
            case PDMLConstants.ESCAPE_CHARACTER:
                return true;
            default:
                return false;
        }
    }

    public static @NotNull String escapeNodeText ( @NotNull CharSequence unescapedNodeText ) {

        StringBuilder sb = new StringBuilder();
        appendNodeText ( unescapedNodeText, sb );
        return sb.toString();
    }

    public static void appendNodeText ( @NotNull CharSequence unescapedNodeText, @NotNull StringBuilder sb ) {

        for ( int i = 0; i < unescapedNodeText.length (); i++ ) {
            appendNodeTextChar ( unescapedNodeText.charAt ( i ), sb );
        }
    }

    public static void appendNodeTextChar ( char c, @NotNull StringBuilder sb ) {

        if ( mustEscapeInNodeText ( c ) ) sb.append ( PDMLConstants.ESCAPE_CHARACTER );
        sb.append ( c );
    }

    public static void writeNodeText ( @NotNull CharSequence unescapedNodeText, Writer writer ) throws IOException {

        for ( int i = 0; i < unescapedNodeText.length (); i++ ) {
            writeNodeTextChar ( unescapedNodeText.charAt ( i ), writer );
        }
    }

    public static void writeNodeTextChar ( char c, @NotNull Writer writer ) throws IOException {

        if ( mustEscapeInNodeText ( c ) ) writer.write ( PDMLConstants.ESCAPE_CHARACTER );
        writer.write ( c );
    }


    // Double-quoted attribute value

    public static boolean mustEscapeInDoubleQuotedAttributeValue ( char c ) {

        switch ( c ) {
            case PDMLConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE:
            case PDMLConstants.ESCAPE_CHARACTER:
                return true;
            default:
                return false;
        }
    }

    public static @NotNull String escapeDoubleQuotedAttributeValue ( @NotNull CharSequence unescapedValue ) {

        StringBuilder sb = new StringBuilder();
        appendDoubleQuotedAttributeValue ( unescapedValue, sb );
        return sb.toString();
    }

    public static void appendDoubleQuotedAttributeValue ( @NotNull CharSequence unescapedValue, @NotNull StringBuilder sb ) {

        for ( int i = 0; i < unescapedValue.length (); i++ ) {
            appendDoubleQuotedAttributeValueChar ( unescapedValue.charAt ( i ), sb );
        }
    }

    public static void appendDoubleQuotedAttributeValueChar ( char c, @NotNull StringBuilder sb ) {

        if ( mustEscapeInDoubleQuotedAttributeValue ( c ) ) sb.append ( PDMLConstants.ESCAPE_CHARACTER );
        sb.append ( c );
    }

    public static void writeDoubleQuotedAttributeValue ( @NotNull CharSequence unescapedValue, Writer writer )
        throws IOException {

        for ( int i = 0; i < unescapedValue.length (); i++ ) {
            writeDoubleQuotedAttributeValueChar ( unescapedValue.charAt ( i ), writer );
        }
    }

    public static void writeDoubleQuotedAttributeValueChar ( char c, @NotNull Writer writer ) throws IOException {

        if ( mustEscapeInDoubleQuotedAttributeValue ( c ) ) writer.write ( PDMLConstants.ESCAPE_CHARACTER );
        writer.write ( c );
    }
}
