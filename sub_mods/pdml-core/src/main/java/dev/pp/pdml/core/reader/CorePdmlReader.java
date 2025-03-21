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


    protected CorePdmlReader ( @NotNull CharReaderWithInserts charReader ) {
        this.charReader = charReader;
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

        this ( CharReaderWithInsertsImpl.createAndAdvance ( reader, resource, lineOffset, columnOffset ) );
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
        TextToken token = currentToken();
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
        TextToken token = currentToken();
        return readNodeEnd () ? token : null;
    }

    public boolean isAtNodeEnd() {
        return isAtChar ( CorePdmlConstants.NODE_END_CHAR );
    }


    // Node Tag

    public @Nullable String readTag() throws IOException, PdmlException {
        return readTagOrText (
            CorePdmlConstants.INVALID_TAG_CHARS, CorePdmlConstants.TAG_ESCAPE_CHARS );
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
            CorePdmlConstants.INVALID_TEXT_CHARS, CorePdmlConstants.TEXT_ESCAPE_CHARS );
    }

    public @Nullable TextToken readTextToken() throws IOException, PdmlException {

        TextLocation location = currentLocation();
        String text = readText ();
        return text == null ? null : new TextToken ( text, location );
    }


    private @Nullable String readTagOrText (
        @NotNull Set<Character> invalidChars,
        @NotNull Map<Character,Character> charEscapeMap ) throws IOException, PdmlException {

        final StringBuilder result = new StringBuilder();

        while ( true ) {

            // handleExtensionNode();

            char currentChar = charReader.currentChar();
            if ( invalidChars.contains ( currentChar ) || isAtEnd() ) {
                return result.isEmpty() ? null : result.toString();
            }

            if ( currentChar == CorePdmlConstants.ESCAPE_CHAR ) {
                appendCharacterEscapeSequence ( charEscapeMap, false, result );
            } else {
                result.append ( currentChar );
                charReader.advance();
            }
        }
    }

    protected void appendCharacterEscapeSequence (
        @NotNull Map<Character,Character> charEscapeMap,
        boolean allowUnicodeEscapes,
        @NotNull StringBuilder result )
        throws IOException, MalformedPdmlException {

        try {
            CharEscapeUtil.unescapeSequenceAndAppend ( charReader, charEscapeMap, allowUnicodeEscapes, result );

        } catch ( InvalidTextException e ) {
/*
            char currentChar = currentChar();
            if ( currentChar == 'U' ) {
                throw abortingErrorAtCurrentLocation (
                    "The \\Uhhhhhhhh syntax for escaping Unicode characters is no more supported. Please use the new \\u{hhhhhh} syntax instead.",
                    "INVALID_ESCAPED_CHARACTER" );
            } else if ( currentChar == '(' || currentChar == ')' ) {
                throw abortingErrorAtCurrentLocation (
                    "The \\( and \\) syntax for escaping parenthesis is no more supported.",
                    "INVALID_ESCAPED_CHARACTER" );
            } else {
 */
                String id = e.getErrorId();
                if ( id == null ) id = "INVALID_CHARACTER_ESCAPE_SEQUENCE";
                throw new MalformedPdmlException ( e.getMessage(), id, e.textToken() );
//            }
        }
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

    public @NotNull TextToken currentToken() {
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

    public boolean skipSpacesAndTabsAndLineBreaks() throws IOException {
        return charReader.skipSpacesAndTabsAndLineBreaks();
    }
}
