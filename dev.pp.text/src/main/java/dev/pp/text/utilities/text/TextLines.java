package dev.pp.text.utilities.text;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.string.HTextAlign;
import dev.pp.text.utilities.string.StringAligner;
import dev.pp.text.utilities.string.StringConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextLines {

    public static @NotNull List<String> textToLines ( @NotNull String text ) {

        return text.lines().collect ( Collectors.toList() );
    }

    public static @NotNull String linesToText ( @NotNull List<String> lines, @NotNull String lineSeparator ) {

        return String.join ( lineSeparator, lines );
    }

    public static @NotNull String linesToText ( @NotNull List<String> lines ) {

        return linesToText ( lines, StringConstants.OS_NEW_LINE );
    }

    public static @NotNull String textToTextWithMaxLineLength ( @NotNull String text, int maxLineLength ) {

        return linesToText ( textToLinesWithMaxLineLength ( text, maxLineLength ) );
    }

    public static @NotNull List<String> textToLinesWithMaxLineLength ( @NotNull String text, int maxLineLength ) {

        List<String> textLines = textToLines ( text );
        return breakLongLines ( textLines, maxLineLength );
    }

    public static @NotNull <T> List<String> valueToAlignedTextLines (
        @Nullable T value,
        @NotNull Function<T, String> valueToStringConverter,
        int width,
        HTextAlign alignment,
        @Nullable Integer maxLines ) {

        String text = value == null ? "" : valueToStringConverter.apply ( value );
        if ( text == null ) text = "";

        // TODO should use textToStreamWithMaxLineLength
        List<String> unalignedLines = textToLinesWithMaxLineLength ( text, width );
        Stream<String> stream = unalignedLines.stream();

        if ( maxLines != null ) stream = stream.limit ( maxLines );

        return stream.map (
            (line) -> StringAligner.align ( line, width, alignment )
        ).collect( Collectors.toList() );
    }

    public static @NotNull List<String> breakLongLines ( List<String> lines, int maxLineLength ) {

        List<String> result = new ArrayList<>();
        for ( String line : lines ) {
            if ( line.length() > maxLineLength ) {
                addTooLongLine ( line, result, maxLineLength );
            } else {
                result.add ( line );
            }
        }
        return result;
    }

    private static void addTooLongLine ( @NotNull String tooLongLine, @NotNull List<String> lines, int maxLineLength ) {

        assert tooLongLine.length() > maxLineLength;

        String remaining = tooLongLine;
        while ( remaining != null ) {
            if ( remaining.length() > maxLineLength ) {
                String thisLine = firstLineOfTooLongLine ( remaining, maxLineLength );
                remaining = remaining.substring ( thisLine.length() );
                lines.add ( thisLine );
            } else {
                lines.add ( remaining );
                remaining = null;
            }
        }
    }

    private static @NotNull String firstLineOfTooLongLine ( @NotNull String tooLongLine, int maxLineLength ) {

        assert tooLongLine.length() > maxLineLength;

        if ( tooLongLine.charAt ( maxLineLength ) == ' ' ) return tooLongLine.substring ( 0, maxLineLength );

        String maxLine = tooLongLine.substring ( 0, maxLineLength );

        int lastSpaceCharIndex = maxLine.lastIndexOf ( ' ' );

        if ( lastSpaceCharIndex <= 0 ) {
            return maxLine;
        } else {
            return tooLongLine.substring ( 0, lastSpaceCharIndex + 1 );
        }
    }

    public static @NotNull String getNthLine ( @NotNull String string, long n ) throws IOException {

        return getNthLineInReader ( new BufferedReader ( new StringReader ( string ) ), n );
    }

    public static @NotNull String getNthLineInReader ( @NotNull BufferedReader reader, long n ) throws IOException {

        assert n >= 1 : "n = " + n + " is invalid because n must be >= 1";

        String result = null;
        String currentLine;
        int currentLineNumber = 1;

        while ( (currentLine = reader.readLine()) != null ) { // empty string for empty line
            if ( currentLineNumber == n ) {
                result = currentLine;
                break;
            }
            currentLineNumber ++;
        }

        assert result != null :
            "n = " + n + " is invalid because n must be <= number of lines in file (" + currentLineNumber + ").";

        // if ( result == null && n == currentLineNumber + 1 ) result = currentLine;

        return result;
    }
}
