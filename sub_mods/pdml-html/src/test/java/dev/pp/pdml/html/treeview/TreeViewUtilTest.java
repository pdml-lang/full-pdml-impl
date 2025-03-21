package dev.pp.pdml.html.treeview;

import dev.pp.pdml.data.util.TestDoc;
import dev.pp.core.basics.utilities.file.TempFileUtils;
import dev.pp.core.text.resource.reader.TextResourceReader;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class TreeViewUtilTest {

    @Test
    void testShowTree() throws Exception {

        String pdmlTestDoc = TestDoc.getPdmlTestDoc();
        TextResourceReader reader = new TextResourceReader ( pdmlTestDoc );

        // delete on exit
        Path htmlFile = TempFileUtils.createEmptyTempFile ( "test", "html", false );

        // TreeViewUtil.showCode ( reader, PdmlParserConfig.defaultConfig(), true, htmlFile );
    }
}
