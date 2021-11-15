package dev.pp.text.utilities.string;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.utilities.character.CharPredicate;

public class StringFinder {

    public static int findFirstIndex ( @NotNull String string, CharPredicate predicate ) {

        for ( int i = 0; i < string.length(); i++ ) {
            char c = string.charAt ( i );
            if ( predicate.accept ( c ) ) {
                return i;
            }
        }
        return -1;
    }
}
