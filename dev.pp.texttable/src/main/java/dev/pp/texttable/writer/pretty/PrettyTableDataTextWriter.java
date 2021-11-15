package dev.pp.texttable.writer.pretty;

import dev.pp.text.annotations.NotNull;
import dev.pp.texttable.data.TableDataProvider;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterConfig;

import java.io.IOException;
import java.io.Writer;

public interface PrettyTableDataTextWriter<ROW, CELL> {

    void write (
        @NotNull TableDataProvider<ROW, CELL> data,
        @NotNull Writer writer,
        @NotNull PrettyTableDataTextWriterConfig<CELL> config ) throws IOException;
}
