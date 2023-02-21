package dev.pdml.parser.nodespec;

import dev.pdml.data.node.NodeName;
import dev.pdml.reader.PdmlReader;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.DataType;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public abstract class PdmlType<T> {

    protected final DataType<T> dataType;

    protected PdmlType ( @NotNull DataType<T> dataType ) {
        this.dataType = dataType;
    }

    public void validate ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {
        dataType.validate ( object, token );
    }

    // TODO? public abstract T readPDMLObject ( @NotNull PDMLReader reader )
    public abstract T readPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException;

    public abstract void insertPDMLObject ( T object, @NotNull PdmlReader reader, @Nullable TextToken errorToken )
        throws IOException;

    public void readValidateAndInsertPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException, DataValidatorException {

        TextToken objectToken = reader.currentToken();
        T object = readPDMLObject ( reader, nodeName );
        validate ( object, objectToken );
        insertPDMLObject ( object, reader, objectToken );
    }
}
