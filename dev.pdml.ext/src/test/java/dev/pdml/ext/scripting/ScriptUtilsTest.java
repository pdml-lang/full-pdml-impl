package dev.pdml.ext.scripting;

import dev.pp.text.utilities.FileUtilities;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScriptUtilsTest {

/*
    @Test
    public void testExecuteScriptWithContext() throws IOException {

        String script = "context.write ( 'Hello from script context 2' );";

        Writer writer = new FileWriter ( "C:\\temp\\scriptContextOutput.txt" );
        ScriptingContext context = new WriterScriptingContext ( writer );

        // ScriptUtils.executeScriptWithWriter ( script, new PrintWriter ( System.out ), "js" );
        ScriptingUtils.executeJavascriptWithContext ( script, context );
        writer.close();
    }
*/

    @Test
    public void testExecuteScriptWithWriter() throws IOException {

        String script = "writer.write ( 'Hello from script' );";
        File file = FileUtilities.createEmptyTempFile ( true );
        Writer writer = new FileWriter ( file );

        // ScriptUtils.executeScriptWithWriter ( script, new PrintWriter ( System.out ), "js" );
        // ScriptingUtils.executeJavascriptWithWriter ( script, writer );
        ScriptingUtils.executeJavascriptWithBindings ( script, Map.of ( "writer", writer ), true );
        writer.close();
        assertEquals ( "Hello from script", FileUtilities.readTextFromUTF8File ( file ) );
    }
}