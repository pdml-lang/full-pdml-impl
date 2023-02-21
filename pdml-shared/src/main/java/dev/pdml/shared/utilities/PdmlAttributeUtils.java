package dev.pdml.shared.utilities;

import dev.pp.basics.annotations.Nullable;

public class PdmlAttributeUtils {

    public static boolean isValidCharInUnquotedValue ( char c ) {

        if ( Character.isWhitespace ( c ) ) return false;

        return switch ( c ) {
            case ' ', '\t', '\r', '\n', '[', ']', '(', ')', '"', '\'' -> false;
            default -> true;
        };
    }

    public static boolean isInvalidCharInUnquotedValue ( char c ) {
        return ! isValidCharInUnquotedValue ( c );
    }


    public static boolean canValueBeUnquoted ( @Nullable String value ) {

        if ( value == null ) return false;

        for ( char c : value.toCharArray() ) {
            if ( isInvalidCharInUnquotedValue ( c ) ) return false;
        }
        return true;
    }
}
