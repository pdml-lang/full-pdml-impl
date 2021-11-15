package dev.pp.texttable.data.impls;

import dev.pp.text.annotations.NotNull;
import dev.pp.texttable.data.TableDataColumn;
import dev.pp.texttable.data.TableDataProvider;

import java.util.Iterator;
import java.util.List;

public class List_TableDataProvider<ROW, CELL> implements TableDataProvider<ROW, CELL> {


    private final @NotNull List<ROW> rows;
    private final @NotNull List<TableDataColumn<ROW, CELL>> columns;


    public List_TableDataProvider (
        @NotNull List<ROW> rows,
        @NotNull List<TableDataColumn<ROW, CELL>> columns ) {

        this.rows = rows;
        this.columns = columns;
    }


    public @NotNull Iterator<ROW> getRows () { return rows.iterator(); };

    public @NotNull List<TableDataColumn<ROW, CELL>> getColumns () { return columns; }

    public @NotNull Long rowCount() { return (long) rows.size(); }
}
