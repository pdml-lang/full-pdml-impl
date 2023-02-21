package dev.pdml.shared.constants;

import dev.pp.basics.annotations.NotNull;

public class PdmlExtensionsConstants {

    // Node
    public static final char NODE_END_TAG_SYMBOL = '/';

    // Namespace
    public static final char NAMESPACE_PREFIX_NAME_SEPARATOR = ':';
    public static final char NAMESPACE_DECLARATION_START_CHAR = '(';
    public static final String NAMESPACE_DECLARATION_START = "(!ns";
    public static final char NAMESPACE_DECLARATION_END = ')';
    public static final char NAMESPACE_DECLARATION_SEPARATOR = ' ';

    // Attribute
    public static final char ATTRIBUTES_SYMBOL = '@';
    public static final @NotNull String ATTRIBUTES_START = String.valueOf ( CorePdmlConstants.NODE_START ) + ATTRIBUTES_SYMBOL;
    public static final char ATTRIBUTES_START_CHAR = ATTRIBUTES_START.charAt ( 0 );
    public static final char ATTRIBUTES_END = CorePdmlConstants.NODE_END;
    public static final char ALTERNATIVE_ATTRIBUTES_START = '(';
    public static final char ALTERNATIVE_ATTRIBUTES_END = ')';
    public static final char ATTRIBUTE_ASSIGN = '=';
    public static final char ATTRIBUTE_VALUE_DOUBLE_QUOTE = '"';
    public static final char ATTRIBUTE_VALUE_SINGLE_QUOTE = '\'';

    // Comment
    public static final char COMMENT_SYMBOL = '-';
    public static final @NotNull String COMMENT_START = String.valueOf ( CorePdmlConstants.NODE_START ) + COMMENT_SYMBOL;
    public static final @NotNull String COMMENT_END = String.valueOf ( COMMENT_SYMBOL ) + CorePdmlConstants.NODE_END;

    // Other
    public static final char TAB = '\t';
    public static final char PATH_SEPARATOR = '/';
    public static final @NotNull String PATH_SEPARATOR_AS_STRING = String.valueOf ( PATH_SEPARATOR );
}
