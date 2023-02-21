package dev.pdml.extscripting.bindings;

import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.utilities.PdmlEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.os.process.OSCommand;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.scripting.bindings.ScriptingBinding;

import java.io.IOException;

/**
 * Functions to work with the currently parsed PDML document.
 */
public class DocBinding implements ScriptingBinding {


    private final @NotNull PdmlReader reader;


    public DocBinding ( @NotNull PdmlReader reader ) {
        this.reader = reader;
    }


    public @NotNull String bindingName () { return "doc"; }

    /**
     * Insert a string into the document, at the current position.
     * @param string The string to be inserted. The string can be PDML code or text that is escaped already.
     * @throws IOException Thrown if an IO error occurs.
     */
    // @HostAccess.Export
    public void insert ( String string ) throws IOException {
        reader.insertStringToRead ( string );
    }

    /**
     * Insert text into the document, at the current position.
     *
     * Note: Use function 'insert' to insert PDML code, or text that is escaped already.
     *
     * @param text The text to be inserted.
     *
     * The text is escaped automatically by this function.
     * Hence, the text should not be escaped already, to avoid double-escaping.
     * @throws IOException Thrown if an IO error occurs.
     */
    public void insertText ( String text ) throws IOException {
        insert ( escapeText ( text ) );
    }

    /**
     * Insert a string, followed by an OS-dependant new line, into the document, at the current position.
     * @param string The string to be inserted. The string can be PDML code or text that is escaped already.
     * @throws IOException Thrown if an IO error occurs.
     */
    public void insertLine ( String string ) throws IOException {
        reader.insertStringToRead ( string + StringConstants.OS_NEW_LINE );
    }

    /**
     * Insert text, followed by an OS-dependant new line, into the document, at the current position.
     *
     * Note: Use function 'insertLine' to insert PDML code, or text that is escaped already.
     *
     * @param text The text to be inserted.
     *
     * The text is escaped automatically by this function.
     * Hence, the text should not be escaped already, to avoid double-escaping.
     * @throws IOException Thrown if an IO error occurs.
     */
    public void insertTextLine ( String text ) throws IOException {
        insertLine ( escapeText ( text ) );
    }

    /**
     * Insert an OS-dependant new line (LF on Linux; CRLF on Windows) into the document, at the current position.
     * @throws IOException Thrown if an IO error occurs.
     */
    public void insertNewLine () throws IOException {
        reader.insertStringToRead ( StringConstants.OS_NEW_LINE );
    }

    /**
     * Escape text.
     * @param text The text to be escaped.
     * @return Escaped text.
     */
    public @NotNull String escapeText ( @NotNull String text ) {
        return PdmlEscaper.escapeNodeText ( text );
    }

    /**
     * Execute an OS command, and insert the command's output (written to stdout) into the document, at the current position.
     *
     * @param OSCommandTokens An array of command tokens.
     * The first token is the program name.
     * Remaining tokens are command line arguments.
     * Example: "program_name" "--arg1" "value1" "--arg2" "value2"
     * @param input The input string that will be fed into the command's standard input device (stdin).
     * Use 'null' if no input is needed by the command.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InterruptedException Thrown if the command is interrupted.
     */
    public void insertOSCommandOutput(
        @NotNull String[] OSCommandTokens,
        @Nullable String input ) throws IOException, InterruptedException {

        insert ( OSCommand.textPipe ( OSCommandTokens, input ) );
    }
}
