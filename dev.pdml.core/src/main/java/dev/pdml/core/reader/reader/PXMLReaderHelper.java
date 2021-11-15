package dev.pdml.core.reader.reader;

import dev.pdml.core.Constants;

public class PXMLReaderHelper {

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

        switch ( c ) {
            case ' ':
            case '\t':
            case '\r':
            case '\n':
            case '[':
            case ']':
            case '(':
            case ')':
            // case '\\':
            case '"':
            case '\'':
                return false;
            default:
                return true;
        }
    }

    public static boolean isEscapeCharacter ( char c ) {

        return c == Constants.ESCAPE_CHARACTER;
    }
}
