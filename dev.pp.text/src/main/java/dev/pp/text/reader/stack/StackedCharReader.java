package dev.pp.text.reader.stack;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.reader.CharReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

// TODO? rename to InsertableCharReader, and push -> insert

public interface StackedCharReader extends CharReader {

    void push ( @NotNull CharReader iterator );

    void push ( @NotNull File file ) throws IOException;

    void push ( @NotNull URL url ) throws IOException;

    void push ( @NotNull String string ) throws IOException;
}
