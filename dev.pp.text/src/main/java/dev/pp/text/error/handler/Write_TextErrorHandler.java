package dev.pp.text.error.handler;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextWarning;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.string.StringConstants;

import java.io.*;
import java.util.function.Function;

public class Write_TextErrorHandler implements TextErrorHandler {


    public static final @NotNull Writer OS_ERR_WRITER = new PrintWriter ( System.err );


    private final @NotNull Writer writer;
    private final @NotNull Function<TextError, String> errorToStringConverter;
    private final @NotNull Function<TextWarning, String> warningToStringConverter;
    private final @Nullable String separator;

    private @Nullable TextError firstError;


    public Write_TextErrorHandler (
        @NotNull Writer writer,
        @NotNull Function<TextError, String> errorToStringConverter,
        @NotNull Function<TextWarning, String> warningToStringConverter,
        @Nullable String separator ) {

        this.writer = writer;
        this.errorToStringConverter = errorToStringConverter;
        this.warningToStringConverter = warningToStringConverter;
        this.separator = separator;

        this.firstError = null;
    }

    public Write_TextErrorHandler ( @NotNull Writer writer ) {

        this ( writer, TextError::toString, TextWarning::toString, StringConstants.OS_NEW_LINE );
    }

    public Write_TextErrorHandler ( @NotNull File file ) throws IOException {

        this ( FileUtilities.getUTF8FileWriter ( file ) );
    }

    public Write_TextErrorHandler () {

        this ( OS_ERR_WRITER );
    }


    public void handleError ( @NotNull TextError error ) {

        if ( firstError == null ) firstError = error;

        // errorCount ++;

        write ( errorToStringConverter.apply ( error ) );
        writeSeparator();
        flush();
    }

    public void handleError ( @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleError ( new TextError ( id, message, token ) );
    }

    public void handleWarning ( @NotNull TextWarning warning ) {

        write ( warningToStringConverter.apply ( warning ) );
        writeSeparator();
        flush();
    }

    public void handleWarning ( @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleWarning ( new TextWarning ( id, message, token ) );
    }

    public TextError firstError() { return firstError; }


    private void writeSeparator() {

        if ( separator != null ) write ( separator );
    }

    private void write ( String string ) {

        try {
            writer.write ( string );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }

    private  void flush() {

        try {
            writer.flush();
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }


/*
    private void writeln ( String string ) {

        try {
            writer.write ( string );
            writer.write ( StringConstants.OS_NEW_LINE );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }

    private void writeln() {

        try {
            writer.write ( StringConstants.OS_NEW_LINE );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }
*/
/*
    private int errorCount = 0;
    private int warningCount = 0;

    private PXMLReaderError firstError = null;
    private PXMLReaderWarning firstWarning = null;

    public void start() {

        errorCount = 0;
        warningCount = 0;

        firstError = null;
        firstWarning = null;
    }
*/

//    public void stop() {

        /*
        if ( errorCount > 0 || warningCount > 0 ) {
            println();
            printCount ( errorCount, "non-canceling error" );
            printCount ( warningCount, "warning" );
        }

        if ( errorCount > 1 ) {
            println();
            println ( "First error:" );
            println ( firstError.toString() );

        } else if ( warningCount > 1 ) {
            println();
            println ( "First warning:" );
            println ( firstWarning.toString() );

        } else {
            println ( "Ok" );
        }
        */
//    }

/*
    private void printCount ( int count, String label ) {

        if ( count == 0 ) return;

        if ( count > 1 ) label = label + "s";
        println ( count + " " + label );
    }

    public void handleError ( @NotNull String id, @NotNull String message, @Nullable TextLocation location ) {

        handleError ( new PXMLReaderError ( id, message, location ) );
    }

    public void handleWarning ( @NotNull String id, @NotNull String message, @Nullable TextLocation location ) {

        handleWarning ( new PXMLReaderWarning ( id, message, location ) );
    }

    public void handleError ( @NotNull PXMLReaderError error ) {

        if ( firstError == null ) firstError = error;

        errorCount ++;

        print ( "Error: " );
        println ( error.toString() );
        println();
    }

    public void handleWarning ( @NotNull PXMLReaderWarning warning ) {

        if ( firstWarning == null ) firstWarning = warning;

        warningCount ++;

        print ( "Warning: " );
        println ( warning.toString() );
        println();
    }
*/
}
