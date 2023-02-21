package dev.pdml.reader;

import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.utilities.character.CharPredicate;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.nio.file.Path;

public interface PdmlReader {

    // Node
    boolean consumeNodeStart() throws IOException, TextErrorException;
    boolean isAtNodeStart() throws IOException, TextErrorException;
    boolean consumeNodeEnd() throws IOException;
    boolean isAtNodeEnd();

    // Name
    @Nullable String readName() throws IOException;
    @Nullable TextToken readNameToken() throws IOException;

    // Text
    @Nullable String readText() throws IOException, TextErrorException;
    @Nullable TextToken readTextToken() throws IOException, TextErrorException;
    // TODO? PXMLToken readRawTextToken() throws IOException, PDMLDocumentException; // without unescaping -> faster

    // Attributes
    @Nullable String readAttributeValue() throws IOException, TextErrorException;
    @Nullable TextToken readAttributeValueToken() throws IOException, TextErrorException;
    // TODO throws PDMLDocumentSyntaxException
    @Nullable String readQuotedAttributeValue() throws IOException, TextErrorException;
    @Nullable TextToken readQuotedAttributeValueToken() throws IOException, TextErrorException;
    @Nullable String readUnquotedAttributeValue() throws IOException, TextErrorException;
    @Nullable TextToken readUnquotedAttributeValueToken() throws IOException, TextErrorException;

    // Comment
    @Nullable String readComment() throws IOException, PdmlDocumentSyntaxException;
    @Nullable TextToken readCommentToken() throws IOException, PdmlDocumentSyntaxException;
    boolean skipComment() throws IOException, PdmlDocumentSyntaxException;
    void skipWhitespaceAndComments() throws IOException, PdmlDocumentSyntaxException;
    boolean isAtStartOfComment() throws IOException;


    // Iteration
    boolean hasChar();
    char currentChar();
    void advanceChar() throws IOException;
    @Nullable String advanceCharWhile ( @NotNull CharPredicate predicate ) throws IOException;

    // Location
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

    // Append
    boolean appendCharWhile ( CharPredicate predicate, StringBuilder sb ) throws IOException;
    boolean appendOptionalNewLine ( StringBuilder sb ) throws IOException;

    // Skip
    boolean skipChar ( char c ) throws IOException;
    boolean skipString ( @NotNull String string ) throws IOException;
    void skipNChars ( long n ) throws IOException;
    boolean skipOneNewline() throws IOException;
    boolean skipOneSpaceOrTab() throws IOException;
    boolean skipOneSpaceOrTabOrNewline() throws IOException;
    boolean skipOptionalSpacesAndTabs() throws IOException;
    boolean skipOptionalSpacesAndTabsAndNewLines() throws IOException;

    // Read
    // @Nullable String readWhile ( @NotNull CharPredicate predicate ) throws IOException;
    @Nullable String readWhileAtChar ( char c ) throws IOException;
    @Nullable String readMaxNChars ( long n ) throws IOException;
    @Nullable String readOptionalSpacesAndTabs() throws IOException;
    @Nullable String readOptionalSpacesAndTabsAndNewLines() throws IOException;
    @Nullable String readOptionalNewLine() throws IOException;
    @Nullable String readUntilEndOfLine ( boolean includeNewLine ) throws IOException;
    @Nullable String readRemaining() throws IOException;

    // Read-ahead
    void setMark ( int readAheadLimit );
    void goBackToMark();
    boolean isNextChar ( char c ) throws IOException;
    @Nullable String  peekNextMaxNChars ( int n ) throws IOException;
    // Character nextChar() throws IOException;
    // @NotNull String peekCurrentString ( int length ) throws IOException;
    char peekFirstCharAfterOptionalSpacesTabsAndNewLines ( int lookAhead ) throws IOException;

    // Insert
    // @Deprecated void insertFileToRead ( @NotNull File file, @NotNull TextToken errorToken ) throws IOException;
    void insertFileToRead ( @NotNull Path filePath ) throws IOException;
    void insertStringToRead ( @NotNull String string );

    // Debugging
    @NotNull String stateToString();
    void stateToOSOut ( @Nullable String label );
}
