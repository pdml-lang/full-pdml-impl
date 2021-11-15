package dev.pdml.core.reader.reader;

import dev.pp.text.utilities.character.CharPredicate;
import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;

import java.io.File;

public interface PXMLReader {

    // pXML functions

    // node
    boolean consumeNodeStart() throws TextReaderException;
    boolean isAtNodeStart() throws TextReaderException;
    boolean consumeNodeEnd() throws PXMLResourceException;
    boolean isAtNodeEnd() throws TextReaderException;

    // name
    @Nullable String readName() throws PXMLResourceException;
    @Nullable TextToken readNameToken() throws PXMLResourceException;

    // text
    @Nullable String readText() throws TextReaderException;
    @Nullable TextToken readTextToken() throws TextReaderException;
    // TODO? PXMLToken readRawTextToken() throws IOException, PXMLDataException; // without unescaping -> faster

    // attributes
    @Nullable String readAttributeValue() throws TextReaderException;
    @Nullable TextToken readAttributeValueToken() throws TextReaderException;
    @Nullable String readQuotedAttributeValue() throws TextReaderException;
    @Nullable TextToken readQuotedAttributeValueToken() throws TextReaderException;
    @Nullable String readUnquotedAttributeValue() throws TextReaderException;
    @Nullable TextToken readUnquotedAttributeValueToken() throws TextReaderException;

    // comment
    @Nullable String readComment() throws PXMLResourceException, MalformedPXMLDocumentException;
    @Nullable TextToken readCommentToken() throws PXMLResourceException, MalformedPXMLDocumentException;
    boolean skipComment() throws PXMLResourceException, MalformedPXMLDocumentException;
    void skipWhitespaceAndComments() throws PXMLResourceException, MalformedPXMLDocumentException;
    boolean isAtStartOfComment() throws PXMLResourceException;
    // extension
    // boolean handleExtension () throws TextReaderException;

    // config
    // @NotNull PXMLReaderConfig getConfig();


    // text reader functions

    // iteration
    boolean hasChar();
    char currentChar();
    void advanceChar() throws PXMLResourceException;
    @Nullable String advanceCharWhile ( @NotNull CharPredicate predicate ) throws PXMLResourceException;

    // location
    @Nullable TextResource currentResource();
    @NotNull TextLocation currentLocation();
    @NotNull TextToken currentToken();

    // isAt
    boolean isAtChar ( char c );
    boolean isAt ( @NotNull CharPredicate predicate );
    boolean isAtString ( @NotNull String s ) throws PXMLResourceException;
    boolean isAtSpaceOrTabOrNewLine();

    // append
    boolean appendCharWhile ( CharPredicate predicate, StringBuilder sb ) throws PXMLResourceException;
    boolean appendOptionalNewLine ( StringBuilder sb ) throws PXMLResourceException;

    // skip
    boolean skipChar ( char c ) throws PXMLResourceException;
    boolean skipString ( @NotNull String string ) throws PXMLResourceException;
    void skipNChars ( long n ) throws PXMLResourceException;
    boolean skipOneNewline() throws PXMLResourceException;
    boolean skipOneSpaceOrTab() throws PXMLResourceException;
    boolean skipOneSpaceOrTabOrNewline() throws PXMLResourceException;
    boolean skipOptionalSpacesAndTabs() throws PXMLResourceException;
    boolean skipOptionalSpacesAndTabsAndNewLines() throws PXMLResourceException;

    // read
    // @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws PXMLResourceException;
    @Nullable String readWhileAtChar ( char c ) throws PXMLResourceException;
    @Nullable String readMaxNChars ( long n ) throws PXMLResourceException;
    @Nullable String readRemaining() throws PXMLResourceException;
    @Nullable String readOptionalSpacesAndTabs() throws PXMLResourceException;
    @Nullable String readOptionalNewLine() throws PXMLResourceException;
    @Nullable String readUntilEndOfLine ( boolean includeNewLine ) throws PXMLResourceException;

    // read-ahead
    void setMark ( int readAheadLimit );
    void goBackToMark();
    boolean isNextChar ( char c ) throws PXMLResourceException;
    // Character nextChar() throws PXMLResourceException;
    // @NotNull String peekCurrentString ( int length ) throws PXMLResourceException;
    char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws PXMLResourceException;

    // push
    void insertFileToRead ( @NotNull File file, @NotNull TextToken errorToken ) throws PXMLResourceException;
    void insertStringToRead ( @NotNull String string, @NotNull TextToken errorToken ) throws PXMLResourceException;
}
