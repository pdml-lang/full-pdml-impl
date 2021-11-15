package dev.pp.text.utilities.string;

import dev.pp.text.annotations.NotNull;

public class StringConstants {

    public static final @NotNull String OS_NEW_LINE = System.getProperty ( "line.separator" );
    public static final @NotNull String UNIX_NEW_LINE = String.valueOf ( '\n' );
    public static final @NotNull String WINDOWS_NEW_LINE = "\r\n";

    public static final @NotNull String NULL_AS_STRING = "null";
}
