package dev.pp.texttable.data.impls;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.texttable.data.TableDataColumn;
import dev.pp.texttable.data.TableDataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class NestedList_TableDataProvider<CELL> implements TableDataProvider<List<CELL>, CELL> {


    private final @NotNull List<List<CELL>> rows;
    private final @NotNull List<TableDataColumn<List<CELL>, CELL>> columns;


    public NestedList_TableDataProvider (
        @NotNull List<List<CELL>> rows,
        @NotNull List<String> columnTitles ) {

        this.rows = rows;
        this.columns = createColumns ( rows, columnTitles );
    }

    public NestedList_TableDataProvider (
        @NotNull List<List<CELL>> rows,
        long columnCount ) {

        this.rows = rows;
        this.columns = createColumns ( rows, columnCount );
    }


    public @NotNull Iterator<List<CELL>> getRows () { return rows.iterator(); };

    public @NotNull List<TableDataColumn<List<CELL>, CELL>> getColumns () { return columns; }

    public @NotNull Long rowCount() { return (long) rows.size(); }


    private @NotNull List<TableDataColumn<List<CELL>, CELL>> createColumns (
        @NotNull List<List<CELL>> rows,
        @NotNull List<String> columnTitles ) {

        List<TableDataColumn<List<CELL>, CELL>> columns = new ArrayList<>();
        int index = 0;
        for ( String title : columnTitles ) {
            columns.add ( createColumn ( title, index ) );
            index ++;
        }

        return columns;
    }

    private @NotNull List<TableDataColumn<List<CELL>, CELL>> createColumns (
        @NotNull List<List<CELL>> rows,
        long columnCount ) {

        List<TableDataColumn<List<CELL>, CELL>> columns = new ArrayList<>();
        for ( int index = 0; index < columnCount; index++ ) {
            columns.add ( createColumn ( null, index ) );
        }

        return columns;
    }

    private @NotNull TableDataColumn<List<CELL>, CELL> createColumn ( final @Nullable String title, final int index ) {

        return new TableDataColumn<List<CELL>, CELL> () {

            @Nullable @Override public String getHeader () {
                return title;
            }

            @NotNull @Override public Function<List<CELL>, CELL> rowToCellConverter() {
                return (row) -> row.get ( index );
            }
        };
    }
}
