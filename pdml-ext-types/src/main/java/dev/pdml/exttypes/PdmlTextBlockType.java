package dev.pdml.exttypes;

import dev.pdml.data.node.NodeName;
import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.exception.PdmlDocumentSyntaxException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.character.CharChecks;
import dev.pp.basics.utilities.string.StringBuilderUtils;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PdmlTextBlockType extends PdmlStringOrNullType {

    // public static final char DELIMITER_CHAR_1 = '-'; // cannot be used because "-]" is end of comment
    public static final char DELIMITER_CHAR_1 = '=';
    public static final char DELIMITER_CHAR_2 = '"';
    public static final char DELIMITER_CHAR_3 = '~';
    public static final int MIN_DELIMITER_CHARS = 3;
    public static final @NotNull String DELIMITER_1 = String.valueOf ( DELIMITER_CHAR_1 ).repeat ( MIN_DELIMITER_CHARS );
    public static final @NotNull String DELIMITER_2 = String.valueOf ( DELIMITER_CHAR_2 ).repeat ( MIN_DELIMITER_CHARS );
    public static final @NotNull String DELIMITER_3 = String.valueOf ( DELIMITER_CHAR_3 ).repeat ( MIN_DELIMITER_CHARS );
    private static final @NotNull String DELIMITERS =
        DELIMITER_1 + ", " + DELIMITER_2 + ", " + DELIMITER_3;

    public static boolean isDelimiterChar ( char c ) {

        return c == DELIMITER_CHAR_1
            || c == DELIMITER_CHAR_2
            || c == DELIMITER_CHAR_3;
    }


    public PdmlTextBlockType ( boolean escapeText ) {
        super ( escapeText );
    }

    public PdmlTextBlockType() {
        this ( true );
    }


    public @Nullable String readPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        reader.skipOptionalSpacesAndTabs();
        if ( ! reader.isAtNewLine() ) {
            // [code i = 1;]
            return reader.readText ();
        } else {
            return readBlockWithDelimiter ( reader );
        }
    }

    /* example:
        [code
            ~~~
            i = 1;
            ~~~
        ]
    */
    private @Nullable String readBlockWithDelimiter (
        @NotNull PdmlReader reader ) throws IOException, PdmlDocumentSyntaxException {

        // now positioned at the end of the tag line, just before the line break

        requestNewLine ( reader );

        @Nullable String indent = reader.readOptionalSpacesAndTabs();

        @Nullable Character delimiterChar = reader.currentChar();
        if ( ! isDelimiterChar ( delimiterChar ) ) {
            throw new PdmlDocumentSyntaxException (
                "Expecting one of the following delimiters to start a text block: " + DELIMITERS,
                "INVALID_DELIMITER",
                reader.currentToken() );
        }

        @Nullable String delimiter = reader.readWhileAtChar ( delimiterChar );
        assert delimiter != null;
        if ( delimiter.length() < MIN_DELIMITER_CHARS ) {
            throw new PdmlDocumentSyntaxException (
                "Expecting at least " + MIN_DELIMITER_CHARS + " delimiter characters.",
                "INVALID_DELIMITER",
                reader.currentToken() );
        }

        reader.skipOptionalSpacesAndTabs();
        requestNewLine ( reader );

        TextToken startToken = reader.currentToken();

        StringBuilder sb = new StringBuilder();
        while ( true ) {

            if ( ! reader.hasChar() ) throw new PdmlDocumentSyntaxException (
                "Missing '" + delimiter + "' on a separate subsequent line to end the text block.",
                "MISSING_END_OF_TEXT_BLOCK",
                startToken );

            if ( indent != null ) {
                if ( reader.isAt ( CharChecks::isNewLine ) ) {
                    // ok, it's an empty line
                } else {
                    if ( ! reader.skipString ( indent ) ) {
                        throw new PdmlDocumentSyntaxException (
                            "Expecting the same indent as the indent on the first line (" + indent.length () + " characters).",
                            "INDENT_EXPECTED",
                            reader.currentToken () );
                    }
                }
            }

            if ( reader.skipString ( delimiter ) ) break;

            StringBuilderUtils.appendIfNotNull ( sb, reader.readUntilEndOfLine ( true ) );
        }

        // end delimiter can be longer than start delimiter
        reader.readWhileAtChar ( delimiterChar );

        reader.skipOptionalSpacesAndTabsAndNewLines();

        StringBuilderUtils.removeOptionalNewLineAtEnd ( sb );

        return sb.length() == 0 ? null : sb.toString();
    }

    private void requestNewLine ( PdmlReader reader ) throws IOException, PdmlDocumentSyntaxException {

        if ( ! reader.skipOneNewline() ) {
            throw new PdmlDocumentSyntaxException (
                "Expecting end of line, but invalid character '" + reader.currentChar() + "' encountered.",
                "INVALID_CHARACTER",
                reader.currentToken() );
        }
    }


/* Note
    readBlockWithEndTag (the [code ... code] syntax variant) has been disabled.
    The old code is still kept here to re-enable it again if there is a good reason to do that.
 */

/*
    public @Nullable String readPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // requestOptionalSpacesAndTabs_AndNewLine ( reader );
        reader.skipOptionalSpacesAndTabs();
        if ( ! reader.isAtNewLine() ) {
            // [code i = 1;]
            return reader.readText();
        }
        requestNewLine ( reader );

        reader.setMark ( 500 );
        reader.readOptionalSpacesAndTabs();
        @Nullable Character delimiterChar = getDelimiterChar ( reader );
        reader.goBackToMark();

        if ( delimiterChar == null ) {
            return readBlockWithEndTag ( nodeName, reader );
        } else {
            return readBlockWithDelimiter ( delimiterChar, reader );
        }
    }
 */


    /* example:
        [code
            i = 1;
        code]
    */
/*
    private @Nullable String readBlockWithEndTag (
        @NotNull NodeName nodeName,
        @NotNull PdmlReader reader ) throws IOException, PdmlDocumentSyntaxException {

        // now positioned at first line, first column of text

        String endString = nodeName.qualifiedName () + CorePdmlConstants.NODE_END;

        List<String> lines = new ArrayList<>();
        while ( true ) {

            if ( ! reader.hasChar() ) throw new PdmlDocumentSyntaxException (
                "Missing '" + endString + "' on a separate subsequent line to end the text block.",
                "MISSING_END_OF_TEXT_BLOCK",
                nodeName.localNameToken() );

            @Nullable String leadingSpacesAndTabs = reader.readOptionalSpacesAndTabs();

            if ( reader.isAtString ( endString ) ) {
                reader.skipNChars ( endString.length() - 1 ); // do not consume the final ']'
                break;
            }

            @Nullable String remainingLine = reader.readUntilEndOfLine ( false );
            reader.skipOneNewline();

            StringBuilder line = new StringBuilder();
            StringBuilderUtils.appendIfNotNull ( line, leadingSpacesAndTabs );
            StringBuilderUtils.appendIfNotNull ( line, remainingLine );
            lines.add ( line.toString() );
        }

        if ( lines.isEmpty() ) return null;

        List<String> trimmedLines = TextIndent.removeSmallestIndent ( lines, true );
        return TextLines.linesToText ( trimmedLines );
    }
 */

    /* example:
        [code
            ~~~
            i = 1;
            ~~~
        ]
    */
/*
    private @Nullable String readBlockWithDelimiter (
        char delimiterChar,
        @NotNull PdmlReader reader ) throws IOException, PdmlDocumentSyntaxException {

        // now positioned at first line, first column of text (delimiter line)

        @Nullable String indent = reader.readOptionalSpacesAndTabs();
        @Nullable String delimiter = reader.readWhileAtChar ( delimiterChar );
        assert delimiter != null;

        requestOptionalSpacesAndTabs_AndNewLine ( reader );
        TextToken startToken = reader.currentToken();

        StringBuilder sb = new StringBuilder();
        while ( true ) {

            if ( ! reader.hasChar() ) throw new PdmlDocumentSyntaxException (
                "Missing '" + delimiter + "' on a separate subsequent line to end the text block.",
                "MISSING_END_OF_TEXT_BLOCK",
                startToken );

            if ( indent != null ) {
                if ( reader.isAt ( CharChecks::isNewLine ) ) {
                    // ok, it's an empty line
                } else {
                    if ( ! reader.skipString ( indent ) ) {
                        throw new PdmlDocumentSyntaxException (
                            "Expecting the same indent as the indent on the first line (" + indent.length () + " characters).",
                            "INDENT_EXPECTED",
                            reader.currentToken () );
                    }
                }
            }

            if ( reader.skipString ( delimiter ) ) break;

            StringBuilderUtils.appendIfNotNull ( sb, reader.readUntilEndOfLine ( true ) );
        }

        // end delimiter can be longer than start delimiter
        reader.readWhileAtChar ( delimiterChar );

        reader.skipOptionalSpacesAndTabsAndNewLines();

        StringBuilderUtils.removeOptionalNewLineAtEnd ( sb );

        return sb.length() == 0 ? null : sb.toString();
    }

    private @Nullable Character getDelimiterChar ( PdmlReader reader ) throws IOException {

        char firstChar = reader.currentChar();
        if ( ! isDelimiterChar ( firstChar ) ) return null;

        @NotNull String minDelimiter = String.valueOf ( firstChar ).repeat ( MIN_DELIMITER_CHARs );
        // return reader.isAtString ( minDelimiter ) ? firstChar : null; // uses setMark
        String delimiters = reader.readMaxNChars ( MIN_DELIMITER_CHARs );
        assert delimiters != null;
        return delimiters.equals ( minDelimiter ) ? firstChar : null;
    }

    private void requestOptionalSpacesAndTabs_AndNewLine ( PdmlReader reader )
        throws IOException, PdmlDocumentSyntaxException {

        reader.skipOptionalSpacesAndTabs();
        requestNewLine ( reader );
    }
 */
}
