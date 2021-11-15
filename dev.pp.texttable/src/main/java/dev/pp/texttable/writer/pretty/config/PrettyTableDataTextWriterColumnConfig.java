package dev.pp.texttable.writer.pretty.config;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.string.HTextAlign;
import dev.pp.texttable.data.TableDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrettyTableDataTextWriterColumnConfig<CELL> {

    public static final int DEFAULT_COLUMN_WIDTH = 12;
    public static final @NotNull HTextAlign DEFAULT_COLUMN_ALIGNMENT = HTextAlign.LEFT;
    public static final int DEFAULT_MAX_CELL_LINES = 5;
    // public static final @NotNull Function<Object, String> DEFAULT_CELL_VALUE_TO_STRING_CONVERTER = Object::toString;

    public static <CELL> List<PrettyTableDataTextWriterColumnConfig<CELL>> createDefaultColumnConfigs (
        int columnCount ) {

        return IntStream.rangeClosed ( 1, columnCount )
            .mapToObj ( i -> new PrettyTableDataTextWriterColumnConfig<CELL>() )
            .collect ( Collectors.toList() );
    }

    public static <CELL> List<PrettyTableDataTextWriterColumnConfig<CELL>> createDefaultColumnConfigs (
        TableDataProvider<?, CELL> data ) {

        return createDefaultColumnConfigs ( (int) data.getColumnCount () );
    }


    private final int columnWidth;
    private final @NotNull HTextAlign columnAlignment;
    private final @Nullable Integer maxCellLines;
    private final @NotNull Function<CELL, String> cellValueToStringConverter;


    public PrettyTableDataTextWriterColumnConfig (
        int columnWidth,
        @NotNull HTextAlign columnAlignment,
        @Nullable Integer maxCellLines,
        @NotNull Function<CELL, String> cellValueToStringConverter ) {

        this.columnWidth = columnWidth;
        this.columnAlignment = columnAlignment;
        this.maxCellLines = maxCellLines;
        this.cellValueToStringConverter = cellValueToStringConverter;
    }

    public PrettyTableDataTextWriterColumnConfig (
        int columnWidth,
        @NotNull HTextAlign columnAlignment,
        @Nullable Integer maxCellLines ) {

        this ( columnWidth,
            columnAlignment,
            maxCellLines,
            Object::toString );
    }

    public PrettyTableDataTextWriterColumnConfig (
        int columnWidth,
        @NotNull HTextAlign columnAlignment ) {

        this ( columnWidth,
            columnAlignment,
            DEFAULT_MAX_CELL_LINES,
            Object::toString );
    }

    public PrettyTableDataTextWriterColumnConfig (
        int columnWidth ) {

        this ( columnWidth,
            DEFAULT_COLUMN_ALIGNMENT,
            DEFAULT_MAX_CELL_LINES,
            Object::toString );
    }

    public PrettyTableDataTextWriterColumnConfig() {

        this ( DEFAULT_COLUMN_WIDTH,
            DEFAULT_COLUMN_ALIGNMENT,
            DEFAULT_MAX_CELL_LINES,
            Object::toString );
    }


    public int getColumnWidth() { return columnWidth; }

    public @NotNull HTextAlign getColumnAlignment() { return columnAlignment; }

    public @Nullable Integer getMaxCellLines() { return maxCellLines; }

    public @NotNull Function<CELL, String> getCellValueToStringConverter() { return cellValueToStringConverter; }


    public PrettyTableDataTextWriterColumnConfig<CELL> withWidth ( int width ) {

        return new PrettyTableDataTextWriterColumnConfig<> (
            width, columnAlignment, maxCellLines, cellValueToStringConverter );
    }

    public PrettyTableDataTextWriterColumnConfig<CELL> withAlignment ( HTextAlign alignment ) {

        return new PrettyTableDataTextWriterColumnConfig<> (
            columnWidth, alignment, maxCellLines, cellValueToStringConverter );
    }

    public PrettyTableDataTextWriterColumnConfig<CELL> withMaxCellLines ( @Nullable Integer maxCellLines ) {

        return new PrettyTableDataTextWriterColumnConfig<> (
            columnWidth, columnAlignment, maxCellLines, cellValueToStringConverter );
    }

    public PrettyTableDataTextWriterColumnConfig<CELL> withCellValueToStringConverter (
        Function<CELL, String> converter ) {

        return new PrettyTableDataTextWriterColumnConfig<> (
            columnWidth, columnAlignment, maxCellLines, converter );
    }

    /*
    null !
    public @Nullable String valueToString ( CELL value ) {

        return cellValueToStringConverter.apply ( value );
    }

     */
}
