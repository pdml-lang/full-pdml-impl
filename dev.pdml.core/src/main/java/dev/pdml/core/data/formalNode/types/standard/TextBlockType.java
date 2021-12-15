package dev.pdml.core.data.formalNode.types.standard;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.exception.MalformedPXMLDocumentException;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.character.CharChecks;
import dev.pp.text.utilities.string.StringBuilderUtils;
import dev.pp.text.utilities.text.TextIndent;
import dev.pp.text.utilities.text.TextLines;

import java.util.ArrayList;
import java.util.List;

public class TextBlockType extends StringType {

    // public static final char DELIMITER_CHAR_1 = '-'; // cannot be used because "-]" is end of comment
    public static final char DELIMITER_CHAR_1 = '=';
    public static final char DELIMITER_CHAR_2 = '"';
    public static final char DELIMITER_CHAR_3 = '~';
    public static final int MIN_DELIMITER_CHARs = 3;

    public static boolean isDelimiterChar ( char c ) {

        return c == DELIMITER_CHAR_1
            || c == DELIMITER_CHAR_2
            || c == DELIMITER_CHAR_3;
    }


    public @Nullable String readPDMLObject ( @NotNull PXMLReader reader, @NotNull ASTNodeName nodeName ) throws TextReaderException {

        requestOptionalSpacesAndTabs_AndNewLine ( reader );

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


    /* example:
        [code
            i = 1;
        code]
    */
    private @Nullable String readBlockWithEndTag (
        @NotNull ASTNodeName nodeName,
        @NotNull PXMLReader reader ) throws TextReaderException {

        // now positioned at first line, first column of text

        String endString = nodeName.fullName() + PDMLConstants.NODE_END;

        List<String> lines = new ArrayList<>();
        while ( true ) {

            if ( ! reader.hasChar() ) throw new MalformedPXMLDocumentException (
                "MISSING_END_OF_TEXT_BLOCK",
                "Missing '" + endString + "' on a separate subsequent line to end the text block.",
                nodeName.getToken() );

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

    /* example:
        [code
            ~~~
            i = 1;
            ~~~
        ]
    */
    private @Nullable String readBlockWithDelimiter (
        char delimiterChar,
        @NotNull PXMLReader reader ) throws TextReaderException {

        // now positioned at first line, first column of text (delimiter line)

        @Nullable String indent = reader.readOptionalSpacesAndTabs();
        @Nullable String delimiter = reader.readWhileAtChar ( delimiterChar );
        assert delimiter != null;

        requestOptionalSpacesAndTabs_AndNewLine ( reader );
        TextToken startToken = reader.currentToken();

        StringBuilder sb = new StringBuilder();
        while ( true ) {

            if ( ! reader.hasChar() ) throw new MalformedPXMLDocumentException (
                "MISSING_END_OF_TEXT_BLOCK",
                "Missing '" + delimiter + "' on a separate subsequent line to end the text block.",
                startToken );

            if ( indent != null ) {
                if ( reader.isAt ( CharChecks::isNewLine ) ) {
                    // ok, it's an empty line
                } else {
                    if ( ! reader.skipString ( indent ) ) {
                        throw new MalformedPXMLDocumentException (
                            "INDENT_EXPECTED",
                            "Expecting the same indent as the indent on the first line (" + indent.length () + " characters).",
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

    private @Nullable Character getDelimiterChar ( PXMLReader reader ) throws PXMLResourceException {

        char firstChar = reader.currentChar();
        if ( ! isDelimiterChar ( firstChar ) ) return null;

        @NotNull String minDelimiter = String.valueOf ( firstChar ).repeat ( MIN_DELIMITER_CHARs );
        // return reader.isAtString ( minDelimiter ) ? firstChar : null; // uses setMark
        String delimiters = reader.readMaxNChars ( MIN_DELIMITER_CHARs );
        assert delimiters != null;
        return delimiters.equals ( minDelimiter ) ? firstChar : null;
    }

    private void requestOptionalSpacesAndTabs_AndNewLine ( PXMLReader reader ) throws TextReaderException {

        reader.skipOptionalSpacesAndTabs();

        if ( ! reader.skipOneNewline() ) {
            throw new MalformedPXMLDocumentException (
                "INVALID_CHARACTER",
                "Expecting end of line, but invalid character '" + reader.currentChar() + "' encountered.",
                reader.currentToken() );
        }
    }
}
