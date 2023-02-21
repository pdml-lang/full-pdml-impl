package dev.pdml.shared.constants;

import dev.pp.basics.annotations.NotNull;

import java.util.regex.Pattern;

public class CorePdmlConstants {

    // Node
    public static final char NODE_START = '[';
    public static final char NODE_END = ']';
    public static final char NAME_VALUE_SEPARATOR = ' ';

    public static final char ESCAPE_CHARACTER = '\\';

    public static final @NotNull String PDML_FILE_EXTENSION = "pdml";

    public static final @NotNull Pattern NAME_PATTERN = Pattern.compile ( "[a-zA-Z_][a-zA-Z0-9_\\.-]*" );
}
