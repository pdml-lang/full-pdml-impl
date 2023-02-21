package dev.pdml.exttypes;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.nodespec.PdmlType;
import dev.pdml.reader.PdmlReader;
import dev.pdml.shared.utilities.PdmlEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.DataType;
import dev.pp.datatype.nonunion.scalar.impls.date.DateDataType;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;

public class PdmlDateType extends PdmlType<LocalDate> {

    public PdmlDateType ( DataType<LocalDate> parameterType ) {
        super ( parameterType );
    }

    public PdmlDateType () {
        super ( DateDataType.DEFAULT );
    }


    public LocalDate readPDMLObject ( @NotNull PdmlReader reader, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        TextLocation location = reader.currentLocation();
        return dataType.parseAndValidate ( reader.readText(), location );
    }

    public void insertPDMLObject ( LocalDate date, @NotNull PdmlReader reader, @Nullable TextToken errorToken )
        throws IOException {

        StringWriter writer = new StringWriter();
        dataType.writeObject ( date, writer );

        reader.insertStringToRead ( PdmlEscaper.escapeNodeText ( writer.toString() ) );
    }
}
