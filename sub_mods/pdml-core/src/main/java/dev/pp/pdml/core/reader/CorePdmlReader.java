package dev.pp.pdml.core.reader;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Set;

import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.character.CharPredicate;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.reader.stack.CharReaderWithInserts;
import dev.pp.core.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.core.text.reader.util.CharEscapeUtil;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.token.TextToken;

public class CorePdmlReader {


    protected final @NotNull CharReaderWithInserts charReader;
    protected final boolean allowUnicodeEscapes;


    protected CorePdmlReader (
        @NotNull CharReaderWithInserts charReader,
        boolean allowUnicodeEscapes ) {

        this.charReader = charReader;
        this.allowUnicodeEscapes = allowUnicodeEscapes;
    }

    /*
    public CorePdmlReader ( @NotNull CharReader charReader ) {
        this ( new CharReaderWithInsertsImpl ( charReader ) );
    }
     */

    public CorePdmlReader (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset ) throws IOException {

        this ( CharReaderWithInsertsImpl.createAndAdvance ( reader, resource, lineOffset, columnOffset ), false );
    }

    public CorePdmlReader (
        @NotNull Reader reader,
        @Nullable TextResource resource ) throws IOException {

        this ( reader, resource, null, null );
    }

    public CorePdmlReader ( @NotNull Reader reader ) throws IOException {
        this ( reader, null );
    }


    // Node Start

    public boolean readNodeStart() throws IOException, PdmlException {
        return skipChar ( CorePdmlConstants.NODE_START_CHAR );
    }

    public @Nullable TextToken readNodeStartToken() throws IOException, PdmlException {
        TextToken token = currentCharToken ();
        return readNodeStart() ? token : null;
    }

    public boolean isAtNodeStart() throws IOException, PdmlException {
        return isAtChar ( CorePdmlConstants.NODE_START_CHAR );
    }


    // Node End

    public boolean readNodeEnd() throws IOException {
        return skipChar ( CorePdmlConstants.NODE_END_CHAR );
    }

    public @Nullable TextToken readNodeEndToken() throws IOException {
        TextToken token = currentCharToken();
        return readNodeEnd () ? token : null;
    }

    public boolean isAtNodeEnd() {
        return isAtChar ( CorePdmlConstants.NODE_END_CHAR );
    }


    // Node Tag

    public @Nullable String readTag() throws IOException, PdmlException {
        return readTagOrText (
            CorePdmlConstants.TAG_END_CHARS,
            CorePdmlConstants.INVALID_TAG_CHARS,
            CorePdmlConstants.TAG_AND_TEXT_ESCAPE_CHARS,
            allowUnicodeEscapes );
    }

    public @Nullable TextToken readTagToken() throws IOException, PdmlException {

        TextLocation location = currentLocation();
        String tag = readTag();
        return tag == null ? null : new TextToken ( tag, location );
    }


    // Separator

    public @Nullable String readSeparator() throws IOException, PdmlException {
        return charReader.readSpaceOrTabOrLineBreak();
    }


    // Text

    public @Nullable String readText() throws IOException, PdmlException {
        return readTagOrText (
            CorePdmlConstants.TEXT_END_CHARS,
            CorePdmlConstants.INVALID_TEXT_CHARS,
            CorePdmlConstants.TAG_AND_TEXT_ESCAPE_CHARS,
            allowUnicodeEscapes );
    }

    public @Nullable TextToken readTextToken() throws IOException, PdmlException {

        TextLocation location = currentLocation();
        String text = readText ();
        return text == null ? null : new TextToken ( text, location );
    }

    public @Nullable String readTagOrText (
        @NotNull Set<Character> endChars,
        @NotNull Set<Character> invalidChars,
        @NotNull Map<Character, Character> escapeChars,
        boolean allowUnicodeEscapes ) throws IOException, PdmlException {

        final StringBuilder result = new StringBuilder();

        while ( true ) {

            char currentChar = charReader.currentChar();

            if ( isAtEnd() ) {
                break;

            } else if ( endChars.contains ( currentChar ) ) {
                break;

            } else if ( invalidChars.contains ( currentChar ) ) {
                errorDetectedAtCurrentPosition (
                    "Character '" + currentChar + "' is not allowed in this context.",
                    "INVALID_CHAR" );

            } else if ( currentChar <= 0X001F &&
                currentChar != '\n' && currentChar != '\r' && currentChar != '\t' && currentChar != '\f' ) {
                String hexString = charToHexString ( currentChar );
                errorDetectedAtCurrentPosition (
                    "Unicode code point " + hexString + " is invalid. Unicode code points below U+001F (control characters) are not allowed, except U+0009 (Character Tabulation), U+000A (End of Line), U+000C (Form Feed), and U+000D (Carriage Return).",
                    "INVALID_CHAR" );

            } else if ( currentChar >= 0X0080 && currentChar <= 0X009F ) {
                String hexString = charToHexString ( currentChar );
                errorDetectedAtCurrentPosition (
                    "Unicode code point " + hexString + " is invalid. Unicode code points in the range U+0080 to U+009F (control characters) are not allowed.",
                    "INVALID_CHAR" );

            /*
                This doesn't work because Java uses UTF-16 to store strings in memory
                Each char in Java is a 16-bit (2-byte) code unit, which follows UTF-16 encoding rules.
                } else if ( currentChar >= 0XD800 && currentChar <= 0XDFFF ) {
                    String hexString = charToHexString ( currentChar );
                    errorDetectedAtCurrentPosition (
                        "Unicode code point " + hexString + " is invalid. Unicode code points in the range U+D800 to U+DFFF are not allowed (they are surrogates reserved to encode code points beyond U+FFFF in UTF-16).",
                        "INVALID_CHAR" );
             */

            } else if ( currentChar == CorePdmlConstants.ESCAPE_CHAR ) {
                appendCharacterEscapeSequence ( escapeChars, allowUnicodeEscapes, result );

            } else {
                result.append ( currentChar );
                charReader.advance();
            }
        }

        return result.isEmpty() ? null : result.toString();
    }

    private @NotNull String charToHexString ( char c ) {
        return Integer.toHexString ( c );
    }


    protected void appendCharacterEscapeSequence (
        @NotNull Map<Character,Character> charEscapeMap,
        boolean allowUnicodeEscapes,
        @NotNull StringBuilder result )
        throws IOException, MalformedPdmlException {

        try {
            CharEscapeUtil.unescapeSequenceAndAppend ( charReader, charEscapeMap, allowUnicodeEscapes, result );

        } catch ( InvalidTextException e ) {
            String id = e.getErrorId();
            if ( id == null ) id = "INVALID_CHARACTER_ESCAPE_SEQUENCE";
            errorDetected ( e.getMessage(), id, e.textToken() );
        }
    }


    // Error handling

    private void errorDetected (
        @NotNull String message,
        @NotNull String id,
        @Nullable TextToken token ) throws MalformedPdmlException {

        throw new MalformedPdmlException ( message, id, token );
    }

    private void errorDetectedAtCurrentPosition (
        @NotNull String message,
        @NotNull String id ) throws MalformedPdmlException {

        throw new MalformedPdmlException ( message, id, currentCharToken () );
    }


    // Reader Method Wrappers

    public boolean isAtEnd() { return charReader.isAtEnd(); }

    public boolean isNotAtEnd() { return charReader.isNotAtEnd(); }

    public @Nullable TextResource currentResource() {
        return charReader.resource();
    }

    public @NotNull TextLocation currentLocation() {
        return charReader.currentLocation();
    }

    public char currentChar() {
        return charReader.currentChar();
    }

    protected boolean isNextChar ( char c ) throws IOException {
        return charReader.isNextChar ( c );
    }

    public @NotNull TextToken currentCharToken() {
        return new TextToken ( currentChar(), currentLocation() );
    }

    public boolean isAtChar ( char c ) {
        return charReader.isAtChar ( c );
    }

    protected boolean isAtString ( @NotNull String string ) throws IOException {
        return charReader.isAtString ( string );
    }

    public void advanceChar() throws IOException {
        charReader.advance();
    }

    protected boolean appendCharWhile ( CharPredicate predicate, StringBuilder sb ) throws IOException {
        return charReader.appendWhile ( predicate, sb );
    }

    protected boolean skipChar ( char c ) throws IOException {
        return charReader.skipChar ( c );
    }

    public boolean skipWhitespace() throws IOException {
        return charReader.skipSpacesAndTabsAndLineBreaks();
    }
}
