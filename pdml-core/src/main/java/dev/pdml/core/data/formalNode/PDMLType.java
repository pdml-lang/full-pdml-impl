package dev.pdml.core.data.formalNode;

import dev.pp.datatype.DataType;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.PDMLReader;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public abstract class PDMLType<T> {

    protected final DataType<T> dataType;

    protected PDMLType ( @NotNull DataType<T> dataType ) {
        this.dataType = dataType;
    }

    public void validate ( @Nullable T object, @Nullable TextToken token ) throws DataValidatorException {
        dataType.validate ( object, token );
    }

    public abstract T readPDMLObject ( @NotNull PDMLReader reader, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException;

    public abstract void insertPDMLObject ( T object, @NotNull PDMLReader reader, @Nullable TextToken errorToken )
        throws IOException;

    public void readValidateAndInsertPDMLObject ( @NotNull PDMLReader reader, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException, DataValidatorException {

        TextToken objectToken = reader.currentToken();
        T object = readPDMLObject ( reader, nodeName );
        validate ( object, objectToken );
        insertPDMLObject ( object, reader, objectToken );
    }
}
