package dev.pp.pdml.reader;

import dev.pp.pdml.core.reader.CorePdmlReader;
import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.PdmlExtensionsConstants;
import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.reader.CharReader;
import dev.pp.core.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.core.text.reader.stack.CharReaderWithInserts;
import dev.pp.core.text.reader.util.MultilineStringLiteralUtil;
import dev.pp.core.text.reader.util.RawStringLiteralUtil;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.token.TextToken;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import static dev.pp.pdml.data.PdmlExtensionsConstants.*;

public class PdmlReader extends CorePdmlReader {

    private PdmlReader ( @NotNull CharReaderWithInserts charReader ) {
        super ( charReader, true );
    }

    public PdmlReader ( @NotNull CharReader charReader ) throws IOException {
        this ( new CharReaderWithInsertsImpl ( charReader ) );
    }

    public PdmlReader (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset ) throws IOException {

        this ( CharReaderWithInsertsImpl.createAndAdvance ( reader, resource, lineOffset, columnOffset ) );
    }

    public PdmlReader (
        @NotNull Reader reader,
        @Nullable TextResource resource ) throws IOException {

        this ( reader, resource, null, null );
    }

    public PdmlReader (
        @NotNull Reader reader ) throws IOException {

        this ( reader, null );
    }

    public PdmlReader (
        @NotNull TextResourceReader textResourceReader ) throws IOException {

        this ( textResourceReader.getReader(), textResourceReader.getTextResource() );
    }


    public boolean skipNamespaceSeparator() throws IOException {
        return charReader.skipChar ( PdmlExtensionsConstants.NAMESPACE_SEPARATOR_CHAR );
    }


    // Comments

    private boolean isAtSingleOrMultilineCommentExtensionStart() throws IOException {
        return isAtString ( SINGLE_OR_MULTI_LINE_COMMENT_EXTENSION_START );
    }

    private boolean isAtMultilineCommentExtensionStart() throws IOException {
        return isAtString ( PdmlExtensionsConstants.MULTI_LINE_COMMENT_EXTENSION_START );
    }

    private boolean isAtSinglelineCommentExtensionStart() throws IOException {
        return isAtString ( PdmlExtensionsConstants.SINGLE_LINE_COMMENT_WITH_2_SLASHES_EXTENSION_START );
    }

    public boolean skipSingleOrMultilineComment() throws IOException, MalformedPdmlException {
        return skipMultilineComment() || skipSinglelineComment();
    }

    public boolean skipMultilineComment() throws IOException, MalformedPdmlException {

        // can be made faster by writing a specific version that doesn't use a StringBuilder
        // to build and return the comment's content
        return readMultilineComment() != null;
    }

    public boolean skipSinglelineComment() throws IOException {

        /*
        if ( isAtSinglelineCommentExtensionStart() ) {
            return charReader.skipUntilEndOfLine ( INCLUDE_LINE_BREAK_IN_SINGLE_LINE_COMMENT );
        } else {
            return false;
        }
         */
        return readSinglelineComment() != null;
    }

    public @Nullable String readSingleOrMultilineComment() throws IOException, MalformedPdmlException {
        String result = readMultilineComment();
        if ( result != null ) {
            return result;
        } else {
            return readSinglelineComment();
        }
    }

    public @Nullable String readMultilineComment() throws IOException, MalformedPdmlException {

        if ( ! isAtMultilineCommentExtensionStart() ) return null;

        StringBuilder result = new StringBuilder();
        readMultilineCommentSnippet ( result );

        return result.toString();
    }

    private void readMultilineCommentSnippet ( @NotNull StringBuilder result ) throws IOException, MalformedPdmlException {

        TextLocation location = currentLocation();
        TextToken token = currentCharToken ();

        // we are at the start of a multiline comment, i.e. ^/*
        String caretAndSlash = String.valueOf ( EXTENSION_START_CHAR ) + SINGLE_OR_MULTI_LINE_COMMENT_START_CHAR;
        assert skipString ( caretAndSlash );
        result.append ( caretAndSlash );

        // The comment can start with more than one *, e.g. ^/*** ... ***/
        String stars = readWhileAtChar ( MULTI_LINE_COMMENT_STAR_CHAR );
        assert stars != null && ! stars.isEmpty();
        result.append ( stars );

        String commentEnd = stars + MULTI_LINE_COMMENT_END_CHAR;

        while ( true ) {

            if ( isAtEnd() ) {
                throw new MalformedPdmlException (
                    "The comment starting at line " + location.getLineNumber() +
                        ", column " + location.getColumnNumber() + " is never closed.",
                    "UNCLOSED_COMMENT",
                    token );
            }

            if ( skipString ( commentEnd ) ) {
                result.append ( commentEnd );
                return;

            } else if ( isAtMultilineCommentExtensionStart () ) {
                readMultilineCommentSnippet ( result ); // recursive call for nested comments

            } else {
                result.append ( currentChar() );
                if ( isNotAtEnd() ) advanceChar();
            }
        }
    }

    public @Nullable String readSinglelineComment() throws IOException {

        /*
        if ( isAtSinglelineCommentExtensionStart() ) {
            return charReader.readUntilEndOfLine ( INCLUDE_LINE_BREAK_IN_SINGLE_LINE_COMMENT );
        } else {
            return null;
        }
         */
        boolean includeLineBreak;
        if ( isAtString ( SINGLE_LINE_COMMENT_WITH_2_SLASHES_EXTENSION_START ) ) {
            includeLineBreak = true;
        } else if ( isAtString ( SINGLE_LINE_COMMENT_WITH_1_SLASH_EXTENSION_START ) ) {
            includeLineBreak = false;
        } else {
            return null;
        }

        return charReader.readUntilEndOfLine ( includeLineBreak );
    }

    public boolean skipWhitespaceAndComments() throws IOException, MalformedPdmlException {

        if ( isAtEnd() ) return false;

        boolean skipped = false;
        while ( true ) {
            if ( skipWhitespace () ||
                skipSingleOrMultilineComment() ) {
                skipped = true;
            } else {
                break;
            }
        }
        return skipped;
    }


    // Namespaces

    public boolean isAtNamespaceDeclarationsExtensionStart () throws IOException {
        return isAtString ( NAMESPACE_DECLARATIONS_EXTENSION_START );
    }


    // Attributes

    public boolean isAtAttributesExtensionStart() throws IOException {
        return isAtString ( ATTRIBUTES_EXTENSION_START );
    }

    public boolean isAtAttributesStart() {
        return isAtChar ( ATTRIBUTES_START_CHAR );
    }

    public boolean readAttributesExtensionStart() throws IOException {
        return skipString ( ATTRIBUTES_EXTENSION_START );
    }

    public @Nullable TextToken readAttributesExtensionStartToken() throws IOException {

        TextLocation location = currentLocation();
        if ( readAttributesExtensionStart() ) {
            return new TextToken ( PdmlExtensionsConstants.ATTRIBUTES_EXTENSION_START, location );
        } else {
            return null;
        }
    }

    public boolean readAttributesStart() throws IOException {
        return skipChar ( PdmlExtensionsConstants.ATTRIBUTES_START_CHAR );
    }

    public @Nullable TextToken readAttributesStartToken() throws IOException {

        TextLocation location = currentLocation();
        if ( readAttributesStart() ) {
            return new TextToken ( PdmlExtensionsConstants.ATTRIBUTES_START_CHAR, location );
        } else {
            return null;
        }
    }

    public boolean readAttributesEnd() throws IOException {
        return skipChar ( PdmlExtensionsConstants.ATTRIBUTES_END_CHAR );
    }

    public boolean readAttributeAssignSymbol() throws IOException {
        return skipChar ( PdmlExtensionsConstants.ATTRIBUTE_ASSIGN_CHAR );
    }


    // Extensions

    public boolean isAtExtensionStart() {
        return isAtChar ( PdmlExtensionsConstants.EXTENSION_START_CHAR );
    }

    public boolean readExtensionStart() throws IOException {
        return skipChar ( PdmlExtensionsConstants.EXTENSION_START_CHAR );
    }

    public @Nullable String readExtensionKindLetters() throws IOException {
        return charReader.readWhile ( Character::isLetter );
    }

    public @NotNull String readRawStringLiteral() throws IOException, MalformedPdmlException {

        @NotNull TextToken startToken = charReader.currentCharToken();
        try {
            return RawStringLiteralUtil.readLiteral ( charReader );
        } catch ( InvalidTextException e ) {
            String id = e.getErrorId();
            TextToken textToken = e.textToken();
            throw new MalformedPdmlException (
                e.getMessage(),
                id != null ? id : "INVALID_RAW_STRING_LITERAL",
                textToken != null ? textToken : startToken );
        }
    }

    public @NotNull String readMultilineStringLiteral() throws IOException, MalformedPdmlException {

        @NotNull TextToken startToken = charReader.currentCharToken();
        try {
            return MultilineStringLiteralUtil.readLiteral (
                charReader, MultilineStringLiteralUtil.DEFAULT_ESCAPE_MAP );
        } catch ( InvalidTextException e ) {
            String id = e.getErrorId();
            TextToken textToken = e.textToken();
            throw new MalformedPdmlException (
                e.getMessage(),
                id != null ? id : "INVALID_RAW_STRING_LITERAL",
                textToken != null ? textToken : startToken );
        }
    }


    // Reader Wrappers

    @Override
    public boolean isAtString ( @NotNull String s ) throws IOException {
        // if ( isAtEnd() ) return false;
        return charReader.isAtString ( s );
    }

    // Skip

    public boolean skipChar ( char c ) throws IOException {
        return super.skipChar ( c );
    }

    public boolean skipString ( @NotNull String string ) throws IOException {
        return charReader.skipString ( string );
    }

    public boolean skipSpaceOrTabOrLineBreak() throws IOException {
        return charReader.skipSpaceOrTabOrLineBreak();
    }

    // Read

    public @Nullable String readWhileAtChar ( char c ) throws IOException {
        return charReader.readWhileAtChar ( c );
    }

    public boolean skipAllWhileCharsMatch ( @NotNull String chars ) throws IOException {
        return charReader.skipAllWhileCharsMatch ( chars );
    }


    // Read-ahead

    public void setMark ( int readAheadLimit ) { charReader.setMark ( readAheadLimit ); }

    public void removeMark() { charReader.removeMark(); }

    public void goBackToMark() { charReader.goBackToMark(); }

    public boolean isNextChar ( char c ) throws IOException {
        return charReader.isNextChar ( c );
    }

    public @Nullable Character peekNextChar() throws IOException {
        return charReader.peekNextChar();
    }


    // Error Handling

    public MalformedPdmlException errorAtCurrentLocation (
        @NotNull String message, @NotNull String id ) {

        return new MalformedPdmlException ( message, id, currentCharToken () );
    }


    // Insert

    public void insertStringToRead ( @NotNull String string ) {
        charReader.insert ( string );
    }

    public void insertFileToRead ( @NotNull Path filePath ) throws IOException {
        charReader.insert ( filePath );
    }



    // Debugging

    public @NotNull String stateToString() { return charReader.stateToString(); }

    public void stateToOSOut ( @Nullable String label ) { charReader.stateToOSOut ( label ); }
}
