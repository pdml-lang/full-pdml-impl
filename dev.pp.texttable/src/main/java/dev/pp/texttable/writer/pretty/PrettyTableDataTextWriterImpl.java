package dev.pp.texttable.writer.pretty;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.string.StringConstants;
import dev.pp.text.utilities.text.TextLines;
import dev.pp.texttable.data.TableDataProvider;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterColumnConfig;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterConfig;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrettyTableDataTextWriterImpl<ROW, CELL> implements PrettyTableDataTextWriter<ROW, CELL> {


    public void write (
        @NotNull TableDataProvider<ROW, CELL> data,
        @NotNull Writer writer,
        @NotNull PrettyTableDataTextWriterConfig<CELL> config ) throws IOException {

        assert data.getColumnCount () == config.getColumnConfigs().size();

        Iterator<ROW> rows = data.getRows ();
        while ( rows.hasNext() ) {
            ROW row = rows.next();

            writeRow ( row, data, config.getColumnConfigs(), writer );
        }
        writer.flush();
    }

    private void writeRow (
        ROW row,
        @NotNull TableDataProvider<ROW, CELL> data,
        @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> columnConfigs,
        @NotNull Writer writer ) throws IOException {

        @NotNull List<List<String>> allCellLines = getCellTextLinesForRow ( row, data, columnConfigs );
        int maxLinesCount = getMaxLinesCount ( allCellLines );
        int cellsCount = columnConfigs.size();
        for ( int lineIndex = 0; lineIndex < maxLinesCount; lineIndex++ ) {
            for ( int cellIndex = 0; cellIndex < cellsCount; cellIndex++ ) {
            // for ( List<String> cellLines : allCellLines ) {
                List<String> singleCellLines = allCellLines.get ( cellIndex );
                String singleCellLineText;
                if ( singleCellLines.size() > lineIndex ) {
                    singleCellLineText = singleCellLines.get ( lineIndex );
                } else {
                    PrettyTableDataTextWriterColumnConfig<CELL> columnConfig = columnConfigs.get ( cellIndex );
                    singleCellLineText = " ".repeat ( columnConfig.getColumnWidth() );
                }

                if ( cellIndex > 0 ) writer.write ( ' ' ); // cell separator
                writer.write ( singleCellLineText );
            }
            writer.write ( StringConstants.OS_NEW_LINE );
        }
    }

    private int getMaxLinesCount ( List<List<String>> cellTextLines ) {

        int maxLinesCount = 0;
        for ( List<String> cellLines : cellTextLines) {
            if ( maxLinesCount < cellLines.size() ) maxLinesCount = cellLines.size();
        }
        return maxLinesCount;
    }

    private @NotNull List<List<String>> getCellTextLinesForRow (
        ROW row,
        @NotNull TableDataProvider<ROW, CELL> data,
        @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> columnConfigs ) {

        // List<CELL> cellValues = data.getCellsForRow ( row );
        List<List<String>> rowTextLines = new ArrayList<>();
        int index = 0;
        for ( CELL cellValue : data.getCellsForRow ( row ) ) {
            PrettyTableDataTextWriterColumnConfig<CELL> columnConfig = columnConfigs.get ( index );
            rowTextLines.add ( getCellTextLinesForCell ( cellValue, columnConfig ) );
            index ++;
        }
        return rowTextLines;
    }

    private @NotNull List<String> getCellTextLinesForCell (
        @Nullable CELL cellValue,
        @NotNull PrettyTableDataTextWriterColumnConfig<CELL> columnConfig ) {

        return TextLines.valueToAlignedTextLines (
            cellValue,
            columnConfig.getCellValueToStringConverter(),
            columnConfig.getColumnWidth(),
            columnConfig.getColumnAlignment(),
            columnConfig.getMaxCellLines() );
    }
}
