package dev.pdml.core.reader;

import dev.pdml.core.PDMLConstants;

public class PDMLReaderHelper {

    public static boolean isValidFirstCharOfXMLName ( char c ) {

        return ( ( c >= 'A' && c <= 'Z' )
            || ( c >= 'a' && c <= 'z' )
            || c == '_' );
    }

    public static boolean isValidCharOfXMLName ( char c ) {

        return isValidFirstCharOfXMLName ( c )
            || c >= '0' && c <= '9'
            || c == '-'
            || c == '.';
    }

    public static boolean isValidCharOfUnquotedAttributeValue ( char c ) {

        return switch ( c ) {
            // case '\\':
            case ' ', '\t', '\r', '\n', '[', ']', '(', ')', '"', '\'' -> false;
            default -> true;
        };
    }

    public static boolean isEscapeCharacter ( char c ) {

        return c == PDMLConstants.ESCAPE_CHARACTER;
    }
}
