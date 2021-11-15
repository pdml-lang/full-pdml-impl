package dev.pp.texttable.writer.pretty.config;

import dev.pp.text.annotations.NotNull;
import dev.pp.texttable.data.TableDataProvider;

import java.util.List;

public class PrettyTableDataTextWriterConfig<CELL> {


    private final @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> columnConfigs;
    private final @NotNull PrettyTableDataTextWriterBorderConfig borderConfig;


    public PrettyTableDataTextWriterConfig (
        @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> columnConfigs,
        @NotNull PrettyTableDataTextWriterBorderConfig borderConfig ) {

        this.columnConfigs = columnConfigs;
        this.borderConfig = borderConfig;
    }

    public PrettyTableDataTextWriterConfig (
        @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> columnConfigs ) {

        this ( columnConfigs, PrettyTableDataTextWriterBorderConfig.DEFAULT );
    }

    public PrettyTableDataTextWriterConfig ( int columnCount ) {

        this ( PrettyTableDataTextWriterColumnConfig.createDefaultColumnConfigs ( columnCount ) );
    }

    public PrettyTableDataTextWriterConfig ( TableDataProvider<?, CELL> data ) {

        this ( PrettyTableDataTextWriterColumnConfig.createDefaultColumnConfigs ( data ) );
    }


    public @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> getColumnConfigs() { return columnConfigs; }

    public @NotNull PrettyTableDataTextWriterBorderConfig getBorderConfig() { return borderConfig; }


    public PrettyTableDataTextWriterConfig<CELL> withColumnConfigs (
        @NotNull List<PrettyTableDataTextWriterColumnConfig<CELL>> columnConfigs ) {

        return new PrettyTableDataTextWriterConfig<> (
            columnConfigs, borderConfig );
    }

    public PrettyTableDataTextWriterConfig<CELL> withBorderConfig (
        PrettyTableDataTextWriterBorderConfig borderConfig ) {

        return new PrettyTableDataTextWriterConfig<> (
            columnConfigs, borderConfig );
    }
}
