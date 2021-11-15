package dev.pp.datatype.common.string;

import dev.pp.datatype.DataType;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;

import java.util.function.Supplier;

public class String_DataType {

    public static final String DEFAULT_NAME = "string";

    public static DataParser<String> DEFAULT_PARSER = ( string, token ) -> string;

    public static DataWriter<String> DEFAULT_WRITER = ( string, writer, nullString ) -> {

        writer.write ( string != null ? string : nullString );
    };

    public static SimpleDocumentation DEFAULT_DOCUMENTATION = new SimpleDocumentation (
    "String", "A sequence of Unicode characters.", "Any text" );

    public static DataType<String> create (
        @NotNull String name,
        boolean isNullable,
        @Nullable DataParser<String> parser,
        @Nullable DataWriter<String> writer,
        @Nullable DataValidator<String> validator,
        @Nullable Supplier<String> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return new DataType<> (
            name, isNullable, parser, writer, validator, defaultValueSupplier, documentation );
    }

    public static DataType<String> createNonNullable (
        @Nullable String defaultValue,
        @Nullable DataValidator<String> validator ) {

        return create (
            DEFAULT_NAME, false, DEFAULT_PARSER, DEFAULT_WRITER,
            validator, defaultValue != null ? () -> defaultValue : null, () -> DEFAULT_DOCUMENTATION );
    }

    public static DataType<String> createNullable (
        @Nullable Supplier<String> defaultValueSupplier,
        @Nullable DataValidator<String> validator ) {

        return create (
            DEFAULT_NAME, true, DEFAULT_PARSER, DEFAULT_WRITER,
            validator, defaultValueSupplier, () -> DEFAULT_DOCUMENTATION );
    }
}
