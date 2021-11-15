package dev.pp.texttable.writer.pretty;

import dev.pp.texttable.data.impls.FormField;
import dev.pp.texttable.data.impls.FormFields_TableDataProvider;
import dev.pp.texttable.data.impls.NestedList_TableDataProvider;
import dev.pp.texttable.writer.pretty.config.PrettyTableDataTextWriterConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrettyTableDataTextWriterImplTest {

    @Test
    public void test() throws IOException {

        List<List<String>> rows = new ArrayList<> ();
        rows.add ( List.of ( "c11", "c12", "c13") );
        rows.add ( List.of ( "c21", "c22", "c23") );
        rows.add ( List.of ( "c31", "c32", "c33") );
        NestedList_TableDataProvider<String> data = new NestedList_TableDataProvider<> ( rows, 3 );

        PrettyTableDataTextWriterImpl<List<String>, String> tableWriter = new PrettyTableDataTextWriterImpl<>();
        StringWriter writer = new StringWriter();
        tableWriter.write ( data, writer, new PrettyTableDataTextWriterConfig<> ( data ) );
        // assertEquals ( "qwe", writer.toString() );
    }

    @Test
    public void testFormFields() throws IOException {

        List<FormField<String>> formFields = new ArrayList<>();
        formFields.add ( new FormField<String> ( "Label 1", "Value 1" ) );
        formFields.add ( new FormField<String> ( "Very long label 2", "Value 2 djkhf kjdhf gkjdsk hdfhg dkfgk dfg kdhgk dkfgk sdfkh kdfkg kdf kdfk kdf kdkjhf kdskjg dfg kdf end value 21" ) );
        formFields.add ( new FormField<String> ( null, null ) );
        formFields.add ( new FormField<String> ( "Label 4", "Value 4" ) );
        FormFields_TableDataProvider<String> data = new FormFields_TableDataProvider<> ( formFields );

        PrettyTableDataTextWriterImpl<FormField<String>, String> tableWriter = new PrettyTableDataTextWriterImpl<>();
        StringWriter writer = new StringWriter();
        tableWriter.write ( data, writer, new PrettyTableDataTextWriterConfig<> ( data ) );
        // assertEquals ( "qwe", writer.toString() );
    }
}