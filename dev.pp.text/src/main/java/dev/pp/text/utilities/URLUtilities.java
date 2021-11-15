package dev.pp.text.utilities;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.utilities.text.TextLines;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class URLUtilities {

    public static Reader getUTF8URLReader ( @NotNull String URLString ) throws IOException {

        return getUTF8URLReader ( new URL ( URLString ) );
    }

    public static Reader getUTF8URLReader ( @NotNull URL url ) throws IOException {

        return new InputStreamReader ( url.openStream(), StandardCharsets.UTF_8 );
    }

    public static @NotNull String getNthLineInURL ( @NotNull URL URL, long n ) throws IOException {

        try ( BufferedReader br = new BufferedReader ( getUTF8URLReader ( URL ) ) ) {
            return TextLines.getNthLineInReader ( br, n );
        }
    }
}
