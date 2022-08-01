package dev.pdml.ext.extensions.types;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.PDMLType;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.ext.extensions.utilities.PDMLEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.DataType;
import dev.pp.datatype.nonUnion.scalar.impls.date.DateDataType;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;

public class PDMLDateType extends PDMLType<LocalDate> {

    public PDMLDateType ( DataType<LocalDate> parameterType ) {

        super ( parameterType );
        // this.parameterType = parameterType;
    }

    public PDMLDateType () {

        super ( DateDataType.DEFAULT );
        // this ( false, null, null, null, 0, 0, null, null );
    }


    public LocalDate readPDMLObject ( @NotNull PDMLReader reader, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        return dataType.parseAndValidate ( reader.readText(), nodeName.getToken() );
    }

    public void insertPDMLObject ( LocalDate date, @NotNull PDMLReader reader, @Nullable TextToken errorToken )
        throws IOException {

        StringWriter writer = new StringWriter();
        dataType.writeObject ( date, writer );

        reader.insertStringToRead ( PDMLEscaper.escapeNodeText ( writer.toString() ) );
    }
}
