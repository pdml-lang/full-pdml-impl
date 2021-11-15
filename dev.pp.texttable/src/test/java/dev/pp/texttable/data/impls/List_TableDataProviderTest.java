package dev.pp.texttable.data.impls;

import dev.pp.text.location.TextLocation;
import dev.pp.texttable.data.TableDataColumn;
import dev.pp.texttable.data.TableDataProviderUtilities;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class List_TableDataProviderTest {

    @Test
    void test() {

        List<TextLocation> rows = new ArrayList<> ();
        rows.add ( new TextLocation ( new File ( "foo.txt"), 1, 2, null ) );
        rows.add ( new TextLocation ( new File ( "bar.txt"), 11, 22, null ) );

        List<TableDataColumn<TextLocation, String>> columns = new ArrayList<>();
        columns.add ( TableDataProviderUtilities.createTableDataColumn (
            "File", (textLocation) -> textLocation.getResourceAsFile().toString() ) );
        columns.add ( TableDataProviderUtilities.createTableDataColumn (
            "Line", (textLocation) -> String.valueOf ( textLocation.getLineNumber() ) ) );
        columns.add ( TableDataProviderUtilities.createTableDataColumn (
            "Column", (textLocation) -> String.valueOf ( textLocation.getColumnNumber() ) ) );


        List_TableDataProvider<TextLocation, String> provider = new List_TableDataProvider<> ( rows, columns );
        assertEquals ( "foo.txt, 1, 2 / bar.txt, 11, 22",
            TableDataProviderUtilities.dataToString ( provider, " / ", ", " ) );
    }
}