package dev.pdml.ext.extensions.types;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.PDMLType;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.ext.extensions.utilities.PDMLEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PDMLStringOrNullType extends PDMLType<String> {

    // public PDMLStringOrNullType ( @NotNull DataType<String> parameterType ) { super ( parameterType ); }

    public PDMLStringOrNullType () {
        super ( CommonDataTypes.STRING_OR_NULL );
    }


    public @Nullable String readPDMLObject ( @NotNull PDMLReader reader, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        return reader.readText();
    }

    public void insertPDMLObject ( @Nullable String string, @NotNull PDMLReader reader, @Nullable TextToken errorToken ) {

        if ( string != null )
            reader.insertStringToRead ( PDMLEscaper.escapeNodeText ( string ) );
    }
}
