package dev.pp.text.reader;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.character.CharConsumer;
import dev.pp.text.utilities.character.CharPredicate;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.resource.URL_TextResource;
import dev.pp.text.utilities.string.StringConstants;
import dev.pp.text.location.TextLocation;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.URLUtilities;

import java.io.*;
import java.net.URL;
import java.util.NoSuchElementException;

public class DefaultCharReader implements CharReader {

    private final @NotNull Reader reader;

    private boolean hasChar;
    private char currentChar;

    private final @Nullable TextResource resource;
    private int nextLineNumber;
    private int nextColumnNumber;

    private boolean hasNextAtMark;
    private char nextCharAtMark;
    private int nextLineNumberAtMark;
    private int nextColumnNumberAtMark;


    // constructors

    public DefaultCharReader ( @NotNull Reader reader, @Nullable TextResource resource ) throws IOException {

        Reader readerWithMarkSupport = reader.markSupported() ? reader : new BufferedReader ( reader );
        this.reader = readerWithMarkSupport;

        this.hasChar = true;
        this.currentChar = 0;

        this.resource = resource;
        this.nextLineNumber = 1;
        this.nextColumnNumber = 0;

        advance();
    }

    public DefaultCharReader ( @NotNull File file ) throws IOException {

        this ( FileUtilities.getUTF8FileReader ( file ), new File_TextResource ( file ) );
    }

    public DefaultCharReader ( @NotNull URL url ) throws IOException {

        this ( URLUtilities.getUTF8URLReader ( url ), new URL_TextResource ( url ) );
    }

    public DefaultCharReader ( @NotNull String string ) throws IOException {

        this ( new StringReader ( string ), new String_TextResource ( string ) );
    }


    public boolean hasChar() { return hasChar; }

    // public char currentChar() { return currentChar; }

    public char currentChar() {

        // assert checkHasChar();

        return currentChar;
    }

    private boolean checkHasChar() {

        if ( hasChar ) {
            return true;
        } else {
            throw new NoSuchElementException (
                "There are no more characters to read at position" + StringConstants.OS_NEW_LINE + currentLocation () );
        }
    }


    // advance

    public void advance() throws IOException {

        assert checkHasChar();

        if ( currentChar == '\n' ) {
            nextLineNumber += 1;
            nextColumnNumber = 1;
        } else {
            nextColumnNumber += 1;
        }

        int nextInt = reader.read();
        if ( nextInt == -1 ) {
            hasChar = false;
            currentChar = 0;
        } else {
            hasChar = true;
            currentChar = (char) nextInt;
        }
    }

    public @Nullable String advanceWhile ( @NotNull CharPredicate predicate ) throws IOException {

        // assert checkHasChar();

        StringBuilder sb = new StringBuilder();
        if ( appendCurrentCharWhile ( predicate, sb ) ) {
            return sb.toString();
        } else {
            return null;
        }
    }


    // location

    public @NotNull TextLocation currentLocation() {

        return new TextLocation ( resource, nextLineNumber, nextColumnNumber, null );
    }

    public @Nullable TextResource resource() { return resource; }

    public int currentLineNumber() { return nextLineNumber; }

    public int currentColumnNumber() { return nextColumnNumber; }


    // consume

    public void consumeCurrentCharAndAdvance ( @NotNull CharConsumer consumer ) throws IOException {

        assert checkHasChar();

        consumer.consume ( currentChar );
        advance();
    }

    public boolean consumeCurrentCharIfAndAdvance ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException {

        assert checkHasChar();

        if ( ! predicate.accept ( currentChar ) ) return false;
        consumeCurrentCharAndAdvance ( consumer );
        return true;
    }

    public boolean consumeCurrentCharWhile ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException {

        // assert checkHasChar();

        boolean charFound = false;
        while ( hasChar ) {
            if ( ! predicate.accept ( currentChar ) ) return charFound;
            consumer.consume ( currentChar );
            charFound = true;
            advance();
        }
        return charFound;
    }

    public boolean consumeRemaining ( @NotNull CharConsumer consumer ) throws IOException {

        boolean charFound = hasChar;
        while ( hasChar ) {
            consumer.consume ( currentChar );
            advance();
        }
        return charFound;
    }


    // append

    public void appendCurrentCharAndAdvance ( @NotNull StringBuilder sb ) throws IOException {

        assert checkHasChar();

        sb.append ( currentChar );
        advance();
    }

    public boolean appendCurrentCharIfAndAdvance ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb )
        throws IOException {

        return consumeCurrentCharIfAndAdvance ( predicate, sb::append );
    }

    public boolean appendCurrentCharWhile ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb ) throws IOException {

        // assert checkHasChar();

        return consumeCurrentCharWhile ( predicate, sb::append );
    }

    public boolean appendRemaining ( @NotNull StringBuilder sb ) throws IOException {

        return consumeRemaining ( sb::append );
    }


    // read

    public @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException {

        StringBuilder sb = new StringBuilder();
        appendCurrentCharWhile ( predicate, sb );
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readWhileAtChar ( char c ) throws IOException {

        return readWhile ( currentChar -> currentChar == c );
    }

    public @Nullable String readMaxNChars ( long n ) throws IOException {

        StringBuilder sb = new StringBuilder();
        for ( long i = 1; i <= n; i++ ) {
            if ( ! hasChar ) break;
            appendCurrentCharAndAdvance ( sb );
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readRemaining() throws IOException {

        StringBuilder sb = new StringBuilder();
        appendRemaining ( sb );
        return sb.length() == 0 ? null : sb.toString();
    }


    // isAt

    public boolean isAt ( @NotNull CharPredicate predicate ) {

        // assert checkHasChar();

        return predicate.accept ( currentChar );
    }

    public boolean isAtChar ( char c ) {

        // assert checkHasChar();

        return currentChar == c;
    }

    public boolean isAtString ( @NotNull String s ) throws IOException {

        // assert checkHasChar();

        // fast return if current character doesn't match
        if ( currentChar != s.charAt ( 0 ) ) return false;

        setMark ( s.length() );

        boolean result = true;
        for ( int i = 1; i < s.length(); i++ ) {

            int nextInt = reader.read();

            if ( nextInt == -1 ) {
                result = false;
                break;
            }

            if ( (char) nextInt != s.charAt ( i ) ) {
                result = false;
                break;
            }
        }

        goBackToMark();

        return result;
    }


    // skip

    public boolean skipIf ( @NotNull CharPredicate predicate ) throws IOException {

        // assert checkHasChar();

        return consumeCurrentCharIfAndAdvance ( predicate, c -> {} );
    }

    public boolean skipChar ( char c ) throws IOException {

        // assert checkHasChar();

        if ( currentChar == c ) {
            advance();
            return true;
        } else {
            return false;
        }
    }

    public boolean skipString ( @NotNull String string ) throws IOException {

        // assert checkHasChar();

        if ( isAtString ( string ) ) {
            skipNChars ( string.length() );
            return true;
        } else {
            return false;
        }
    }

    public boolean skipWhile ( @NotNull CharPredicate predicate ) throws IOException {

        // assert checkHasChar();

        return consumeCurrentCharWhile ( predicate, c -> {} );
    }

    public void skipNChars ( long n ) throws IOException {

        // assert checkHasChar();

        if ( n < 0 ) throw new IllegalArgumentException ( "n cannot be < 0, but is " + n + "." );

        for ( long i = 1; i <= n; i++ ) {
            if ( hasChar ) {
                advance();
            } else {
                throw new IllegalArgumentException (
                    "Cannot skip " + n + " characters, because the end of input has been reached after " + i + " skips."  );
            }
        }
    }


    // read-ahead

    public void setMark ( int readAheadLimit ) {

        assert checkHasChar();

        hasNextAtMark = hasChar;
        nextCharAtMark = currentChar;
        nextLineNumberAtMark = nextLineNumber;
        nextColumnNumberAtMark = nextColumnNumber;

        try {
            reader.mark ( readAheadLimit );
        } catch ( IOException e ) {
            // should never happen because markSupported() is ensured in constructor
            throw new RuntimeException ( e );
        }
    }

    public void goBackToMark() {

        try {
            reader.reset();
        } catch ( IOException e ) {
            // should never happen because markSupported() is ensured in constructor
            throw new RuntimeException ( e );
        }

        hasChar = hasNextAtMark;
        currentChar = nextCharAtMark;
        nextLineNumber = nextLineNumberAtMark;
        nextColumnNumber = nextColumnNumberAtMark;
    }


    // peek

    public @Nullable Character peekNextChar() throws IOException {

        // assert checkHasChar();
        if ( ! hasChar ) return null;

        setMark ( 1 );
        int peekedInt = reader.read();
        goBackToMark();

        if ( peekedInt == -1 ) {
            return null;
        } else {
            return (char) peekedInt;
        }
    }

    public boolean isNextChar ( char c ) throws IOException {

        // assert checkHasChar();

        Character next = peekNextChar();
        if ( next != null ) {
            return next == c;
        } else {
            return false;
        }
    }

    public @Nullable Character peekCharAfterRequired ( @NotNull CharPredicate predicate, int lookAhead ) throws IOException {

        return peekCharAfter ( predicate, lookAhead, true );
    }

    public @Nullable Character peekCharAfterOptional ( @NotNull CharPredicate predicate, int lookAhead ) throws IOException {

        return peekCharAfter ( predicate, lookAhead, false );
    }

    private @Nullable Character peekCharAfter (
        @NotNull CharPredicate predicate, int lookAhead, boolean required ) throws IOException {

        // assert checkHasChar();

        if ( required ) {
            if ( ! predicate.accept ( currentChar ) ) return null;
        }

        setMark ( lookAhead );
        skipWhile ( predicate );
        Character result = hasChar ? currentChar : null;
        goBackToMark();

        return result;
    }

/*
    public @NotNull String peekCurrentString ( int length ) throws IOException {
        if ( length <= 0 ) throw new IllegalArgumentException ( "length cannot be <= 0, but is " + length + "." );

        StringBuilder sb = new StringBuilder();
        sb.append ( previousChar );
        if ( length == 1 || ! hasNext ) return sb.toString();

        sb.append ( nextChar );
        if ( length == 2 ) return sb.toString();

        setMark ( length );
        for ( int i = 3; i <= length; i++ ) {
            int peekedInt = reader.read();
            if ( peekedInt == -1 ) break;
            sb.append ( (char) peekedInt );
        }
        goBackToMark();

        return sb.toString();
    }

*/

    // debugging

    public @NotNull String stateToString() {

        StringBuilder sb = new StringBuilder();

        sb.append ( "at " );
        sb.append ( currentChar );
        sb.append ( " (" );
        sb.append ( nextLineNumber );
        sb.append ( "," );
        sb.append ( nextColumnNumber );
        sb.append ( ")" );

        return sb.toString();
    }

    public void stateToOSOut ( @Nullable String label ) {

        System.out.println();
        if ( label != null ) System.out.println ( label );
        System.out.println ( stateToString() );
    }
}
