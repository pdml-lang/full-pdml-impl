package dev.pdml.core.data.formalNode.types.standard;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.types.PDMLType;
import dev.pdml.core.reader.exception.PXMLResourceException;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.utilities.PXMLEscaper;
import dev.pp.datatype.DataType;
import dev.pp.datatype.common.string.String_DataType;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;

public class StringType extends PDMLType<String> {

    public StringType ( DataType<String> parameterType ) {

        super ( parameterType );
        // this.parameterType = parameterType;
    }

    public StringType() {

        super ( String_DataType.createNonNullable ( null, null ) );
        // this ( false, null, null, null, 0, 0, null, null );
    }


    public String readPDMLObject ( @NotNull PXMLReader reader, @NotNull ASTNodeName nodeName )
        throws TextReaderException {

        return reader.readText();
    }

    public void insertPDMLObject ( String string, @NotNull PXMLReader reader, @Nullable TextToken errorToken )
        throws PXMLResourceException {

        reader.insertStringToRead ( PXMLEscaper.escapeNodeText ( string ), errorToken );
    }
}
