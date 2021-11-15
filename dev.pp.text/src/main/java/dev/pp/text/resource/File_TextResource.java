package dev.pp.text.resource;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.FileUtilities;

import java.io.File;
import java.io.IOException;

public class File_TextResource implements TextResource {

    private final @NotNull File file;


    public File_TextResource ( @NotNull File file ) {

        this.file = file;
    }


    public @NotNull File getResource() { return file; }

    public @NotNull String getName() { return FileUtilities.getAbsoluteOSPath ( file ); }

    public @NotNull String getTextLine ( long lineNumber ) throws IOException {

        return FileUtilities.getNthLineInFile ( file, lineNumber );
    }

    @Override
    public String toString() { return "File: " + FileUtilities.getAbsoluteOSPath ( file ); }
}
