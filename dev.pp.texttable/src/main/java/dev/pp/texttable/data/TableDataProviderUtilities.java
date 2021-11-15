package dev.pp.texttable.data;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.string.StringConstants;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class TableDataProviderUtilities {

    public static @NotNull <ROW, CELL> TableDataColumn<ROW, CELL> createTableDataColumn (
        final @Nullable String title,
        final Function<ROW, CELL> rowToCellConverter ) {

        return new TableDataColumn<ROW, CELL> () {

            @Nullable @Override public String getHeader () {
                return title;
            }

            @NotNull @Override public Function<ROW, CELL> rowToCellConverter () {
                return rowToCellConverter;
            }
        };
    }

    public static @NotNull <ROW, CELL> TableDataColumn<ROW, CELL> createTableDataColumn (
        final Function<ROW, CELL> rowToCellConverter ) {

        return createTableDataColumn ( null, rowToCellConverter );
    }

    public static <ROW, CELL> void writeData (
        TableDataProvider<ROW, CELL> provider,
        @NotNull String rowSeparator,
        @NotNull String cellSeparator,
        @NotNull Writer writer ) throws IOException {

        Iterator<ROW> rowsIterator = provider.getRows ();
        List<TableDataColumn<ROW, CELL>> columns = provider.getColumns ();
        boolean isFirstRow = true;
        while ( rowsIterator.hasNext() ) {
            ROW row = rowsIterator.next();
            if ( ! isFirstRow ) writer.write ( rowSeparator );

            boolean isFirstCell = true;
            for ( TableDataColumn<ROW, CELL> column : columns ) {
                CELL cell = column.getCellForRow ( row );
                if ( ! isFirstCell ) writer.write ( cellSeparator );
                writer.write ( cell.toString() );
                isFirstCell = false;
            }

            isFirstRow = false;
        }
    }

    public static @NotNull <ROW, CELL> String dataToString (
        TableDataProvider<ROW, CELL> provider,
        @NotNull String rowSeparator,
        @NotNull String cellSeparator ) {

        StringWriter writer = new StringWriter();
        try {
            writeData ( provider, rowSeparator, cellSeparator, writer );
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }

        return writer.toString();
    }

    public static @NotNull <ROW, CELL> String dataToString ( TableDataProvider<ROW, CELL> provider ) {

        return dataToString ( provider, StringConstants.OS_NEW_LINE, ", " );
    }
}
