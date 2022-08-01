package dev.pdml.ext.utilities.scriptingapidoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScriptingBindingsDocletTest {

    @Test
    public void testRemovePackagePaths() {

        assertEquals ( "String", ScriptingBindingsDoclet.removePackagePaths ( "String" ) );
        assertEquals ( "String", ScriptingBindingsDoclet.removePackagePaths ( "java.lang.String" ) );
        assertEquals ( "List<String>", ScriptingBindingsDoclet.removePackagePaths ( "java.util.List<java.lang.String>" ) );
        assertEquals ( "Map<String, Integer>", ScriptingBindingsDoclet.removePackagePaths ( "java.util.Map<java.lang.String, java.lang.Integer>" ) );
    }

}