package dev.pp.text.utilities.string;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

public class StringAligner {

    public static @NotNull String alignOrTruncateWithEllipses (
        @Nullable String string, int width, @NotNull HTextAlign alignment ) {

        if ( width < 1 ) throw new IllegalArgumentException (
            "width (" + width + ") cannot be < 1." );

        if ( string != null && string.length() > width ) {
            return StringTruncator.truncateWithEllipses ( string, width );
        } else {
            return align ( string, width, alignment );
        }
    }

    public static @NotNull String align ( @Nullable String string, int width, @NotNull HTextAlign alignment ) {
        if ( width < 1 ) throw new IllegalArgumentException (
            "width (" + width + ") cannot be < 1.");

        if ( string == null ) return " ".repeat ( width );

        if ( string.length() > width ) throw new IllegalArgumentException (
            "string length (" + string.length() + ") cannot be greater than width (" + width + ").");

        // string = string.trim();
        if ( string.length() == width ) return string;

        int spacesCount = width - string.length();

        switch ( alignment ) {
            case LEFT:
                return string + " ".repeat ( spacesCount );
            case RIGHT:
                return " ".repeat ( spacesCount ) + string;
            case CENTER:
                int leftSpacesCount = spacesCount / 2;
                String leftSpaces = " ".repeat ( leftSpacesCount );
                String rightSpaces = " ".repeat ( spacesCount - leftSpacesCount );
                return leftSpaces + string + rightSpaces;
            default:
                throw new RuntimeException ( "Unexpected value for alignment: " + alignment );
        }
    }
}
