package dev.pp.text.error.handler;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextWarning;
import dev.pp.text.token.TextToken;

import java.util.ArrayList;
import java.util.List;

public class Collect_TextErrorHandler implements TextErrorHandler {


    final @NotNull List<TextError> errors;
    final @NotNull List<TextWarning> warnings;


    public Collect_TextErrorHandler() {

        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

/*
    public void start() {}

    public void stop() {}
*/

    public void handleError ( @NotNull TextError error ) { errors.add ( error ); }

    public void handleError ( @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        handleError ( new TextError ( id, message, token ) );
    }

    public void handleWarning ( @NotNull TextWarning warning ) { warnings.add ( warning ); }

    public void handleWarning ( @Nullable String id, @NotNull String message, @Nullable TextToken token ) {

        warnings.add ( new TextWarning ( id, message, token ) );
    }

//    public void handleWarning ( @NotNull PXMLReaderWarning warning ) { warnings.add ( warning ); }


    // queries
/*
    public @Nullable List<PXMLReaderErrorOrWarning> errorsAndWarnings() {

        if ( hasErrors() || hasWarnings() ) {
            List<PXMLReaderErrorOrWarning> list = new ArrayList<>();
            list.addAll ( errors );
            list.addAll ( warnings );
            return Collections.unmodifiableList ( list );
        } else {
            return null;
        }
    }

    public @Nullable List<PXMLReaderError> errors() {

        if ( hasErrors() ) {
            return Collections.unmodifiableList ( errors );
        } else {
            return null;
        }
    }

    public @Nullable List<PXMLReaderWarning> warnings() {

        if ( hasWarnings() ) {
            return Collections.unmodifiableList ( warnings );
        } else {
            return null;
        }
    }


    public PXMLReaderErrorOrWarning firstErrorOrWarning() {

        if ( hasErrors() ) {
            return errors.get ( 0 );
        } else if ( hasWarnings() ) {
            return warnings.get ( 0 );
        } else {
            return null;
        }
    }
*/
    public TextError firstError() {

        if ( errors.isEmpty() ) {
            return null;
        } else {
            return errors.get ( 0 );
        }
    }
/*
    public PXMLReaderWarning firstWarning() {

        if ( hasWarnings() ) {
            return warnings.get ( 0 );
        } else {
            return null;
        }
    }



    public boolean hasErrorsOrWarnings() { return hasErrors() || hasWarnings(); }

    public boolean hasErrors() { return ! errors.isEmpty(); }

    public boolean hasWarnings() { return ! warnings.isEmpty(); }


    public int errorOrWarningCount() { return errorCount() + warningCount(); }

    public int errorCount() { return errors.size(); }

    public int warningCount() { return warnings.size(); }
*/
}
