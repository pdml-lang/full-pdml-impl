package dev.pp.parameters.formalParameter;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;

import dev.pp.datatype.parser.DataParserException;
import dev.pp.datatype.writer.DataWriter;
import dev.pp.datatype.parser.DataParser;
import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.datatype.DataType;
import dev.pp.text.token.TextToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class FormalParameter<T> {


    private final @NotNull String name;
    private final @Nullable Set<String> alternativeNames;
    private final @NotNull DataType<T> type;
    private final int order;
    private final @Nullable Supplier<SimpleDocumentation> documentation;


    public FormalParameter (
        @NotNull String name,
        @Nullable Set<String> alternativeNames,
        @NotNull DataType<T> type,
        int order,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        if ( alternativeNames != null && alternativeNames.contains ( name ) ) throw new RuntimeException (
            "Name '" + name + "' is not unique." );

        this.name = name;
        this.alternativeNames = alternativeNames;
        this.type = type;
        this.order = order;
        this.documentation = documentation;
    }


    // getters

    public @NotNull String getName() { return name; }

    public @Nullable Set<String> getAlternativeNames () { return alternativeNames; }

    public @NotNull DataType<T> getType() { return type; }

    public int getOrder() { return order; }

    public @Nullable Supplier<SimpleDocumentation> getDocumentation() { return documentation; }


    // type getters

    public @NotNull String getTypeName() { return type.getName(); }

    public boolean isNullable() { return type.isNullable(); }

    public @Nullable DataParser<T> getParser() { return type.getParser(); }

    public @Nullable DataWriter<T> getWriter() { return type.getWriter(); }

    public @Nullable DataValidator<T> getValidator() { return type.getValidator(); }

    public @Nullable Supplier<T> getDefaultValueSupplier() { return type.getDefaultValueSupplier(); }


    // type wrappers

    public void validate ( T object, @Nullable TextToken token ) throws DataValidatorException {

        type.validate ( object, token );
    }

    public T getDefaultValue() { return type.getDefaultValue(); }

    public T getDefaultValueOrElse ( T elseValue ) { return type.getDefaultValueOrElse ( elseValue ); }

    public T getDefaultValueOrNull() { return type.getDefaultValueOrNull(); }

    public @NotNull String objectToString ( @Nullable T object, @Nullable String nullString ) {

        return type.objectToString ( object, nullString );
    }

    public @Nullable T stringToObjectWithoutValidation ( @Nullable String string, @Nullable TextToken token )
        throws DataParserException {

        return type.parse ( string, token );
    }

    public @Nullable T stringToValidatedObject ( @Nullable String string, @Nullable TextToken token )
        throws DataParserException, DataValidatorException {

        T object = stringToObjectWithoutValidation ( string, token );
        validate ( object, token );
        return object;
    }


    public @NotNull List<String> getAllNames() {

        ArrayList<String> list = new ArrayList<>();
        list.add ( name );
        if ( alternativeNames != null ) list.addAll ( alternativeNames );
        return list;
    }

    public boolean isRequired() { return getDefaultValueSupplier() == null; }

    @Override public String toString() { return name; }
}
