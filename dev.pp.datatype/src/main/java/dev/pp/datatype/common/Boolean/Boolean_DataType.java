package dev.pp.datatype.common.Boolean;

import dev.pp.datatype.DataType;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.parser.DataParserException;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;

import java.util.function.Supplier;

public class Boolean_DataType {

    public static final String DEFAULT_NAME = "boolean";

    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";
    public static final String YES_VALUE = "yes";
    public static final String NO_VALUE = "no";

    public static final String VALID_VALUES =
        YES_VALUE + ", " + NO_VALUE + ", " + TRUE_VALUE + ", and " + FALSE_VALUE + " (case-insensitive)";

    public static DataParser<Boolean> DEFAULT_PARSER = ( string, token ) -> {

        String lstring = string.toLowerCase();

        if ( lstring.equals ( YES_VALUE ) || lstring.equals ( TRUE_VALUE ) ) {
            return true;

        } else if ( lstring.equals ( NO_VALUE ) || lstring.equals ( FALSE_VALUE ) ) {
            return false;

        } else {
            throw new DataParserException (
                "ILLEGAL_BOOLEAN_VALUE",
                "'" + string + "' is an invalid boolean value. Valid values are: " + VALID_VALUES + ".",
                token, null );
        }
    };

    public static DataWriter<Boolean> DEFAULT_WRITER = ( value, writer, nullString ) -> {

        if ( value != null ) {
            writer.write ( value ? YES_VALUE : NO_VALUE );
        } else {
            writer.write ( nullString );
        }
    };

    public static SimpleDocumentation DEFAULT_DOCUMENTATION = new SimpleDocumentation (
    "Boolean", "A boolean value. Valid values are: " + VALID_VALUES, "yes" );

    public static DataType<Boolean> create (
        @NotNull String name,
        boolean isNullable,
        @Nullable DataParser<Boolean> parser,
        @Nullable DataWriter<Boolean> writer,
        @Nullable DataValidator<Boolean> validator,
        @Nullable Supplier<Boolean> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return new DataType<> (
            name, isNullable, parser, writer, validator, defaultValueSupplier, documentation );
    }

    public static DataType<Boolean> createNonNullable ( boolean defaultValue ) {

        return create (
            DEFAULT_NAME, false, DEFAULT_PARSER, DEFAULT_WRITER,
            null, () -> defaultValue, () -> DEFAULT_DOCUMENTATION );
    }

    public static DataType<Boolean> createNullable ( @Nullable Boolean defaultValue ) {

        return create (
            DEFAULT_NAME, true, DEFAULT_PARSER, DEFAULT_WRITER,
            null, () -> defaultValue, () -> DEFAULT_DOCUMENTATION );
    }
}
