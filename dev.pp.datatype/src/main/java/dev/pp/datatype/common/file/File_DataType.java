package dev.pp.datatype.common.file;

import dev.pp.datatype.DataType;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.utilities.FileUtilities;

import java.io.File;
import java.util.function.Supplier;

public class File_DataType { //extends ParameterType<File> {

    /* TODO
        - FileExtensionValidator
        - FileNameMatchesRegexValidator
        - FileNameExtensionMatchesRegexValidator
     */

    // static members

    public static final String DEFAULT_NAME = "file";

    public static DataParser<File> DEFAULT_PARSER = ( string, token) -> { return new File ( string ); };

    public static DataWriter<File> DEFAULT_WRITER = ( file, writer, nullString ) -> {

        if ( file != null ) {
            writer.write ( FileUtilities.getAbsoluteOSPath ( file ) );
        } else {
            writer.write ( nullString );
        }
    };

    public static SimpleDocumentation DEFAULT_DOCUMENTATION = new SimpleDocumentation (
    "File",
    "An absolute or relative file path. It the path is relative, it is relative to the current working directory.",
    "docs/health/diet.pml" );

    public static DataType<File> create (
        @NotNull String name,
        boolean isNullable,
        @Nullable DataParser<File> parser,
        @Nullable DataWriter<File> writer,
        @Nullable DataValidator<File> validator,
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return new DataType<> (
            name, isNullable, parser, writer, validator, defaultValueSupplier, documentation );
    }

    public static DataType<File> createNonNullable (
        @Nullable DataValidator<File> validator,
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return create (
            DEFAULT_NAME, false, DEFAULT_PARSER, DEFAULT_WRITER,
            validator, defaultValueSupplier, documentation );
    }

    public static DataType<File> createNullable (
        @Nullable DataValidator<File> validator,
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return create (
            DEFAULT_NAME, true, DEFAULT_PARSER, DEFAULT_WRITER,
            validator, defaultValueSupplier, documentation );
    }

    public static DataType<File> createForExistentFile (
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return createNonNullable ( new ExistentFileValidator(), defaultValueSupplier, documentation );
    }

    public static DataType<File> createForExistentFile (
        @Nullable Supplier<File> defaultValueSupplier ) {

        return createNonNullable ( new ExistentFileValidator(), defaultValueSupplier, () -> DEFAULT_DOCUMENTATION );
    }

    public static DataType<File> createForExistentFileOrNull (
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        return createNullable ( new ExistentFileValidator(), defaultValueSupplier, documentation );
    }

    // constructors
/*
    public File_ParameterType (
        @NotNull String name,
        @Nullable ObjectParser<File> parser,
        @Nullable ObjectWriter<File> writer,
        @Nullable ObjectValidator<File> validator,
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable SimpleDocumentation documentation ) {

        super ( name, false, parser, writer, validator, defaultValueSupplier, documentation );
    }

    public File_ParameterType (
        @NotNull String name,
        @Nullable Supplier<File> defaultValueSupplier,
        @Nullable SimpleDocumentation documentation ) {

        this ( name, DEFAULT_PARSER, DEFAULT_WRITER, DEFAULT_VALIDATOR, defaultValueSupplier, documentation );
    }

    public File_ParameterType ( @Nullable Path defaultValue ) {

        this ( DEFAULT_NAME,
            defaultValue == null ? null : () -> defaultValue,
            DEFAULT_DOCUMENTATION );
    }
*/
}
