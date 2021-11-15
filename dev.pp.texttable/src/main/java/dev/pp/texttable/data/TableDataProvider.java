package dev.pp.texttable.data;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public interface TableDataProvider<ROW, CELL> {

    @NotNull Iterator<ROW> getRows();

    @NotNull List<TableDataColumn<ROW, CELL>> getColumns();

    default @Nullable Long getRowCount() { return null; }

    default long getColumnCount () { return getColumns().size(); };

    default boolean hasColumnTitles() {

        for ( TableDataColumn<ROW, CELL> column : getColumns () ) {
            if ( column.hasHeader () ) return true;
        }
        return false;
    }

    default List<CELL> getCellsForRow ( ROW row ) {

        return getColumns()
            .stream()
            .map ( ( column) -> column.getCellForRow ( row ) )
            .collect ( Collectors.toList() );
    }
}
