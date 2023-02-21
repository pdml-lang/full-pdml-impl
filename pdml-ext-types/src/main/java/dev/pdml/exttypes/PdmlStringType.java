package dev.pdml.exttypes;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.nodespec.PdmlType;
import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.utilities.PdmlEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.datatype.nonunion.scalar.impls.Null.NullDataType;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class PdmlStringType extends PdmlType<String> {

    // public PDMLStringType ( @NotNull DataType<String> parameterType ) { super ( parameterType ); }

    public PdmlStringType () {
        super ( CommonDataTypes.STRING );
    }

    public @NotNull String readPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        TextLocation location = reader.currentLocation();
        @Nullable String string = reader.readText();
        NullDataType.checkNotNullString ( string, location );
        assert string != null;
        return string;
    }

    public void insertPDMLObject ( @NotNull String string, @NotNull PdmlReader reader, @Nullable TextToken errorToken ) {
        reader.insertStringToRead ( PdmlEscaper.escapeNodeText ( string ) );
    }
}
