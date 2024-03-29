package dev.pdml.exttypes;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.nodespec.PdmlType;
import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.utilities.PdmlEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PdmlStringOrNullType extends PdmlType<String> {

    // public PDMLStringOrNullType ( @NotNull DataType<String> parameterType ) { super ( parameterType ); }

    public PdmlStringOrNullType () {
        super ( CommonDataTypes.STRING_OR_NULL );
    }


    public @Nullable String readPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        return reader.readText();
    }

    public void insertPDMLObject ( @Nullable String string, @NotNull PdmlReader reader, @Nullable TextToken errorToken ) {

        if ( string != null )
            reader.insertStringToRead ( PdmlEscaper.escapeNodeText ( string ) );
    }
}
