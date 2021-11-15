package dev.pp.text.utilities;

import dev.pp.text.annotations.Nullable;

public class DebugUtils {

    public static void printNameValue ( @Nullable String name, @Nullable Object value ) {

        System.out.print ( name + ": " );
        System.out.println ( "<" + value + ">" );
    }

    public static void printNewLine() {

        System.out.println();
    }
}
