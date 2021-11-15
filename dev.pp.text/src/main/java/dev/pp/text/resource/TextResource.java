package dev.pp.text.resource;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.io.File;

public interface TextResource {

    @Nullable Object getResource();

    default @Nullable File getResourceAsFile() {

        @Nullable Object resource = getResource();
        if ( resource instanceof File ) {
            return (File) resource;
        } else {
            return null;
        }
    }

    @NotNull String getName();

    @NotNull String getTextLine ( long lineNumber ) throws Exception;

    /*
    default @Nullable String getTextLineOrNull ( long lineNumber ) {

        try {
            return getTextLine ( lineNumber );
        } catch ( Exception e ) {
            return null;
        }
    }

    default @NotNull String getTextLineWithMarker ( long lineNumber, int markerPosition, @NotNull String marker )
        throws Exception {

        String line = getTextLine ( lineNumber );
        return StringUtilities.insertMarker ( line, markerPosition, marker );
    }
    */
}
