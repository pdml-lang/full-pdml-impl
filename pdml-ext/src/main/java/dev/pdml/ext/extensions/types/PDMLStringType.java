package dev.pdml.ext.extensions.types;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.PDMLType;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.ext.extensions.utilities.PDMLEscaper;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.datatype.nonUnion.scalar.impls.Null.NullDataType;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PDMLStringType extends PDMLType<String> {

    // public PDMLStringType ( @NotNull DataType<String> parameterType ) { super ( parameterType ); }

    public PDMLStringType() {
        super ( CommonDataTypes.STRING );
    }

    public @NotNull String readPDMLObject ( @NotNull PDMLReader reader, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        @Nullable String string = reader.readText();
        NullDataType.checkNotNullString ( string, nodeName.getToken() );
        assert string != null;
        return string;
    }

    public void insertPDMLObject ( @NotNull String string, @NotNull PDMLReader reader, @Nullable TextToken errorToken ) {
        reader.insertStringToRead ( PDMLEscaper.escapeNodeText ( string ) );
    }
}
