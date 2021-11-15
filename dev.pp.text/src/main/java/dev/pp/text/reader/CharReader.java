package dev.pp.text.reader;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.character.CharConsumer;
import dev.pp.text.utilities.character.CharPredicate;
import dev.pp.text.location.TextLocation;
import dev.pp.text.resource.TextResource;

import java.io.IOException;

public interface CharReader {

    boolean hasChar();

    char currentChar();

    // advance
    void advance() throws IOException;
    // char getCurrentAndAdvance() throws IOException;
    // char advanceAndGetCurrent() throws IOException;
    // ? @Nullable Character advanceIf ( @NotNull CharPredicate predicate ) throws IOException;
    // advanceIf ( @NotNull CharPredicate predicate ) throws IOException;
    // char getCurrentAndAdvanceIf ( @NotNull CharPredicate predicate ) throws IOException;
    // char AdvanceIfAndGetCurrent ( @NotNull CharPredicate predicate ) throws IOException;
    @Nullable String advanceWhile ( @NotNull CharPredicate predicate ) throws IOException;

    // location
    @NotNull TextLocation currentLocation ();
    @Nullable TextResource resource();
    int currentLineNumber ();
    int currentColumnNumber ();

    // consume
    void consumeCurrentCharAndAdvance ( @NotNull CharConsumer consumer ) throws IOException;
    boolean consumeCurrentCharIfAndAdvance ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException;
    boolean consumeCurrentCharWhile ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException;
    boolean consumeRemaining ( @NotNull CharConsumer consumer ) throws IOException;
    // boolean consumeIfHasNext ( @NotNull CharConsumer consumer ) throws IOException;
    // void consumeUntil ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException;
    // void consumeUntilOrAll ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException;
    // void consumeNChars ( long n, @NotNull CharConsumer consumer ) throws IOException;
    // void consumeNCharsOrAll ( long n, @NotNull CharConsumer consumer ) throws IOException;

    // append
    void appendCurrentCharAndAdvance ( @NotNull StringBuilder sb ) throws IOException;
    boolean appendCurrentCharIfAndAdvance ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb ) throws IOException;
    boolean appendCurrentCharWhile ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb ) throws IOException;
    boolean appendRemaining ( @NotNull StringBuilder sb ) throws IOException;
    // boolean appendIfHasNext ( @NotNull StringBuilder sb ) throws IOException;
    // boolean appendNextIf ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb ) throws IOException;

    // read
    @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException;
    @Nullable String readWhileAtChar ( char c ) throws IOException;
    // @Nullable String readNChars ( long n ) throws IOException;
    @Nullable String readMaxNChars ( long n ) throws IOException;
    @Nullable String readRemaining() throws IOException;

    // isAt
    boolean isAt ( @NotNull CharPredicate predicate );
    boolean isAtChar ( char c );
    boolean isAtString ( @NotNull String s ) throws IOException;

    // skip
    boolean skipIf ( @NotNull CharPredicate predicate ) throws IOException;
    boolean skipChar ( char c ) throws IOException;
    boolean skipString ( @NotNull String s ) throws IOException;
    boolean skipWhile ( @NotNull CharPredicate predicate ) throws IOException;
    void skipNChars ( long n ) throws IOException;
    // void skipNCharsOrRemaining ( long n ) throws IOException;

    // read-ahead
    void setMark ( int readAheadLimit );
    void goBackToMark();

    // peek
    @Nullable Character peekNextChar() throws IOException;
    boolean isNextChar ( char c ) throws IOException;
    @Nullable Character peekCharAfterRequired ( @NotNull CharPredicate predicate, int lookAhead ) throws IOException;
    @Nullable Character peekCharAfterOptional ( @NotNull CharPredicate predicate, int lookAhead ) throws IOException;
    // void peekNextWhile ( @NotNull CharPredicate predicate, @NotNull CharConsumer consumer ) throws IOException;
    // @NotNull String peekNextWhile ( int length ) throws IOException;
    // void peekNextWhile ( @NotNull CharPredicate predicate, @NotNull StringBuilder sb ) throws IOException;
    // void peekNextNChars ( long n ) throws IOException;
    // void peekNextNCharsNOrAll ( long n ) throws IOException;
    // void peekNextAll ( long n ) throws IOException;

    // debugging
    @NotNull String stateToString();
    void stateToOSOut ( @Nullable String label );
}
