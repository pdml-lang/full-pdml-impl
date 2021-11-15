package dev.pp.datatype;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;

import dev.pp.datatype.parser.DataParserException;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Supplier;

public class DataType<T> {


    // add final?
    public static String DEFAULT_NULL_ERROR_ID = "NULL_NOT_ALLOWED";
    public static String DEFAULT_NULL_ERROR_MESSAGE = "A value must be provided. 'null' (no value) is not allowed.";


    private final @NotNull String name;
    private final boolean isNullable;
    private @Nullable DataParser<T> parser = null;
    private @Nullable DataWriter<T> writer = null;
    private @Nullable DataValidator<T> validator = null;
    private @Nullable Supplier<T> defaultValueSupplier = null;
    private @Nullable Supplier<SimpleDocumentation> documentation = null;


    public DataType (
        @NotNull String name,
        boolean isNullable,
        @Nullable DataParser<T> parser,
        @Nullable DataWriter<T> writer,
        @Nullable DataValidator<T> validator,
        @Nullable Supplier<T> defaultValueSupplier,
        @Nullable Supplier<SimpleDocumentation> documentation ) {
        
        this.name = name;
        this.isNullable = isNullable;
        this.parser = parser;
        this.writer = writer;
        this.validator = validator;
        this.defaultValueSupplier = defaultValueSupplier;
        this.documentation = documentation;
    }

    public DataType (
        @NotNull String name,
        boolean isNullable ) {

        this.name = name;
        this.isNullable = isNullable;
    }


    // getters

    public @NotNull String getName() { return name; }

    public boolean isNullable() { return isNullable; }

    public @Nullable DataParser<T> getParser() { return parser; }

    public @Nullable DataWriter<T> getWriter() { return writer; }

    public @Nullable DataValidator<T> getValidator() { return validator; }

    public @Nullable Supplier<T> getDefaultValueSupplier() { return defaultValueSupplier; }

    public @Nullable Supplier<SimpleDocumentation> getDocumentation() { return documentation; }


    // setters

    public DataType<T> setParser ( @Nullable DataParser<T> parser ) {
        this.parser = parser;
        return this;
    }

    public DataType<T> setWriter ( @Nullable DataWriter<T> writer ) {
        this.writer = writer;
        return this;
    }

    public DataType<T> setValidator ( @Nullable DataValidator<T> validator ) {
        this.validator = validator;
        return this;
    }

    public DataType<T> setDefaultValueSupplier ( @Nullable Supplier<T> defaultValueSupplier ) {
        this.defaultValueSupplier = defaultValueSupplier;
        return this;
    }

    public DataType<T> setDefaultValue ( @Nullable T defaultValue ) {
        return setDefaultValueSupplier ( () -> defaultValue );
    }

    public DataType<T> setDocumentation ( @Nullable Supplier<SimpleDocumentation> documentation ) {
        this.documentation = documentation;
        return this;
    }

    /* TODO
    public ParameterType<T> setTitle ( @NotNUll String title ) {
    public ParameterType<T> setDescription ( @NotNUll String description ) {
    public ParameterType<T> setExamples ( @NotNUll String examples ) {
    */


    // validation

    public void validate (
        @Nullable T object,
        @Nullable TextToken token,
        @NotNull String nullErrorId,
        @NotNull String nullErrorMessage ) throws DataValidatorException {

        validateNull ( object, token, nullErrorId, nullErrorMessage );
        if ( object == null ) return;

        if ( validator != null ) {
            validator.validate ( object, token );
        }
    }

    public void validate ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {

        validate ( object, token, DEFAULT_NULL_ERROR_ID, DEFAULT_NULL_ERROR_MESSAGE );
    }

    public boolean isValid ( @Nullable T object ) {

        try {
            validate ( object, null );
            return true;
        } catch ( DataValidatorException e ) {
            return false;
        }
    }

    public void validateNull (
        @Nullable T object,
        @Nullable TextToken token,
        @NotNull String errorId,
        @NotNull String errorMessage ) throws DataValidatorException {

        if ( ! isNullValid ( object ) ) throw new DataValidatorException (
            errorId,
            errorMessage,
            token,
            null );
    }

    public void validateNull ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {

        validateNull ( object, token, DEFAULT_NULL_ERROR_ID, DEFAULT_NULL_ERROR_MESSAGE );
    }

    public boolean isNullValid ( @Nullable T object ) {

        return object != null || isNullable;
    }


    // default value

    public T getDefaultValue() {

        if ( defaultValueSupplier != null ) {
            return defaultValueSupplier.get();
        } else {
            throw new IllegalCallerException ( "No default value is provided for type '" + name + "'." );
        }
    }

    public T getDefaultValueOrElse ( T elseValue ) {

        if ( defaultValueSupplier != null ) {
            return defaultValueSupplier.get();
        } else {
            return elseValue;
        }
    }

    public T getDefaultValueOrNull() {

        if ( defaultValueSupplier != null ) {
            return defaultValueSupplier.get();
        } else {
            return null;
        }
    }


    // string operations

    public @Nullable T parse ( @Nullable String string, @Nullable TextToken token )
        throws DataParserException {

        if ( string == null || string.isEmpty() ) return null;

        if ( parser == null ) throw new IllegalCallerException ( "No parser is provided for type '" + name + "'." );

        return parser.parse ( string, token );
    }

    public @NotNull String objectToString ( @Nullable T object, @NotNull String nullString ) {

        if ( writer == null ) throw new IllegalCallerException ( "No writer is provided for type '" + name + "'." );

        StringWriter stringWriter = new StringWriter();
        try {
            writer.write ( object, stringWriter, nullString );
        } catch ( IOException e ) {
            // should never happen
            throw new RuntimeException ( e );
        }

        return stringWriter.toString();
    }


    @Override public String toString() { return name; }
}
