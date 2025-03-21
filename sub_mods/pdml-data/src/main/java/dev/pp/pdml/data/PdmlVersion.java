package dev.pp.pdml.data;

import dev.pp.core.basics.annotations.NotNull;

public class PdmlVersion {

    public static @NotNull String APPLICATION_NAME = "PDML Companion";
    // public static @NotNull String APPLICATION_NAME_WITHOUT_SPACES = APPLICATION_NAME.replace ( ' ', '_' );
    public static @NotNull String APPLICATION_SHORT_NAME = "PDMLC";

    public static @NotNull String VERSION = "0.78.0";
    public static @NotNull String VERSION_DATE = "2025-03-21";
    public static @NotNull String VERSION_TEXT = APPLICATION_SHORT_NAME + " " + VERSION + " " + VERSION_DATE;

    /* currently not used
    public static @NotNull String getMajorVersion() {

        int index = VERSION.indexOf ( "." );
        return VERSION.substring ( 0, index );
    }

    public static @NotNull String getMinorVersion() {

        int index = VERSION.lastIndexOf ( "." );
        return VERSION.substring ( index + 1 );
    }

    public static int getMajorVersionNumber() {
        return Integer.parseInt ( getMajorVersion() );
    }

    public static int getMinorVersionNumber() {
        return Integer.parseInt ( getMinorVersion() );
    }
     */
}
