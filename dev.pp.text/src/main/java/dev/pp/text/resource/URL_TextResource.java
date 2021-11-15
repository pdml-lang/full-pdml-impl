package dev.pp.text.resource;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.URLUtilities;

import java.io.IOException;
import java.net.URL;

public class URL_TextResource implements TextResource {

    private final @NotNull URL URL;


    public URL_TextResource ( @NotNull URL URL ) {

        this.URL = URL;
    }


    public @Nullable Object getResource() { return URL; }

    public @NotNull String getName() { return URL.toString(); }

    public @NotNull String getTextLine ( long lineNumber ) throws IOException {

        return URLUtilities.getNthLineInURL ( URL, lineNumber );
    }

    @Override
    public String toString() { return "URL: " + URL; }
}
