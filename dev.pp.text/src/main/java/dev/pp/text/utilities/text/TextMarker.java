package dev.pp.text.utilities.text;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.string.StringConstants;

import java.util.ArrayList;
import java.util.List;

public class TextMarker {

    public static char DEFAULT_MARKER_IN_MARKER_LINE = '^';

    public static @NotNull String insertInlineMarker (
        final @NotNull String string,
        final int markerPosition,
        final @NotNull String marker ) {

        assert markerPosition >= 1;
        assert markerPosition <= string.length() + 1;

        if ( markerPosition <= 1 ) {
            return marker + string;
        } else if ( markerPosition > string.length() ) {
            return string + marker;
        } else {
            return string.substring ( 0, markerPosition - 1 ) + marker + string.substring ( markerPosition - 1 );
        }
    }

    public static @NotNull String breakSingleTextLineAndInsertMarkerLine (
        final @NotNull String textLine,
        final int markerIndex,
        final char markerChar,
        final int markerLength,
        final boolean removeIndent,
        final @Nullable Integer maxCharsInLine ) {

        String textLine_ = textLine;
        int markerIndex_ = markerIndex;
        if ( removeIndent ) {
            textLine_ = TextIndent.removeIndent ( textLine_ );
            if ( textLine_ == null ) {
                textLine_ = textLine;
            } else {
                int removedCount = textLine.length() - textLine_.length();
                if ( markerIndex < removedCount ) {
                    textLine_ = textLine;
                } else {
                    markerIndex_ = markerIndex - removedCount;
                }
            }
        }

        List<String> textLines;
        int lineIndexContainingMarkerStart = -1;
        int markerIndexInLine = -1;
        if ( maxCharsInLine == null ) {
            textLines = new ArrayList<>();
            textLines.add ( textLine_ );
            lineIndexContainingMarkerStart = 0;
            markerIndexInLine = markerIndex_;
        } else {
            textLines = TextLines.textToLinesWithMaxLineLength ( textLine_, maxCharsInLine );

            if ( markerIndex_ >= textLine_.length() ) {
                int lastLineIndex = textLines.size() - 1;
                lineIndexContainingMarkerStart = lastLineIndex;
                String lastLine = textLines.get ( lastLineIndex );
                markerIndexInLine = lastLine.length();

            } else {
                int currentLineStartIndex = 0;
                // for ( String line : textLines ) {
                for ( int currentLineIndex = 0; currentLineIndex < textLines.size(); currentLineIndex++ ) {
                    String currentLine = textLines.get ( currentLineIndex );
                    int currentLineLength = currentLine.length();
                    int currentLineEndIndex = currentLineStartIndex + currentLineLength - 1;

                    if ( markerIndex_ >= currentLineStartIndex && markerIndex_ <= currentLineEndIndex ) {
                        lineIndexContainingMarkerStart = currentLineIndex;
                        markerIndexInLine = markerIndex_ - currentLineStartIndex;
                        break;
                    }
                    currentLineStartIndex = currentLineStartIndex + currentLineLength;
                }
            }
        }

        assert lineIndexContainingMarkerStart >= 0;
        assert markerIndexInLine >= 0;

        String lineContainingMarkerStart = textLines.get ( lineIndexContainingMarkerStart );
        String markerLine = createMarkerLineForTextLine ( lineContainingMarkerStart, markerIndexInLine, markerChar, markerLength );

        if ( lineIndexContainingMarkerStart == textLines.size() - 1 ) {
            textLines.add ( markerLine );
        } else {
            textLines.add ( lineIndexContainingMarkerStart + 1, markerLine );
        }

        return TextLines.linesToText ( textLines );
    }

    public static @NotNull String appendMarkerLineToTextLine (
        final @NotNull String textLine,
        final int markerIndex,
        final char markerChar,
        final int markerLength ) {

        String makerLine = createMarkerLineForTextLine ( textLine, markerIndex, markerChar, markerLength );

        return textLine + StringConstants.OS_NEW_LINE + makerLine;
    }

    public static @NotNull String createMarkerLineForTextLine (
        final @NotNull String textLine,
        final int markerIndex,
        final char markerChar,
        final int markerLength ) {

        assert markerIndex >= 0;
        assert markerIndex <= textLine.length();

        StringBuilder markerLine = new StringBuilder();
        markerLine.append ( " ".repeat ( markerIndex ) );

        int markerLength_ = markerLength;
        int maxMarkerLength = textLine.length() - markerIndex + 1;
        if ( markerLength_ > maxMarkerLength ) markerLength_ = maxMarkerLength;
        if ( markerLength_ < 1 ) markerLength_ = 1;

        markerLine.append ( String.valueOf ( markerChar ).repeat ( markerLength_ ) );

        return markerLine.toString();
    }
}
