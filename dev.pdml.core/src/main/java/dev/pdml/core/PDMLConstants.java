package dev.pdml.core;

import dev.pp.text.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class PDMLConstants {

    // node
    public static final char NODE_START = '[';
    public static final char NODE_END = ']';
    public static final char NAME_VALUE_SEPARATOR = ' ';
    public static final char NODE_END_TAG_SYMBOL = '/';

    // namespace
    public static final char NAMESPACE_PREFIX_NAME_SEPARATOR = ':';
    public static final char NAMESPACE_DECLARATION_START_CHAR = '(';
    public static final String NAMESPACE_DECLARATION_START = "(!ns";
    public static final char NAMESPACE_DECLARATION_END = ')';
    public static final char NAMESPACE_DECLARATION_SEPARATOR = ' ';
    public static final URI UNKNOWN_URI = getUnknownURI();

    // attribute
    public static final char ATTRIBUTES_START = '(';
    public static final char ATTRIBUTES_END = ')';
    public static final char ATTRIBUTE_ASSIGN = '=';
    public static final char ATTRIBUTE_VALUE_DOUBLE_QUOTE = '"';
    public static final char ATTRIBUTE_VALUE_SINGLE_QUOTE = '\'';
    public static final char ATTRIBUTES_SEPARATOR = ' ';

    // comment
    public static final @NotNull String COMMENT_START = "[-";
    public static final @NotNull String COMMENT_END = "-]";
    public static final char COMMENT_SYMBOL = '-';

    public static final char ESCAPE_CHARACTER = '\\';
    public static final char TAB = '\t';

    public static final @NotNull String PDML_FILE_EXTENSION = "pdml";

    public static final @NotNull String NEW_LINE = System.getProperty ( "line.separator" );


    private static URI getUnknownURI() {

        try {
            return new URI ( "http://www.unknown-uri.com" );
        } catch ( URISyntaxException e ) {
            throw new RuntimeException ( e );
        }
    }
}
