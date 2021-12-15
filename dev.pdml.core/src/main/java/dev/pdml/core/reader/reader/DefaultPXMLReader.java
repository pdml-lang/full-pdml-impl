package dev.pdml.core.reader.reader;

import dev.pdml.core.reader.parser.ParserHelper;
import dev.pp.text.utilities.character.CharChecks;
import dev.pp.text.utilities.character.CharPredicate;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.stack.DefaultStackedCharReader;
import dev.pp.text.reader.stack.StackedCharReader;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.string.StringConstants;
import dev.pdml.core.PDMLConstants;
import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pdml.core.reader.reader.extensions.PXMLExtensionsHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class DefaultPXMLReader implements PXMLReader {


    private final @NotNull StackedCharReader charReader;
    private final @NotNull PXMLReaderConfig config;

    private final @NotNull TextErrorHandler errorHandler;

    private final @Nullable PXMLExtensionsHandler extensionsHandler;
    private final @NotNull ExtensionsContext extensionsContext;


    // Constructors

    // Reader

    public DefaultPXMLReader (
        @NotNull Reader reader,
        @NotNull PXMLReaderConfig config ) throws PXMLResourceException {

        try {
            this.charReader = new DefaultStackedCharReader ( reader, config.getResource() );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
        this.config = config;

        this.errorHandler = config.getErrorHandler();
        this.extensionsHandler = config.getExtensionsHandler();

        this.extensionsContext = new ExtensionsContext ( this, this.errorHandler );
    }

    public DefaultPXMLReader ( @NotNull Reader reader, @Nullable TextResource resource ) throws PXMLResourceException {

        this ( reader, new PXMLReaderConfig ( resource ) );
    }

    public DefaultPXMLReader ( @NotNull String string ) throws PXMLResourceException {

        this ( new StringReader ( string ), new String_TextResource ( string) );
    }

    public DefaultPXMLReader ( @NotNull File file ) throws PXMLResourceException, IOException {

        this ( FileUtilities.getUTF8FileReader ( file ), new File_TextResource ( file ) );
    }


    // node

    public boolean consumeNodeStart() throws TextReaderException {

        boolean result = isAtNodeStart();
        if ( result ) advanceChar();
        return result;
    }

    public boolean isAtNodeStart() throws TextReaderException {

        // if ( ! isAtChar ( Constants.NODE_START ) ) return false;

        handleExtension();

        if ( ! isAtChar ( PDMLConstants.NODE_START ) ) return false;

        if ( isNextChar ( PDMLConstants.COMMENT_SYMBOL ) ) return false;

        return true;
    }

    public boolean consumeNodeEnd() throws PXMLResourceException {

        return skipChar ( PDMLConstants.NODE_END );
    }

    public boolean isAtNodeEnd() throws PXMLResourceException {

        return isAtChar ( PDMLConstants.NODE_END );
    }

    // name

    public @Nullable String readName() throws PXMLResourceException {

        char firstChar = currentChar();
        if ( ! PXMLReaderHelper.isValidFirstCharOfXMLName ( firstChar ) ) return null;

        advanceChar ();
        final StringBuilder sb = new StringBuilder();
        sb.append ( firstChar );

        appendCharWhile ( PXMLReaderHelper::isValidCharOfXMLName, sb );

        return sb.toString();
    }

    public @Nullable TextToken readNameToken() throws PXMLResourceException {

        TextLocation location = currentLocation();
        String name = readName();
        if ( name != null ) {
            return new TextToken ( name, location );
        } else {
            return null;
        }
    }


    // text

    public @Nullable String readText() throws TextReaderException {

        final StringBuilder result = new StringBuilder();

        while ( hasChar() ) {

            handleExtension ();

            if ( isAtChar ( PDMLConstants.NODE_START ) ||
                isAtChar ( PDMLConstants.NODE_END ) ) break;

            final char currentChar = currentChar();
            if ( PXMLReaderHelper.isEscapeCharacter ( currentChar ) ) {
                appendEscapedCharacter ( result, false );
            } else {
                result.append ( currentChar );
            }

            advanceChar ();
        }

        return result.length() == 0 ? null : result.toString();
    }

    public @Nullable TextToken readTextToken() throws TextReaderException {

        TextLocation location = currentLocation();
        String text = readText();
        if ( text != null ) {
            return new TextToken ( text, location );
        } else {
            return null;
        }
    }



    // attributes

    public @Nullable String readAttributeValue() throws TextReaderException {

        final String result = readQuotedAttributeValue();
        if ( result != null ) return result;

        return readUnquotedAttributeValue();
    }

    public @Nullable TextToken readAttributeValueToken() throws TextReaderException {

        TextLocation location = currentLocation();
        String value = readAttributeValue();
        if ( value != null ) {
            return new TextToken ( value, location );
        } else {
            return null;
        }
    }

    public @Nullable String readQuotedAttributeValue() throws TextReaderException {

        final char quoteChar = currentChar();

        if ( quoteChar != PDMLConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE &&
            quoteChar != PDMLConstants.ATTRIBUTE_VALUE_SINGLE_QUOTE ) return null;

        final TextToken startToken = currentToken();

        advanceChar(); // consume quote

        final StringBuilder result = new StringBuilder();

        while ( true ) {

            handleExtension ();

            if ( ! hasChar() ) throw cancelingError (
                "MISSING_END_QUOTE",
                "Missing " + quoteChar + " to end the value. A value quoted with " + quoteChar + " must be ended by a subsequent " + quoteChar + ".", startToken );

            final char nextChar = currentChar ();

            if ( nextChar == quoteChar ) {
                advanceChar (); // consume quote
                break;
            }

            if ( PXMLReaderHelper.isEscapeCharacter ( nextChar ) ) {
                appendEscapedCharacter ( result, true );
            } else {
                result.append ( nextChar );
            }

            advanceChar ();
        }

        return result.toString();
    }

    public @Nullable TextToken readQuotedAttributeValueToken() throws TextReaderException {

        TextLocation location = currentLocation();
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

    public @Nullable String readUnquotedAttributeValue() throws TextReaderException {

        final StringBuilder result = new StringBuilder();

        while ( hasChar() ) {

            handleExtension ();

            char currentChar = currentChar();
            if ( ! PXMLReaderHelper.isValidCharOfUnquotedAttributeValue ( currentChar ) ) break;

            result.append ( currentChar );

            advanceChar();
        }

        return result.length () == 0 ? null : result.toString ();
    }

    public @Nullable TextToken readUnquotedAttributeValueToken() throws TextReaderException {

        TextLocation location = currentLocation();
        String value = readUnquotedAttributeValue();
        if ( value != null ) {
            return new TextToken ( value, location );
        } else {
            return null;
        }
    }

    // comment

    public @Nullable String readComment() throws PXMLResourceException, MalformedPXMLDocumentException {

        if ( ! isAtStartOfComment() ) return null;

        StringBuilder result = new StringBuilder();
        readCommentSnippet ( result );

        return result.toString();
    }

    public @Nullable TextToken readCommentToken() throws PXMLResourceException, MalformedPXMLDocumentException {

        TextLocation location = currentLocation();
        String text = readComment();
        if ( text != null ) {
            return new TextToken ( text, location );
        } else {
            return null;
        }
    }

    public boolean skipComment() throws PXMLResourceException, MalformedPXMLDocumentException {

        // can be made faster by writing a specific version that doesn't use a StringBuilder
        // to build and return the comment's content
        return readComment() != null;
    }

    private void readCommentSnippet ( @NotNull StringBuilder result ) throws PXMLResourceException, MalformedPXMLDocumentException {

        TextLocation location = currentLocation();
        TextToken token = currentToken();

        // we are at the start of a comment
        result.append ( currentChar() );
        advanceChar ();
        result.append ( currentChar() );
        advanceChar ();

        while ( true ) {

            if ( ! hasChar () ) throw cancelingError (
                "UNCLOSED_COMMENT",
                "The comment starting at line " + location.getLineNumber() +
                        ", column " + location.getColumnNumber() + " is never closed.",
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

    public boolean isAtStartOfComment() throws PXMLResourceException {

        if ( ! hasChar() ) return false;

        return isAtString ( PDMLConstants.COMMENT_START );
    }

    private boolean isAtEndOfComment() throws PXMLResourceException {

        if ( ! hasChar() ) return false;

        return isAtString ( PDMLConstants.COMMENT_END );
    }

    public void skipWhitespaceAndComments() throws PXMLResourceException, MalformedPXMLDocumentException {

        if ( ! hasChar () ) return;

        while ( skipOptionalSpacesAndTabsAndNewLines () || skipComment() ) {}
    }


    // extension

    private boolean handleExtension () throws TextReaderException {

        if ( extensionsHandler == null ) {
            return false;
        } else {
            return extensionsHandler.handleExtension ( extensionsContext );
        }
    }


    // config
    // public @NotNull PXMLReaderConfig getConfig() { return config; }


    // reader wrappers

    // iteration

    public boolean hasChar() { return charReader.hasChar(); }

    public char currentChar() { return charReader.currentChar(); }

    public void advanceChar() throws PXMLResourceException {

        try {
            charReader.advance();
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public @Nullable String advanceCharWhile ( @NotNull CharPredicate predicate ) throws PXMLResourceException {

        // if ( ! hasChar () ) return null;

        try {
            return charReader.advanceWhile ( predicate );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    // location

    public @Nullable TextResource currentResource() { return charReader.resource(); }

    public @NotNull TextLocation currentLocation () { return charReader.currentLocation(); }

    public @NotNull TextToken currentToken() { return new TextToken ( currentChar(), currentLocation() ); }

    // isAt

//    public boolean isAtChar ( char c ) { return textReader.isAtChar ( c ); }

    public boolean isAtChar ( char c ) { return charReader.isAtChar ( c ); }

    public boolean isAt ( @NotNull CharPredicate predicate ) { return charReader.isAt ( predicate ); }

    public boolean isAtString ( @NotNull String s ) throws PXMLResourceException {

        // if ( ! hasChar () ) return false;

        try {
            return charReader.isAtString ( s );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean isAtSpaceOrTabOrNewLine () { return charReader.isAt ( CharChecks::isSpaceOrTabOrNewLine ); }

    // append

    public boolean appendCharWhile ( CharPredicate predicate, StringBuilder sb ) throws PXMLResourceException {

        // if ( ! hasChar () ) return false;

        try {
            return charReader.appendCurrentCharWhile ( predicate, sb );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean appendOptionalNewLine ( StringBuilder sb ) throws PXMLResourceException {

        try {
            boolean isWindowsNewLine = charReader.appendCurrentCharIfAndAdvance ( CharChecks::isWindowsNewLineStart, sb );
            boolean isUnixNewLine = charReader.appendCurrentCharIfAndAdvance ( CharChecks::isUnixNewLine, sb );
            return isWindowsNewLine || isUnixNewLine;
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    // skip

    public boolean skipChar ( char c ) throws PXMLResourceException {

        try {
            return charReader.skipChar ( c );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean skipString ( @NotNull String string ) throws PXMLResourceException {

        try {
            return charReader.skipString ( string );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public void skipNChars ( long n ) throws PXMLResourceException{

        try {
            charReader.skipNChars ( n );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean skipOneNewline () throws PXMLResourceException {

        try {
            charReader.skipChar ( '\r');
            return charReader.skipChar ( '\n');
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean skipOneSpaceOrTab() throws PXMLResourceException {

        try {
            return charReader.skipIf ( CharChecks::isSpaceOrTab );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean skipOneSpaceOrTabOrNewline () throws PXMLResourceException {

        try {
            boolean firstCharIsCR = charReader.isAtChar ( '\r' );
            boolean skipped = charReader.skipIf ( CharChecks::isSpaceOrTabOrNewLine );
            if ( firstCharIsCR ) charReader.skipChar ( '\n');
            return skipped;
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean skipOptionalSpacesAndTabs() throws PXMLResourceException {

        try {
            return charReader.skipWhile ( CharChecks::isSpaceOrTab );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public boolean skipOptionalSpacesAndTabsAndNewLines() throws PXMLResourceException {

        try {
            return charReader.skipWhile ( CharChecks::isSpaceOrTabOrNewLine );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }


    // read

    /*
    public @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws PXMLResourceException {

        try {
            return charReader.readWhile ( predicate );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }
    */

    public @Nullable String readWhileAtChar ( char c ) throws PXMLResourceException {

        try {
            return charReader.readWhileAtChar ( c );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public @Nullable String readMaxNChars ( long n ) throws PXMLResourceException {

        try {
            return charReader.readMaxNChars ( n );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public @Nullable String readOptionalSpacesAndTabs() throws PXMLResourceException {

        try {
            return charReader.readWhile ( CharChecks::isSpaceOrTab );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public @Nullable String readOptionalNewLine() throws PXMLResourceException {

        char firstChar = charReader.currentChar();
        if ( CharChecks.isNotNewLine ( firstChar ) ) return null;

        try {
            charReader.advance();
            if ( firstChar == '\n' ) {
                return StringConstants.UNIX_NEW_LINE;
            }

            assert firstChar == '\r';
            assert charReader.currentChar() == '\n';
            charReader.advance();
            return StringConstants.WINDOWS_NEW_LINE;

        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public @Nullable String readUntilEndOfLine ( boolean includeNewLine ) throws PXMLResourceException {

        StringBuilder sb = new StringBuilder();
        try {
            charReader.appendCurrentCharWhile ( CharChecks::isNotNewLine, sb );
            if ( includeNewLine ) {
                String newLine = readOptionalNewLine();
                if ( newLine != null ) sb.append ( newLine );
            }
            return sb.length() == 0 ? null : sb.toString();
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    public @Nullable String readRemaining() throws PXMLResourceException {

        try {
            return charReader.readRemaining();
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    // read-ahead

    public void setMark ( int readAheadLimit ) { charReader.setMark ( readAheadLimit ); }

    public void goBackToMark() { charReader.goBackToMark(); }

    public boolean isNextChar ( char c ) throws PXMLResourceException {

        try {
            return charReader.isNextChar ( c );
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }

    /*
    public Character nextChar() throws PXMLResourceException {

        try {
            return charReader.peekNextChar();
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }
    */

    public char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws PXMLResourceException {

        try {
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
            if ( c != null ) {
                return c;
            } else {
                return 0;
            }
        } catch ( IOException e ) {
            throw wrapIOExceptionAtCurrentLocation ( e );
        }
    }


    // push

    public void insertFileToRead ( @NotNull File file, @NotNull TextToken errorToken )
        throws PXMLResourceException {

        try {
            charReader.push ( file );
            // advanceChar ();
        } catch ( IOException e ) {
            throw wrapIOException ( e, errorToken );
        }
    }

    public void insertStringToRead ( @NotNull String string, @NotNull TextToken errorToken )
        throws PXMLResourceException {

        try {
            charReader.push ( string );
            // advanceChar ();
        } catch ( IOException e ) {
            throw wrapIOException ( e, errorToken );
        }
    }




    // private helper methods

    // character escaping

    private void appendEscapedCharacter ( StringBuilder result, boolean forQuotedAttribute )
        throws PXMLResourceException, MalformedPXMLDocumentException {

        // now positioned at the \ of \u1234

        advanceChar(); // consume \

        if ( ! hasChar() ) throw cancelingErrorAtCurrentLocation (
            "MISSING_ESCAPED_CHARACTER",
            "Expecting another character after the escape character '" + PDMLConstants.ESCAPE_CHARACTER +
                "' at the end of the document."
        );

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
                    if ( firstChar == PDMLConstants.ATTRIBUTES_START || firstChar == PDMLConstants.ATTRIBUTES_END ) {
                        result.append ( firstChar );
                        return;
                    }
                } else {
                    if ( firstChar == PDMLConstants.ATTRIBUTE_VALUE_DOUBLE_QUOTE || firstChar == PDMLConstants.ATTRIBUTE_VALUE_SINGLE_QUOTE ) {
                        result.append ( firstChar );
                        return;
                    }
                }

                nonCancelingErrorAtCurrentLocation (
                    "INVALID_ESCAPED_CHARACTER",
                    "Invalid character escape sequence \"" + PDMLConstants.ESCAPE_CHARACTER + firstChar + "\"." );
        }
    }

    private void appendUnicodeEscapeSequence ( StringBuilder result, int hexCount ) throws PXMLResourceException, MalformedPXMLDocumentException {

        // now positioned at the u of \u1234

        TextToken token = currentToken();

        StringBuilder hexSb = new StringBuilder();
        for ( int i = 1; i <= hexCount; i++ ) {
            advanceChar();
            if ( ! hasChar() ) throw cancelingError (
                "INVALID_UNICODE_ESCAPE",
                "Expecting " + hexCount + " hex digits to define a Unicode escape sequence. But found only " + (i - 1) + ".",
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
            "INVALID_HEX_CHAR",
        "Invalid hexadecimal character '" + c + "'. Only 0..9, a..f, and A..F are allowed." );
            return '0';
        }
    }


    // errors

    private void nonCancelingErrorAtCurrentLocation ( @NotNull String id, @NotNull String message ) {

        ParserHelper.nonCancelingErrorAtCurrentLocation ( id, message, this, errorHandler );
    }

    private MalformedPXMLDocumentException cancelingErrorAtCurrentLocation (
        @NotNull String id, @NotNull String message ) {

        return ParserHelper.cancelingErrorAtCurrentLocation ( id, message, this, errorHandler );
    }

    private MalformedPXMLDocumentException cancelingError (
        @NotNull String id, @NotNull String message, @NotNull TextToken token ) {

        return ParserHelper.cancelingError ( id, message, token, errorHandler );
    }

    private PXMLResourceException wrapIOExceptionAtCurrentLocation ( @NotNull IOException ioException ) {

        return wrapIOException ( ioException, currentToken() );
    }

    private PXMLResourceException wrapIOException(
        @NotNull IOException exception,
        @NotNull TextToken token ) {

        String id = "IO_ERROR";

        String resourceName = token.getResourceName();
        String message = "The following error occurred while reading";
        if ( resourceName != null ) {
            message = message + " " + resourceName;
        }
        message = message + ": " + exception.getMessage();

        errorHandler.handleError ( id, message, token );

        return new PXMLResourceException ( id, message, token, exception );
    }
}
