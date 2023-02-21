package dev.pdml.shared.utilities;

import dev.pdml.shared.constants.CorePdmlConstants;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pp.basics.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class PdmlEscaper {


    // Node text

    public static boolean mustEscapeInNodeText ( char c ) {

        return switch ( c ) {
            case CorePdmlConstants.NODE_START, CorePdmlConstants.NODE_END, CorePdmlConstants.ESCAPE_CHARACTER -> true;
            default -> false;
        };
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

        if ( mustEscapeInNodeText ( c ) ) sb.append ( CorePdmlConstants.ESCAPE_CHARACTER );
        sb.append ( c );
    }

    public static void writeNodeText ( @NotNull CharSequence unescapedNodeText, Writer writer ) throws IOException {

        for ( int i = 0; i < unescapedNodeText.length (); i++ ) {
            writeNodeTextChar ( unescapedNodeText.charAt ( i ), writer );
        }
    }

    public static void writeNodeTextChar ( char c, @NotNull Writer writer ) throws IOException {

        if ( mustEscapeInNodeText ( c ) ) writer.write ( CorePdmlConstants.ESCAPE_CHARACTER );
        writer.write ( c );
    }


    // Double-quoted attribute value

    public static boolean mustEscapeInDoubleQuotedAttributeValue ( char c ) {

        return switch ( c ) {
            case PdmlExtensionsConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE, CorePdmlConstants.ESCAPE_CHARACTER -> true;
            default -> false;
        };
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

        if ( mustEscapeInDoubleQuotedAttributeValue ( c ) ) sb.append ( CorePdmlConstants.ESCAPE_CHARACTER );
        sb.append ( c );
    }

    public static void writeDoubleQuotedAttributeValue ( @NotNull CharSequence unescapedValue, Writer writer )
        throws IOException {

        for ( int i = 0; i < unescapedValue.length (); i++ ) {
            writeDoubleQuotedAttributeValueChar ( unescapedValue.charAt ( i ), writer );
        }
    }

    public static void writeDoubleQuotedAttributeValueChar ( char c, @NotNull Writer writer ) throws IOException {

        if ( mustEscapeInDoubleQuotedAttributeValue ( c ) ) writer.write ( CorePdmlConstants.ESCAPE_CHARACTER );
        writer.write ( c );
    }


    // Other

    public static boolean isEscapeCharacter ( char c ) {
        return c == CorePdmlConstants.ESCAPE_CHARACTER;
    }
}
