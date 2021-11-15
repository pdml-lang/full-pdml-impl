package dev.pdml.ext.scripting;

import dev.pp.text.annotations.NotNull;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.Writer;

public class ScriptingUtils {

    // Note: Currently (2021-09) only Javascript is well supported in GraalVM

    private static final String JAVASCRIPT_LANGUAGE_ID = "js";

    public static void executeJavascriptWithContext (
        @NotNull String script,
        @NotNull ScriptingContext JSContext ) {

        try ( Context context = Context.newBuilder()
            .option ( "engine.WarnInterpreterOnly", "false" ) // see Warning: Implementation does not support runtime compilation.
            // at https://www.graalvm.org/reference-manual/js/FAQ/#errors
            .allowAllAccess ( true )
            .build() ) {

            Value bindings = context.getBindings ( JAVASCRIPT_LANGUAGE_ID );
            bindings.putMember ( "context", JSContext );
            // TODO check exception
            context.eval ( JAVASCRIPT_LANGUAGE_ID, script );
            // Value value = context.eval ( JAVASCRIPT_LANGUAGE_ID, script );
            // System.out.println ( value );
        }
    }

    public static void executeJavascriptWithWriter (
        @NotNull String script,
        @NotNull Writer writer ) {

        try ( Context context = Context.newBuilder()
            .option ( "engine.WarnInterpreterOnly", "false" ) // see Warning: Implementation does not support runtime compilation.
                                                              // at https://www.graalvm.org/reference-manual/js/FAQ/#errors
            .allowAllAccess ( true )
            .build() ) {

            Value bindings = context.getBindings ( JAVASCRIPT_LANGUAGE_ID );
            bindings.putMember ( "writer", writer );
            // TODO check exception
            context.eval ( JAVASCRIPT_LANGUAGE_ID, script );
            // Value value = context.eval ( JAVASCRIPT_LANGUAGE_ID, script );
            // System.out.println ( value );
        }
    }

/*
    public static void executeScriptWithWriter (
        @NotNull String script,
        @NotNull Writer writer,
        @NotNull String language ) {

        try ( Context context = Context.newBuilder()
            .option ( "engine.WarnInterpreterOnly", "false" ) // see Warning: Implementation does not support runtime compilation.
                                                              // at https://www.graalvm.org/reference-manual/js/FAQ/#errors
            .allowAllAccess ( true )
            .build() ) {

            Value bindings = context.getBindings ( language );
            bindings.putMember ( "writer", writer );
            Value value = context.eval ( language, script );
            System.out.println ( value );
        }
    }
*/
}
