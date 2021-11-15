package dev.pp.text.reader.stack;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.character.CharConsumer;
import dev.pp.text.utilities.character.CharPredicate;
import dev.pp.text.reader.CharReader;
import dev.pp.text.reader.DefaultCharReader;
import dev.pp.text.location.TextLocation;
import dev.pp.text.resource.TextResource;
import dev.pp.text.utilities.string.StringConstants;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Stack;


public class DefaultStackedCharReader implements StackedCharReader {


    private final @NotNull Stack<CharReader> readers;

    private @NotNull CharReader currentReader;


    // constructors

    public DefaultStackedCharReader ( @NotNull CharReader initialIterator ) {

        this.readers = new Stack<>();

        push ( initialIterator );
    }

    public DefaultStackedCharReader ( @NotNull Reader reader, @Nullable TextResource resource ) throws IOException {

        this ( new DefaultCharReader ( reader, resource ) );
    }

    public DefaultStackedCharReader ( @NotNull File file ) throws IOException {

        this ( new DefaultCharReader ( file ) );
    }

    public DefaultStackedCharReader ( @NotNull URL url ) throws IOException {

        this ( new DefaultCharReader ( url ) );
    }

    public DefaultStackedCharReader ( @NotNull String string ) throws IOException {

        this ( new DefaultCharReader ( string ) );
    }


    // push

    public void push ( @NotNull CharReader iterator ) {

        readers.push ( iterator );
        currentReader = iterator;
    }

    public void push ( @NotNull File file ) throws IOException {

        push ( new DefaultCharReader ( file ) );
    }

    public void push ( @NotNull URL url ) throws IOException {

        push ( new DefaultCharReader ( url ) );
    }

/*
    public void push ( @NotNull String string, @Nullable Object resource ) throws IOException {
        push ( new DefaultCharIterator( string, resource ) );
    }

 */

    public void push ( @NotNull String string ) throws IOException {

        push ( new DefaultCharReader ( string ) );
    }


    // iteration

    public boolean hasChar () {

        if ( currentReader.hasChar() ) return true;

        if ( readers.isEmpty() ) return currentReader.hasChar();

        for ( CharReader iterator : readers ) {
            if ( iterator.hasChar() ) return true;
        }
        return false;
    }

    public char currentChar () { return currentReader.currentChar(); }


    // advance

    /*
    public void advance() throws IOException {

        // if ( currentIterator.hasChar () ) return currentIterator.advance ();
        if ( currentIterator.hasChar () ) {
            currentIterator.advance ();
            return;
        }

        iterators.pop();

        CharIterator newIterator = getNextIteratorWithHasNext();
        if ( newIterator != null ) currentIterator = newIterator;

        // return currentIterator.advance ();
        currentIterator.advance();
    }
*/
/*
    public void advance() throws IOException {

        // if ( currentIterator.hasChar () ) return currentIterator.advance ();
        if ( currentIterator.hasChar () ) {
            currentIterator.advance ();
            if ( currentIterator.hasChar () ) {
                return;
            }
        }

        iterators.pop();

        CharIterator newIterator = getNextIteratorWithHasChar();
        if ( newIterator != null ) {
            currentIterator = newIterator;
        } else {
            // throw new NoSuchElementException (
            //    "There are no more characters to read at position" + StringConstants.NEW_LINE + nextLocation () );
            currentIterator.advance ();
        }

        // return currentIterator.advance ();
        // currentIterator.advance();
    }
*/
    public void advance() throws IOException {

        currentReader.advance();

        while ( true ) {

            // char result = currentReader.read();
            if ( currentReader.hasChar() ) return;

            readers.pop();

            if ( readers.empty() ) {
                // return result;
                return;
            }

            currentReader = readers.peek();
        }
    }
/*
    private @Nullable CharIterator getNextIteratorWithHasChar() {

        while ( ! iterators.isEmpty() ) {
            CharIterator iterator = iterators.peek();
            if ( iterator.hasChar () ) return iterator;
            iterators.pop();
        }
        return null;
    }
*/

    /*
        TODO !!!
        Some functions (who involve reading more than one character) don't work correctly
        if current reader is finished, but other non-finished reader is on stack.
        Instead of delegating to currentReader, these function should have their own implementation that uses the
        read() function of this class.
    */


    public @Nullable String advanceWhile ( @NotNull CharPredicate predicate ) throws IOException {
        return currentReader.advanceWhile ( predicate ); }


    // location

    public @NotNull TextLocation currentLocation () {

        if ( readers.isEmpty() ) return currentReader.currentLocation();

        TextLocation result = null;
        for ( CharReader iterator : readers ) {
            result = new TextLocation (
                iterator.resource(), iterator.currentLineNumber (), iterator.currentColumnNumber (), result );
        }
        return result;
    }

    public @Nullable TextResource resource() { return currentReader.resource(); }

    public int currentLineNumber () { return currentReader.currentLineNumber (); }

    public int currentColumnNumber () { return currentReader.currentColumnNumber (); }


    // consume

    public void consumeCurrentCharAndAdvance ( @NotNull CharConsumer consumer ) throws IOException {
        currentReader.consumeCurrentCharAndAdvance ( consumer ); }

    public boolean consumeCurrentCharIfAndAdvance (
        @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException {
            return currentReader.consumeCurrentCharIfAndAdvance ( predicate, consumer ); }

    public boolean consumeCurrentCharWhile (
        @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException {
            return currentReader.consumeCurrentCharWhile ( predicate, consumer ); }

    public boolean consumeRemaining ( @NotNull CharConsumer consumer ) throws IOException {
        return currentReader.consumeRemaining ( consumer ); }


    // append

    public void appendCurrentCharAndAdvance ( @NotNull StringBuilder sb ) throws IOException {
        currentReader.appendCurrentCharAndAdvance ( sb ); }

    public boolean appendCurrentCharWhile (
        @NotNull CharPredicate predicate, @NotNull StringBuilder sb ) throws IOException {
            return currentReader.appendCurrentCharWhile ( predicate, sb ); }

    public boolean appendCurrentCharIfAndAdvance ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb )
        throws IOException {

        return currentReader.appendCurrentCharIfAndAdvance ( predicate, sb );
    }

    public boolean appendRemaining ( @NotNull StringBuilder sb ) throws IOException {
        return currentReader.appendRemaining ( sb ); }


    // read

    public @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException {
        return currentReader.readWhile ( predicate ); }

    public @Nullable String readWhileAtChar ( char c ) throws IOException {
        return currentReader.readWhileAtChar ( c ); }

    public @Nullable String readRemaining() throws IOException {
        return currentReader.readRemaining(); }

    public @Nullable String readMaxNChars ( long n ) throws IOException {
        return currentReader.readMaxNChars ( n ); }

    // isAt

    public boolean isAt ( @NotNull CharPredicate predicate ) { return currentReader.isAt ( predicate ); }

    public boolean isAtChar ( char c ) { return currentReader.isAtChar ( c ); }

    public boolean isAtString ( @NotNull String s ) throws IOException { return currentReader.isAtString ( s ); }


    // skip

    public boolean skipIf ( @NotNull CharPredicate predicate ) throws IOException {
        return currentReader.skipIf ( predicate ); }

    public boolean skipChar ( char c ) throws IOException {
        return currentReader.skipChar ( c ); }

    public boolean skipString ( @NotNull String string ) throws IOException {
        return currentReader.skipString ( string ); }

    public boolean skipWhile ( @NotNull CharPredicate predicate ) throws IOException {
        return currentReader.skipWhile ( predicate ); }

    public void skipNChars ( long n ) throws IOException {
        currentReader.skipNChars ( n ); }


    // read-ahead

    public void setMark ( int readAheadLimit ) { currentReader.setMark ( readAheadLimit ); }

    public void goBackToMark() { currentReader.goBackToMark(); }


    // peek

    public @Nullable Character peekNextChar() throws IOException {
        return currentReader.peekNextChar(); }

    public boolean isNextChar ( char c ) throws IOException {
        return currentReader.isNextChar ( c ); }

    public @Nullable Character peekCharAfterRequired (
        @NotNull CharPredicate predicate, int lookAhead ) throws IOException {

        return currentReader.peekCharAfterRequired ( predicate, lookAhead );
    }

    public @Nullable Character peekCharAfterOptional (
        @NotNull CharPredicate predicate, int lookAhead ) throws IOException {

        return currentReader.peekCharAfterOptional ( predicate, lookAhead );
    }

    // debugging

    public @NotNull String stateToString() {

        StringBuilder sb = new StringBuilder();

        sb.append ( "Iterators on stack: " );
        sb.append ( readers.size() );
        sb.append ( StringConstants.OS_NEW_LINE );

        int i = 1;
        for ( CharReader iterator : readers ) {
            sb.append ( "#" + i + ": " + iterator.stateToString() );
            sb.append ( StringConstants.OS_NEW_LINE );
            i++;
        }
        sb.append ( "current iterator: " );
        sb.append ( currentReader.stateToString() );

        return sb.toString();
    }

    public void stateToOSOut ( @Nullable String label ) {

        System.out.println();
        if ( label != null ) System.out.println ( label );
        System.out.println ( stateToString() );
    }
}
