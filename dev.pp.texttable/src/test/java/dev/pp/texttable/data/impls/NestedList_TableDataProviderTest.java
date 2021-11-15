package dev.pp.texttable.data.impls;

import dev.pp.texttable.data.TableDataProviderUtilities;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NestedList_TableDataProviderTest {

    @Test
    void test() {

        List<List<String>> rows = new ArrayList<>();
        rows.add ( List.of ( "c11", "c12", "c13") );
        rows.add ( List.of ( "c21", "c22", "c23") );
        rows.add ( List.of ( "c31", "c32", "c33") );

        NestedList_TableDataProvider<String> provider = new NestedList_TableDataProvider<> ( rows, 3 );
        assertEquals ( "c11, c12, c13 / c21, c22, c23 / c31, c32, c33",
            TableDataProviderUtilities.dataToString ( provider, " / ", ", " ) );
    }
}