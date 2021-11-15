package dev.pp.datatype.common.path;

import dev.pp.datatype.DataType;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.parser.DataParserException;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.function.Supplier;

public class Path_DataType {

    public static final String DEFAULT_NAME = "path";

    public static DataParser<Path> DEFAULT_PARSER = ( string, token ) -> {

        try {
            return Path.of ( string );
        } catch ( InvalidPathException e ) {
            throw new DataParserException (
                "INVALID_FILE_PATH",
                "'" + string + "' is an invalid file path. Reason: " + e.getMessage(),
                token, e );
        }
    };

    public static DataWriter<Path> DEFAULT_WRITER = ( path, writer, nullString ) -> {

        if ( path != null ) {
            writer.write ( path.normalize().toString() );
        } else {
            writer.write ( nullString );
        }
    };

    public static SimpleDocumentation DEFAULT_DOCUMENTATION = new SimpleDocumentation (
    "File or Directory Path",
    "An absolute or relative file or directory path.",
    "docs/health/diet.pml" );

    public static DataType<Path> create (
        @NotNull String name,
        boolean isNullable,
        @Nullable DataParser<Path> parser,
        @Nullable DataWriter<Path> writer,
        @Nullable DataValidator<Path> validator,
        @Nullable Supplier<Path> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return new DataType<> (
            name, isNullable, parser, writer, validator, defaultValueSupplier, documentation );
    }

    public static DataType<Path> createNonNullable (
        @Nullable DataValidator<Path> validator,
        @Nullable Supplier<Path> defaultValueSupplier ) {

        return create (
            DEFAULT_NAME, false, DEFAULT_PARSER, DEFAULT_WRITER,
            validator, defaultValueSupplier, () -> DEFAULT_DOCUMENTATION );
    }

    public static DataType<Path> createNullable (
        @Nullable DataValidator<Path> validator,
        @Nullable Supplier<Path> defaultValueSupplier ) {

        return create (
            DEFAULT_NAME, true, DEFAULT_PARSER, DEFAULT_WRITER,
            validator, defaultValueSupplier, () -> DEFAULT_DOCUMENTATION );
    }
}
