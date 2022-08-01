package dev.pdml.core.reader;

import dev.pp.basics.utilities.character.CharPredicate;
import dev.pdml.core.exception.PDMLDocumentSyntaxException;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface PDMLReader {

    // pXML functions

    // node
    boolean consumeNodeStart() throws IOException, TextErrorException;
    boolean isAtNodeStart() throws IOException, TextErrorException;
    boolean consumeNodeEnd() throws IOException;
    boolean isAtNodeEnd();

    // name
    @Nullable String readName() throws IOException;
    @Nullable TextToken readNameToken() throws IOException;

    // text
    @Nullable String readText() throws IOException, TextErrorException;
    @Nullable TextToken readTextToken() throws IOException, TextErrorException;
    // TODO? PXMLToken readRawTextToken() throws IOException, PDMLDocumentException; // without unescaping -> faster

    // attributes
    @Nullable String readAttributeValue() throws IOException, TextErrorException;
    @Nullable TextToken readAttributeValueToken() throws IOException, TextErrorException;
    // TODO throws PDMLDocumentSyntaxException
    @Nullable String readQuotedAttributeValue() throws IOException, TextErrorException;
    @Nullable TextToken readQuotedAttributeValueToken() throws IOException, TextErrorException;
    @Nullable String readUnquotedAttributeValue() throws IOException, TextErrorException;
    @Nullable TextToken readUnquotedAttributeValueToken() throws IOException, TextErrorException;

    // comment
    @Nullable String readComment() throws IOException, PDMLDocumentSyntaxException;
    @Nullable TextToken readCommentToken() throws IOException, PDMLDocumentSyntaxException;
    boolean skipComment() throws IOException, PDMLDocumentSyntaxException;
    void skipWhitespaceAndComments() throws IOException, PDMLDocumentSyntaxException;
    boolean isAtStartOfComment() throws IOException;
    // extension
    // boolean handleExtension () throws TextReaderException;

    // config
    // @NotNull PXMLReaderConfig getConfig();


    // text reader functions

    // iteration
    boolean hasChar();
    char currentChar();
    void advanceChar() throws IOException;
    @Nullable String advanceCharWhile ( @NotNull CharPredicate predicate ) throws IOException;

    // location
    @NotNull CharReaderWithInserts currentCharReader();
    @Nullable TextResource currentResource();
    @NotNull TextLocation currentLocation();
    @NotNull TextToken currentToken();

    // isAt
    boolean isAtChar ( char c );
    boolean isAt ( @NotNull CharPredicate predicate );
    boolean isAtString ( @NotNull String s ) throws IOException;
    boolean isAtNewLine();
    boolean isAtSpaceOrTabOrNewLine();

    // append
    boolean appendCharWhile ( CharPredicate predicate, StringBuilder sb ) throws IOException;
    boolean appendOptionalNewLine ( StringBuilder sb ) throws IOException;

    // skip
    boolean skipChar ( char c ) throws IOException;
    boolean skipString ( @NotNull String string ) throws IOException;
    void skipNChars ( long n ) throws IOException;
    boolean skipOneNewline() throws IOException;
    boolean skipOneSpaceOrTab() throws IOException;
    boolean skipOneSpaceOrTabOrNewline() throws IOException;
    boolean skipOptionalSpacesAndTabs() throws IOException;
    boolean skipOptionalSpacesAndTabsAndNewLines() throws IOException;

    // read
    // @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException;
    @Nullable String readWhileAtChar ( char c ) throws IOException;
    @Nullable String readMaxNChars ( long n ) throws IOException;
    @Nullable String readOptionalSpacesAndTabs() throws IOException;
    @Nullable String readOptionalNewLine() throws IOException;
    @Nullable String readUntilEndOfLine ( boolean includeNewLine ) throws IOException;
    @Nullable String readRemaining() throws IOException;

    // read-ahead
    void setMark ( int readAheadLimit );
    void goBackToMark();
    boolean isNextChar ( char c ) throws IOException;
    @Nullable String  peekNextMaxNChars ( int n ) throws IOException;
    // Character nextChar() throws IOException;
    // @NotNull String peekCurrentString ( int length ) throws IOException;
    char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws IOException;

    // push
    // @Deprecated void insertFileToRead ( @NotNull File file, @NotNull TextToken errorToken ) throws IOException;
    void insertFileToRead ( @NotNull Path filePath ) throws IOException;
    void insertStringToRead ( @NotNull String string );

    // debugging
    @NotNull String stateToString();
    void stateToOSOut ( @Nullable String label );
}
