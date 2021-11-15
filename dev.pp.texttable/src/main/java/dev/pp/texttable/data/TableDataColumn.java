package dev.pp.texttable.data;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.util.function.Function;

public interface TableDataColumn<ROW, CELL> {

    @NotNull Function<ROW, CELL> rowToCellConverter();

    default @Nullable String getHeader() { return null; }

    default @Nullable String getFooter() { return null; }

    default boolean hasHeader() {

        @Nullable String header = getHeader();
        return header != null && ! header.isEmpty();
    }

    default boolean hasFooter() {

        @Nullable String footer = getFooter();
        return footer != null && ! footer.isEmpty();
    }

    default CELL getCellForRow ( ROW row ) { return rowToCellConverter().apply ( row ); }

    // default @NotNull Function<ROW, Object> rowToCellDetailConverter() { return null; }
}
