package dev.pdml.shared.utilities;

public class PdmlNameUtils {

    public static boolean isValidFirstCharOfName ( char c ) {

        return ( ( c >= 'A' && c <= 'Z' )
            || ( c >= 'a' && c <= 'z' )
            || c == '_' );
    }

    public static boolean isValidCharOfName ( char c ) {

        return isValidFirstCharOfName ( c )
            || c >= '0' && c <= '9'
            || c == '-'
            || c == '.';
    }
}
