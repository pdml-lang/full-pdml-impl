package dev.pdml.ext.scripting;

import dev.pp.text.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class WriterScriptingContext implements ScriptingContext {

    private final Writer writer;


    public WriterScriptingContext ( Writer writer ) {

        this.writer = writer;
    }


    public void write ( @NotNull String string ) throws IOException {

        writer.write ( string );
    }
}
