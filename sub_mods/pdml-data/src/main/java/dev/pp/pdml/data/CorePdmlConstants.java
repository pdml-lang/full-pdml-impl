package dev.pp.pdml.data;

import dev.pp.core.basics.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CorePdmlConstants {

    private static final char EXTENSION_START_CHAR = '^';

    private static final char ATTRIBUTES_START_CHAR = '(';
    private static final char ATTRIBUTES_END_CHAR = ')';
    private static final char ATTRIBUTE_ASSIGN_CHAR = '=';

    private static final char QUOTED_STRING_DELIMITER_CHAR = '"';
    private static final char RAW_STRING_START_CHAR = '~';

    private static final char NAMESPACE_SEPARATOR_CHAR = '|';


    public static final char NODE_START_CHAR = '[';
    public static final char NODE_END_CHAR = ']';

    public static final char ESCAPE_CHAR = '\\';


    protected static final @NotNull Map<Character, Character> SHARED_ESCAPE_CHARS = createSharedEscapeMap();

    private static @NotNull Map<Character, Character> createSharedEscapeMap() {

        Map<Character, Character> map = new HashMap<> ();
        map.put ( ESCAPE_CHAR, ESCAPE_CHAR );
        map.put ( NODE_START_CHAR, NODE_START_CHAR );
        map.put ( NODE_END_CHAR, NODE_END_CHAR );
        map.put ( EXTENSION_START_CHAR, EXTENSION_START_CHAR );
        map.put ( 's', ' ' );
        map.put ( 't', '\t' );
        map.put ( 'n', '\n' );
        map.put ( 'r', '\r' );
        map.put ( ATTRIBUTES_START_CHAR, ATTRIBUTES_START_CHAR );
        map.put ( ATTRIBUTES_END_CHAR, ATTRIBUTES_END_CHAR );
        map.put ( ATTRIBUTE_ASSIGN_CHAR, ATTRIBUTE_ASSIGN_CHAR );
        map.put ( QUOTED_STRING_DELIMITER_CHAR, QUOTED_STRING_DELIMITER_CHAR );
        map.put ( RAW_STRING_START_CHAR, RAW_STRING_START_CHAR );
        map.put ( NAMESPACE_SEPARATOR_CHAR, NAMESPACE_SEPARATOR_CHAR );

        return Collections.unmodifiableMap ( map );
    }


    // Node Tag

    public static final Set<Character> INVALID_TAG_CHARS =
        Set.of (
            NODE_START_CHAR, NODE_END_CHAR, EXTENSION_START_CHAR,
            ' ', '\t', '\n', '\r',
            ATTRIBUTES_START_CHAR, ATTRIBUTES_END_CHAR, ATTRIBUTE_ASSIGN_CHAR,
            QUOTED_STRING_DELIMITER_CHAR, RAW_STRING_START_CHAR,
            NAMESPACE_SEPARATOR_CHAR );

    public static final Map<Character, Character> TAG_ESCAPE_CHARS = SHARED_ESCAPE_CHARS;


    // Text

    public static final Set<Character> INVALID_TEXT_CHARS =
        Set.of ( NODE_START_CHAR, NODE_END_CHAR, EXTENSION_START_CHAR );

    public static final @NotNull Map<Character, Character> TEXT_ESCAPE_CHARS = SHARED_ESCAPE_CHARS;


    // Other
    public static final char PATH_SEPARATOR = '/';
    public static final @NotNull String PDML_FILE_EXTENSION = "pdml";
}
