package dev.pp.pdml.data;

import dev.pp.core.basics.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CorePdmlConstants {

    public static final char NODE_START_CHAR = '[';
    public static final char NODE_END_CHAR = ']';

    public static final char ESCAPE_CHAR = '\\';

    public static final @NotNull Set<Character> TAG_END_CHARS =
        Set.of (
            ']',
            ' ', '\t', '\n', '\r' );

    public static final @NotNull Set<Character> TEXT_END_CHARS =
        Set.of ( '[', ']' );

    public static final @NotNull Set<Character> INVALID_TAG_CHARS =
        Set.of (
            '[', ']',
            ' ', '\t', '\n', '\r', '\f',
            '^', '(', ')', '=', '"', '~', '|', ':', ',', '`', '!', '$' );

    public static final @NotNull Set<Character> INVALID_TEXT_CHARS =
        Set.of ( '[', ']', '^' );

    public static final @NotNull Map<Character, Character> TAG_AND_TEXT_ESCAPE_CHARS = createTagEscapeChars ();

    private static @NotNull Map<Character, Character> createTagEscapeChars() {

        Map<Character, Character> map = new HashMap<>();
        map.put ( '\\', '\\' );
        map.put ( 's', ' ' );
        map.put ( 't', '\t' );
        map.put ( 'n', '\n' );
        map.put ( 'r', '\r' );
        map.put ( 'f', '\f' );

        for ( Character c : INVALID_TAG_CHARS ) {
            if ( c != ' '
                && c != '\t'
                && c != '\n'
                && c != '\r'
                && c != '\f') {

                map.put ( c, c );
            }
        }

        return Collections.unmodifiableMap ( map );
    }

    // Other
    public static final char PATH_SEPARATOR = '/';
    public static final @NotNull String PDML_FILE_EXTENSION = "pdml";
}
