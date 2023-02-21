package dev.pdml.reader;

import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pdml.reader.extensions.PdmlExtensionsHandler;
import dev.pdml.shared.constants.CorePdmlConstants;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pdml.shared.utilities.PdmlAttributeUtils;
import dev.pdml.shared.utilities.PdmlNameUtils;
import dev.pdml.shared.utilities.PdmlEscaper;
import dev.pp.basics.utilities.character.CharChecks;
import dev.pp.basics.utilities.character.CharPredicate;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public class PdmlReaderImpl implements PdmlReader {


    private final @NotNull CharReaderWithInserts charReader;

    private final @NotNull TextInspectionMessageHandler errorHandler;
    private final @Nullable PdmlExtensionsHandler extensionsHandler;
    private final @NotNull PdmlExtensionsContext extensionsContext;


    // Constructors

    public PdmlReaderImpl (
        @NotNull CharReaderWithInserts charReader,
        @NotNull PdmlReaderOptions options ) {

        this.charReader = charReader;

        this.errorHandler = options.errorHandler();
        this.extensionsHandler = options.extensionsHandler();
        this.extensionsContext = new PdmlExtensionsContext (
            this, this.errorHandler, options.scriptingEnvironment() );
    }

    public PdmlReaderImpl (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull PdmlReaderOptions options ) throws IOException {

        this ( CharReaderWithInsertsImpl.createAndAdvance ( reader, resource, lineOffset, columnOffset ), options );
    }

    public PdmlReaderImpl (
        @NotNull Reader reader,
        @Nullable TextResource resource ) throws IOException {

        this ( reader, resource, null, null, new PdmlReaderOptions() );
    }

    public PdmlReaderImpl (@NotNull Reader reader ) throws IOException {
        this ( reader, null );
    }

    public PdmlReaderImpl() throws IOException {
        this ( OSIO.standardInputUTF8Reader() );
    }


    // node

    public boolean consumeNodeStart() throws IOException, TextErrorException {

        boolean result = isAtNodeStart();
        if ( result ) advanceChar();
        return result;
    }

    public boolean isAtNodeStart() throws IOException, TextErrorException {

        // if ( ! isAtChar ( Constants.NODE_START ) ) return false;

        handleExtension();

        if ( ! isAtChar ( CorePdmlConstants.NODE_START ) ) return false;

        if ( isNextChar ( PdmlExtensionsConstants.COMMENT_SYMBOL ) ) return false;

        return true;
    }

    public boolean consumeNodeEnd() throws IOException {

        return skipChar ( CorePdmlConstants.NODE_END );
    }

    public boolean isAtNodeEnd() {

        return isAtChar ( CorePdmlConstants.NODE_END );
    }

    // name

    public @Nullable String readName() throws IOException {

        char firstChar = currentChar();
        if ( ! PdmlNameUtils.isValidFirstCharOfName ( firstChar ) ) return null;

        advanceChar ();
        final StringBuilder sb = new StringBuilder();
        sb.append ( firstChar );

        appendCharWhile ( PdmlNameUtils::isValidCharOfName, sb );

        return sb.toString();
    }

    public @Nullable TextToken readNameToken() throws IOException {

        TextLocation location = currentLocation();
        String name = readName();
        if ( name != null ) {
            return new TextToken ( name, location );
        } else {
            return null;
        }
    }


    // text

    public @Nullable String readText() throws IOException, TextErrorException {

        final StringBuilder result = new StringBuilder();

        while ( hasChar() ) {

            handleExtension();

            if ( isAtChar ( CorePdmlConstants.NODE_START ) ||
                isAtChar ( CorePdmlConstants.NODE_END ) ) break;

            final char currentChar = currentChar();
            if ( PdmlEscaper.isEscapeCharacter ( currentChar ) ) {
                appendEscapedCharacter ( result, false );
            } else {
                result.append ( currentChar );
            }

            advanceChar();
        }

        return result.length() == 0 ? null : result.toString();
    }

    public @Nullable TextToken readTextToken() throws IOException, TextErrorException {

        TextLocation location = currentLocation();
        String text = readText();
        if ( text != null ) {
            return new TextToken ( text, location );
        } else {
            return null;
        }
    }



    // attributes

    public @Nullable String readAttributeValue() throws IOException, TextErrorException {

        final String result = readQuotedAttributeValue();
        if ( result != null ) return result;

        return readUnquotedAttributeValue();
    }

    public @Nullable TextToken readAttributeValueToken() throws IOException, TextErrorException {

        final TextToken result = readQuotedAttributeValueToken();
        if ( result != null ) return result;

        return readUnquotedAttributeValueToken();
    }

    public @Nullable String readQuotedAttributeValue() throws IOException, TextErrorException {

        final char quoteChar = currentChar();

        if ( quoteChar != PdmlExtensionsConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE &&
            quoteChar != PdmlExtensionsConstants.ATTRIBUTE_VALUE_SINGLE_QUOTE ) return null;

        final TextToken startToken = currentToken();

        advanceChar(); // consume quote

        final StringBuilder result = new StringBuilder();

        while ( true ) {

            handleExtension();

            if ( ! hasChar() ) throw cancelingError (
                "Missing " + quoteChar + " to end the value. A value quoted with " + quoteChar + " must be ended by a subsequent " + quoteChar + ".",
                "MISSING_END_QUOTE",
                startToken );

            final char nextChar = currentChar ();

            if ( nextChar == quoteChar ) {
                advanceChar (); // consume quote
                break;
            }

            if ( PdmlEscaper.isEscapeCharacter ( nextChar ) ) {
                appendEscapedCharacter ( result, true );
            } else {
                result.append ( nextChar );
            }

            advanceChar ();
        }

        return result.toString();
        // return result.isEmpty() ? null : result.toString();
    }

    public @Nullable TextToken readQuotedAttributeValueToken() throws IOException, TextErrorException {

        // TextLocation location = currentLocation();
        TextLocation currentLocation = currentLocation();
        // add 1 to column to start just after the " or '
        TextLocation location = new TextLocation (
            currentLocation.getResource(),
            currentLocation.getLineNumber(),
            currentLocation.getColumnNumber() + 1,
            currentLocation.getParentLocation() );
        String value = readQuotedAttributeValue();
        if ( value != null ) {
            return new TextToken ( value, location );
        } else {
            return null;
        }
    }
/*
    public @Nullable String readUnquotedAttributeValue() throws PXMLResourceException {

        if ( ! hasChar() ) return null;

        return advanceCharWhile ( PXMLReaderHelper::isValidCharOfUnquotedAttributeValue );
    }
*/

    public @Nullable String readUnquotedAttributeValue() throws IOException, TextErrorException {

        final StringBuilder result = new StringBuilder();

        while ( hasChar() ) {

            handleExtension();

            char currentChar = currentChar();
            if ( ! PdmlAttributeUtils.isValidCharInUnquotedValue ( currentChar ) ) break;

            result.append ( currentChar );

            advanceChar();
        }

        return result.length () == 0 ? null : result.toString ();
    }

    public @Nullable TextToken readUnquotedAttributeValueToken() throws IOException, TextErrorException {

        TextLocation location = currentLocation();
        String value = readUnquotedAttributeValue();
        if ( value != null ) {
            return new TextToken ( value, location );
        } else {
            return null;
        }
    }

    // comment

    public @Nullable String readComment() throws IOException, PdmlDocumentSyntaxException {

        if ( ! isAtStartOfComment() ) return null;

        StringBuilder result = new StringBuilder();
        readCommentSnippet ( result );

        return result.toString();
    }

    public @Nullable TextToken readCommentToken() throws IOException, PdmlDocumentSyntaxException {

        TextLocation location = currentLocation();
        String text = readComment();
        if ( text != null ) {
            return new TextToken ( text, location );
        } else {
            return null;
        }
    }

    public boolean skipComment() throws IOException, PdmlDocumentSyntaxException {

        // can be made faster by writing a specific version that doesn't use a StringBuilder
        // to build and return the comment's content
        return readComment() != null;
    }

    private void readCommentSnippet ( @NotNull StringBuilder result ) throws IOException, PdmlDocumentSyntaxException {

        TextLocation location = currentLocation();
        TextToken token = currentToken();

        // we are at the start of a comment
        result.append ( currentChar() );
        advanceChar ();
        result.append ( currentChar() );
        advanceChar ();

        while ( true ) {

            if ( ! hasChar () ) throw cancelingError (
                "The comment starting at line " + location.getLineNumber() +
                        ", column " + location.getColumnNumber() + " is never closed.",
                "UNCLOSED_COMMENT",
                token );

            if ( isAtEndOfComment() ) {
                result.append ( currentChar() );
                advanceChar ();
                result.append ( currentChar() );
                advanceChar ();
                return;

            } else if ( isAtStartOfComment() ) {
                readCommentSnippet ( result ); // recursive call for nested comments

            } else {
                result.append ( currentChar () );
                if ( hasChar () ) advanceChar ();
            }
        }
    }

    public boolean isAtStartOfComment() throws IOException {

        if ( ! hasChar() ) return false;

        return isAtString ( PdmlExtensionsConstants.COMMENT_START );
    }

    private boolean isAtEndOfComment() throws IOException {

        if ( ! hasChar() ) return false;

        return isAtString ( PdmlExtensionsConstants.COMMENT_END );
    }

    public void skipWhitespaceAndComments() throws IOException, PdmlDocumentSyntaxException {

        if ( ! hasChar () ) return;

        while ( skipOptionalSpacesAndTabsAndNewLines () || skipComment() ) {}
    }


    // extension

    private boolean handleExtension() throws IOException, TextErrorException {

        if ( extensionsHandler == null ) {
            return false;
        } else {
            return extensionsHandler.handleExtension ( extensionsContext );
        }
    }


    // reader wrappers

    // iteration

    public boolean hasChar() { return charReader.hasChar(); }

    public char currentChar() { return charReader.currentChar(); }

    public void advanceChar() throws IOException {
        charReader.advance();
    }

    public @Nullable String advanceCharWhile ( @NotNull CharPredicate predicate ) throws IOException {
        return charReader.advanceWhile ( predicate );
    }

    // location

    public @NotNull CharReaderWithInserts currentCharReader() { return charReader; }

    public @Nullable TextResource currentResource() { return charReader.resource(); }

    public @NotNull TextLocation currentLocation() { return charReader.currentLocation(); }

    public @NotNull TextToken currentToken() { return new TextToken ( currentChar(), currentLocation() ); }

    // isAt

//    public boolean isAtChar ( char c ) { return textReader.isAtChar ( c ); }

    public boolean isAtChar ( char c ) { return charReader.isAtChar ( c ); }

    public boolean isAt ( @NotNull CharPredicate predicate ) { return charReader.isAt ( predicate ); }

    public boolean isAtString ( @NotNull String s ) throws IOException {
        // if ( ! hasChar () ) return false;
        return charReader.isAtString ( s );
    }

    public boolean isAtNewLine () { return charReader.isAt ( CharChecks::isNewLine ); }

    public boolean isAtSpaceOrTabOrNewLine () { return charReader.isAt ( CharChecks::isSpaceOrTabOrNewLine ); }

    // append

    public boolean appendCharWhile ( CharPredicate predicate, StringBuilder sb ) throws IOException {
        // if ( ! hasChar () ) return false;
        return charReader.appendWhile ( predicate, sb );
    }

    public boolean appendOptionalNewLine ( StringBuilder sb ) throws IOException {

        boolean isWindowsNewLine = charReader.appendCurrentCharIfAndAdvance ( CharChecks::isWindowsNewLineStart, sb );
        boolean isUnixNewLine = charReader.appendCurrentCharIfAndAdvance ( CharChecks::isUnixNewLine, sb );
        return isWindowsNewLine || isUnixNewLine;
    }

    // skip

    public boolean skipChar ( char c ) throws IOException {
        return charReader.skipChar ( c );
    }

    public boolean skipString ( @NotNull String string ) throws IOException {
        return charReader.skipString ( string );
    }

    public void skipNChars ( long n ) throws IOException {
        charReader.skipNChars ( n );
    }

    public boolean skipOneNewline () throws IOException {

        charReader.skipChar ( '\r');
        return charReader.skipChar ( '\n');
    }

    public boolean skipOneSpaceOrTab() throws IOException {
        return charReader.skipIf ( CharChecks::isSpaceOrTab );
    }

    public boolean skipOneSpaceOrTabOrNewline () throws IOException {

        boolean firstCharIsCR = charReader.isAtChar ( '\r' );
        boolean skipped = charReader.skipIf ( CharChecks::isSpaceOrTabOrNewLine );
        if ( firstCharIsCR ) charReader.skipChar ( '\n');
        return skipped;
    }

    public boolean skipOptionalSpacesAndTabs() throws IOException {
        return charReader.skipWhile ( CharChecks::isSpaceOrTab );
    }

    public boolean skipOptionalSpacesAndTabsAndNewLines() throws IOException {
        return charReader.skipWhile ( CharChecks::isSpaceOrTabOrNewLine );
    }


    // read

    /*
    public @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException {
        return charReader.readWhile ( predicate );
    }
    */

    public @Nullable String readWhileAtChar ( char c ) throws IOException {
        return charReader.readWhileAtChar ( c );
    }

    public @Nullable String readMaxNChars ( long n ) throws IOException {
        return charReader.readMaxNChars ( n );
    }

    public @Nullable String readOptionalSpacesAndTabs() throws IOException {
        return charReader.readWhile ( CharChecks::isSpaceOrTab );
    }

    public @Nullable String readOptionalSpacesAndTabsAndNewLines() throws IOException {
        return charReader.readWhile ( CharChecks::isSpaceOrTabOrNewLine );
    }

    public @Nullable String readOptionalNewLine() throws IOException {

        char firstChar = charReader.currentChar();
        if ( CharChecks.isNotNewLine ( firstChar ) ) return null;

        charReader.advance();
        if ( firstChar == '\n' ) {
            return StringConstants.UNIX_NEW_LINE;
        }

        assert firstChar == '\r';
        assert charReader.currentChar() == '\n';
        charReader.advance();
        return StringConstants.WINDOWS_NEW_LINE;
    }

    public @Nullable String readUntilEndOfLine ( boolean includeNewLine ) throws IOException {

        StringBuilder sb = new StringBuilder();
        charReader.appendWhile ( CharChecks::isNotNewLine, sb );
        if ( includeNewLine ) {
            String newLine = readOptionalNewLine();
            if ( newLine != null ) sb.append ( newLine );
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public @Nullable String readRemaining() throws IOException {
        return charReader.readRemaining();
    }

    // read-ahead

    public void setMark ( int readAheadLimit ) { charReader.setMark ( readAheadLimit ); }

    public void goBackToMark() { charReader.goBackToMark(); }

    public boolean isNextChar ( char c ) throws IOException {
        return charReader.isNextChar ( c );
    }

    public @Nullable String peekNextMaxNChars ( int n ) throws IOException {
        return charReader.peekNextMaxNChars ( n );
    }

    /*
    public Character nextChar() throws IOException {
        return charReader.peekNextChar();
    }
    */

    public char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws IOException {

/*
        assert charReader != null;
        if ( charReader == null ) throw new RuntimeException ( "QQQ");
//            return charReader.peekCharAfter ( CharChecks::isSpaceOrTabOrNewLine, lookAhead );
        if ( charReader.hasChar() ) {
            return charReader.peekCharAfter ( CharChecks::isSpaceOrTabOrNewLine, lookAhead );
        } else {
            return 0;
        }
*/

        @Nullable Character c = charReader.peekCharAfterOptional ( CharChecks::isSpaceOrTabOrNewLine, lookAhead );
        return c != null ? c : 0;
    }


    // push

    /*
    public void insertFileToRead ( @NotNull File file, @NotNull TextToken errorToken ) throws IOException {
        charReader.push ( file );
        // advanceChar ();
    }
    */

    public void insertFileToRead ( @NotNull Path filePath ) throws IOException {
        charReader.insert ( filePath );
    }

    public void insertStringToRead ( @NotNull String string ) {
        charReader.insert ( string );
    }


    // private helper methods

    // character escaping

    private void appendEscapedCharacter ( StringBuilder result, boolean forQuotedAttribute )
        throws IOException, PdmlDocumentSyntaxException {

        // now positioned at the \ of \u1234

        advanceChar(); // consume \

        if ( ! hasChar() ) throw cancelingErrorAtCurrentLocation (
            "Expecting another character after the escape character '" + CorePdmlConstants.ESCAPE_CHARACTER +
                "' at the end of the document.",
            "MISSING_ESCAPED_CHARACTER" );

        final char firstChar = currentChar();

        switch ( firstChar ) {
            case '[':
            case ']':
            case '\\':
                result.append ( firstChar );
                return;
            case 't':
                result.append ( '\t' );
                return;
            case 'r':
                result.append ( '\r' );
                return;
            case 'n':
                result.append ( '\n' );
                return;
            case 'u':
                appendUnicodeEscapeSequence ( result, 4 );
                return;
            case 'U':
                appendUnicodeEscapeSequence ( result, 8 );
                return;
            default:
                if ( ! forQuotedAttribute ) {
                    if ( firstChar == PdmlExtensionsConstants.ALTERNATIVE_ATTRIBUTES_START ||
                        firstChar == PdmlExtensionsConstants.ALTERNATIVE_ATTRIBUTES_END ) {
                        result.append ( firstChar );
                        return;
                    }
                } else {
                    if ( firstChar == PdmlExtensionsConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE ||
                        firstChar == PdmlExtensionsConstants.ATTRIBUTE_VALUE_SINGLE_QUOTE ) {
                        result.append ( firstChar );
                        return;
                    }
                }

                nonCancelingErrorAtCurrentLocation (
                    "Invalid character escape sequence \"" + CorePdmlConstants.ESCAPE_CHARACTER + firstChar + "\".",
                    "INVALID_ESCAPED_CHARACTER" );
        }
    }

    private void appendUnicodeEscapeSequence ( StringBuilder result, int hexCount ) throws IOException, PdmlDocumentSyntaxException {

        // now positioned at the u of \u1234

        TextToken token = currentToken();

        StringBuilder hexSb = new StringBuilder();
        for ( int i = 1; i <= hexCount; i++ ) {
            advanceChar();
            if ( ! hasChar() ) throw cancelingError (
                "Expecting " + hexCount + " hex digits to define a Unicode escape sequence. But found only " + (i - 1) + ".",
                "INVALID_UNICODE_ESCAPE",
                token );
            char hexChar = requireHexChar ( currentChar() );
            hexSb.append ( hexChar );
        }

        // NumberFormatException should never happen because the validity has been checked already
        try {
            int codePoint = Integer.parseInt( hexSb.toString(), 16 );
            result.appendCodePoint ( codePoint );
        } catch ( NumberFormatException e ) {
            throw new RuntimeException ( e );
        }
    }

    private char requireHexChar ( char c ) {

        if ( ( c >= '0' && c <= '9' )
            || ( c >= 'a' && c <= 'f' )
            || ( c >= 'A' && c <= 'F' ) ) {
            return c;
        } else {
            nonCancelingErrorAtCurrentLocation (
            "Invalid hexadecimal character '" + c + "'. Only 0..9, a..f, and A..F are allowed.",
                "INVALID_HEX_CHAR" );
            return '0';
        }
    }


    // debugging

    public @NotNull String stateToString() { return charReader.stateToString(); }

    public void stateToOSOut ( @Nullable String label ) { charReader.stateToOSOut ( label ); }

    // errors

    private void nonCancelingErrorAtCurrentLocation ( @NotNull String message, @NotNull String id ) {

        PdmlReaderErrorHelper.nonAbortingErrorAtCurrentLocation ( message, id, this, errorHandler );
    }

    private PdmlDocumentSyntaxException cancelingErrorAtCurrentLocation (
        @NotNull String message, @NotNull String id ) {

        return PdmlReaderErrorHelper.abortingSyntaxErrorAtCurrentLocation ( message, id, this, errorHandler );
    }

    private PdmlDocumentSyntaxException cancelingError (
        @NotNull String message, @NotNull String id, @NotNull TextToken token ) {

        return PdmlReaderErrorHelper.abortingSyntaxError ( message, id, token, errorHandler );
    }
}
