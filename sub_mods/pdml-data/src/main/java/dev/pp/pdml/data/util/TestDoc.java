package dev.pp.pdml.data.util;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.utilities.ResourcesUtils;

import java.io.IOException;
import java.nio.file.Path;

public class TestDoc {

    public static @NotNull String getPdmlTestDoc() {

        Path path = Path.of ( "dev/pp/pdml/data/util/PDML_test_doc.pdml" );
        try {
            return ResourcesUtils.readTextResource ( path, TestDoc.class );
        } catch ( IOException e ) {
            throw new RuntimeException ( "Error reading '" + path + "': " + e );
        }
    }
}
