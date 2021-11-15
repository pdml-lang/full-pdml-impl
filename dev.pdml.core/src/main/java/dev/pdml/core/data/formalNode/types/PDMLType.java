package dev.pdml.core.data.formalNode.types;

import dev.pp.datatype.DataType;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.reader.exception.TextReaderException;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pp.text.token.TextToken;

public abstract class PDMLType<T> {

    protected final DataType<T> parameterType;

    protected PDMLType ( DataType<T> parameterType ) {

        this.parameterType = parameterType;
    }

    public void validate ( T object, @Nullable TextToken token ) throws DataValidatorException {

        parameterType.validate ( object, token );
    }

    public abstract T readPDMLObject ( @NotNull PXMLReader reader, @NotNull ASTNodeName nodeName ) throws TextReaderException;

    public abstract void insertPDMLObject ( T object, @NotNull PXMLReader reader, @Nullable TextToken errorToken )
        throws PXMLResourceException;

    public void readValidateAndInsertPDMLObject ( @NotNull PXMLReader reader, @NotNull ASTNodeName nodeName )
        throws TextReaderException {

        TextToken objectToken = reader.currentToken();
        T o = readPDMLObject ( reader, nodeName );
        validate ( o, objectToken );
        insertPDMLObject ( o, reader, objectToken );
    }
}
