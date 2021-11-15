package dev.pdml.ext.scripting;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

class ScriptUtilsTest {

    @Test
    public void testExecuteScriptWithContext() throws IOException {

        String script = "context.write ( 'Hello from script context' );";

        Writer writer = new FileWriter ( "C:\\temp\\scriptContextOutput.txt" );
        ScriptingContext context = new WriterScriptingContext ( writer );

        // ScriptUtils.executeScriptWithWriter ( script, new PrintWriter ( System.out ), "js" );
        ScriptingUtils.executeJavascriptWithContext ( script, context );
        writer.close();
    }

    @Test
    public void testExecuteScriptWithWriter() throws IOException {

        String script = "writer.write ( 'Hello from script' );";

        Writer writer = new FileWriter ( "C:\\temp\\scriptOutput.txt" );

        // ScriptUtils.executeScriptWithWriter ( script, new PrintWriter ( System.out ), "js" );
        ScriptingUtils.executeJavascriptWithWriter ( script, writer );
        writer.close();
    }
}