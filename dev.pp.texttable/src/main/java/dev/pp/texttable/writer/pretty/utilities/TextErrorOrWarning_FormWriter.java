package dev.pp.texttable.writer.pretty.utilities;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextErrorOrWarning;
import dev.pp.text.error.TextWarning;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.handler.Write_TextErrorHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.text.utilities.string.HTextAlign;
import dev.pp.text.utilities.string.StringConstants;
import dev.pp.text.utilities.text.TextMarker;
import dev.pp.texttable.data.impls.FormField;
import dev.pp.texttable.data.impls.FormFields;
import dev.pp.texttable.data.impls.FormFields_TableDataProvider;
import dev.pp.texttable.writer.pretty.PrettyTableDataTextWriterImpl;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterColumnConfig;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterConfig;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.function.Function;

public class TextErrorOrWarning_FormWriter {

    private static final int DEFAULT_LABEL_WIDTH = 10;
    private static final int DEFAULT_VALUE_WIDTH = 108;
    private static final int DEFAULT_WIDTH = DEFAULT_LABEL_WIDTH + 1 +DEFAULT_VALUE_WIDTH;

    public static TextErrorHandler createErrorHandler (
        @NotNull Writer writer,
        @Nullable String separator ) {

        return new Write_TextErrorHandler (
            writer,
            TextErrorOrWarning_FormWriter::toString,
            TextErrorOrWarning_FormWriter::toString,
            separator );
    }

    public static TextErrorHandler createErrorHandler() {

        return createErrorHandler (
            Write_TextErrorHandler.OS_ERR_WRITER,
            "-".repeat ( DEFAULT_WIDTH ) + StringConstants.OS_NEW_LINE );
    }

    public static @NotNull String toString ( @NotNull TextErrorOrWarning ew ) {

        StringWriter sw = new StringWriter();
        try {
            write ( ew, sw );
        } catch ( Exception e ) {
            throw new RuntimeException ( e );
        }
        return sw.toString();
    }

    public static void write ( @NotNull TextErrorOrWarning ew, @NotNull Writer writer ) throws Exception {

        FormFields<String> formFields = createFormFields ( ew );

        FormFields_TableDataProvider<String> data = new FormFields_TableDataProvider<String> ( formFields );

        List<PrettyTableDataTextWriterColumnConfig<String>> columnConfigs = List.of (
            new PrettyTableDataTextWriterColumnConfig<> ( DEFAULT_LABEL_WIDTH, HTextAlign.LEFT, null ),
            new PrettyTableDataTextWriterColumnConfig<> ( DEFAULT_VALUE_WIDTH, HTextAlign.LEFT, null ) );

        new PrettyTableDataTextWriterImpl<FormField<String>, String>().write (
            data,
            writer,
            new PrettyTableDataTextWriterConfig<> ( columnConfigs ) );
    }

    private static FormFields<String> createFormFields ( @NotNull TextErrorOrWarning ew ) throws Exception {

        FormFields<String> formFields = new FormFields<String>();

        String label = ew.getLabel();
        formFields.add ( label, ew.getMessage() );

        @Nullable TextLocation location = ew.getLocation();
        if ( location != null ) {

            @Nullable String code  = ew.getTextLine();
            if ( code != null ) {
                int markerLength = ew.getToken() == null ? 1 : ew.getToken().getText().length();
                code = TextMarker.breakSingleTextLineAndInsertMarkerLine (
                    code, (int) location.getColumnNumber() - 1, TextMarker.DEFAULT_MARKER_IN_MARKER_LINE,
                    markerLength, true, 105 );
                formFields.add ( "Code", code );
            }

            formFields.add ( "Location", location.toString ( false, false ) );

            String parentLocations = location.parentLocationsToString ( false );
            if ( parentLocations != null ) {
                formFields.add ( "Call stack", parentLocations );
            }
        }

        String id = ew.getId();
        if ( id != null ) {
            formFields.add ( label + " id", id );
        }

        return formFields;
    }
}
