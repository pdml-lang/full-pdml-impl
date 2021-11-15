package dev.pp.text.utilities.text;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.character.CharChecks;
import dev.pp.text.utilities.string.StringFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextIndent {

    public static @NotNull String removeSmallestIndent ( @NotNull String lines, boolean ignoreEmptyLines ) {

        return TextLines.linesToText ( removeSmallestIndent ( TextLines.textToLines ( lines ), ignoreEmptyLines ) );
    }

    public static @NotNull List<String> removeSmallestIndent ( @NotNull List<String> lines, boolean ignoreEmptyLines ) {

        int smallestNonIndentIndex = getSmallestNonIndentIndex ( lines, ignoreEmptyLines );

        if ( smallestNonIndentIndex > 0 ) {
            return lines.stream()
                .map ( line -> {
                    if ( line.length() > smallestNonIndentIndex ) {
                        return removeIndent ( line, smallestNonIndentIndex );
                    } else {
                        return "";
                    }
                })
                .collect ( Collectors.toList() );
        } else {
            return lines;
        }
    }

    public static int getSmallestNonIndentIndex ( @NotNull String lines, boolean ignoreEmptyLines ) {

        return getSmallestNonIndentIndex ( TextLines.textToLines ( lines ), ignoreEmptyLines );
    }

    public static int getSmallestNonIndentIndex ( @NotNull List<String> lines, boolean ignoreEmptyLines ) {

        int result = Integer.MAX_VALUE;
        for ( String line : lines ) {
            if ( line == null || line.isEmpty() ) {
                if ( ignoreEmptyLines ) {
                    continue;
                } else {
                    return 0;
                }
            }
            int index = firstNonIndentIndex ( line );
            if ( index == 0 ) return 0;
            if ( index == -1 ) index = line.length();
            if ( index < result ) result = index;
        }
        return result;
    }

    public static int firstNonIndentIndex ( @NotNull String string ) {

        return StringFinder.findFirstIndex ( string, CharChecks::isNotSpaceOrTab );
    }

    public static @Nullable String removeIndent ( @NotNull String string ) {

        return removeIndent ( string, firstNonIndentIndex ( string ) );
    }
/*
    public static @Nullable String removeIndent ( @NotNull String string, @NotNull String indent ) {

        assert string.length() >= indent.length();

        removeIndent ( string, indent.length() - 1 );
    }
*/

    public static @Nullable String removeIndent ( @NotNull String string, int firstNonIndentIndex ) {

        assert string.length() > firstNonIndentIndex;

        if ( firstNonIndentIndex == -1 ) {
            return null;
        } else if ( firstNonIndentIndex == 0 ) {
            return string;
        } else {
            return string.substring ( firstNonIndentIndex );
        }
    }
}
