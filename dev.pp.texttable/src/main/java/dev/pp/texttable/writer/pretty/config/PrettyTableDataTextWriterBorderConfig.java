package dev.pp.texttable.writer.pretty.config;

import dev.pp.text.annotations.NotNull;

public class PrettyTableDataTextWriterBorderConfig {


    public static final boolean DEFAULT_WRITE_TABLE_BORDER = true;
    public static final boolean DEFAULT_WRITE_ROW_SEPARATORS = true;
    public static final boolean DEFAULT_WRITE_COLUMN_SEPARATORS = true;

    public static final @NotNull PrettyTableDataTextWriterBorderConfig DEFAULT =
        new PrettyTableDataTextWriterBorderConfig (
            DEFAULT_WRITE_TABLE_BORDER, DEFAULT_WRITE_ROW_SEPARATORS, DEFAULT_WRITE_COLUMN_SEPARATORS );


    private final boolean writeTableBorder;
    private final boolean writeRowSeparators;
    private final boolean writeColumnSeparators;


    public PrettyTableDataTextWriterBorderConfig (
        boolean writeTableBorder,
        boolean writeRowSeparators,
        boolean writeColumnSeparators ) {

        this.writeTableBorder = writeTableBorder;
        this.writeRowSeparators = writeRowSeparators;
        this.writeColumnSeparators = writeColumnSeparators;
    }


    public boolean isWriteTableBorder () { return writeTableBorder; }

    public boolean isWriteRowSeparators () { return writeRowSeparators; }

    public boolean isWriteColumnSeparators () { return writeColumnSeparators; }


    public PrettyTableDataTextWriterBorderConfig withTableBorder ( boolean writeTableBorder ) {

        return new PrettyTableDataTextWriterBorderConfig (
            writeTableBorder, writeRowSeparators, writeColumnSeparators );
    }

    public PrettyTableDataTextWriterBorderConfig withRowSeparators ( boolean writeRowSeparators ) {

        return new PrettyTableDataTextWriterBorderConfig (
            writeTableBorder, writeRowSeparators, writeColumnSeparators );
    }

    public PrettyTableDataTextWriterBorderConfig withColumnSeparators( boolean writeColumnSeparators ) {

        return new PrettyTableDataTextWriterBorderConfig (
            writeTableBorder, writeRowSeparators, writeColumnSeparators );
    }
}
