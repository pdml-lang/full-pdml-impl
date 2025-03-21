package dev.pp.pdml.ext.types.instances;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.ext.types.PdmlType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTypeTest {

    @Test
    void test() throws Exception {

        PdmlType<?> type = LocalDateType.NON_NULL_INSTANCE;

        TaggedNode rootNode = TextTypeTest.parse ( "[root 2024-09-03]", type );
        assertEquals ( "2024-09-03", rootNode.toText() );
        LocalDate date = rootNode.getCastedJavaObjectContained();
        assertNotNull ( date );
        assertEquals ( "2024-09-03", date.toString() );

        rootNode = TextTypeTest.parse ( "[untyped_root ^t[local_date 2024-09-03]]", type );
        assertEquals ( "2024-09-03", rootNode.toText() );

        assertThrows ( PdmlException.class,
            () -> TextTypeTest.parse ( "[root 2024-09]", type ) );

        assertThrows ( PdmlException.class,
            () -> TextTypeTest.parse ( "[root]", type ) );
    }
}
