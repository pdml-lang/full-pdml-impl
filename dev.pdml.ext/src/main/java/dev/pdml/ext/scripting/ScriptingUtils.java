package dev.pdml.ext.scripting;

import dev.pp.text.annotations.NotNull;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.Map;

public class ScriptingUtils {

    // Note: Currently (2021-09) only Javascript is well supported in GraalVM

    private static final String JAVASCRIPT_LANGUAGE_ID = "js";

/*
    public static void executeJavascriptWithBindings (
        @NotNull String script,
        @NotNull Map<String, Object> bindings,
        @NotNull TextErrorHandler errorHandler ) {

        try {
            executeJavascriptWithBindings ( script, bindings );
        } catch ( PolyglotException | IllegalStateException | IllegalArgumentException e ) {
            errorHandler.handleError (
                "SCRIPTING_ERROR",
                "The following error occurred when a script was executed: " + e.getMessage(),
                null );
        }
    }
*/

    public static void executeJavascriptWithBindings (
        @NotNull String script,
        @NotNull Map<String, Object> bindings,
        boolean allowAllAccess ) {

        try ( Context context = Context.newBuilder()
            .option ( "engine.WarnInterpreterOnly", "false" ) // see Warning: Implementation does not support runtime compilation.
            // at https://www.graalvm.org/reference-manual/js/FAQ/#errors
            .allowAllAccess ( allowAllAccess )
            .build() ) {

            Value contextBindings = context.getBindings ( JAVASCRIPT_LANGUAGE_ID );
            for ( var entry : bindings.entrySet() ) {
                contextBindings.putMember ( entry.getKey(), entry.getValue() );
            }
            // TODO check exception
            context.eval ( JAVASCRIPT_LANGUAGE_ID, script );
            // Value value = context.eval ( JAVASCRIPT_LANGUAGE_ID, script );
            // System.out.println ( value );
        }
    }

/*
    public static void executeJavascriptWithContext (
        @NotNull String script,
        @NotNull ScriptingContext JSContext,
        boolean allowAllAccess ) {

        executeJavascriptWithBindings ( script, Map.of ( "context", JSContext ), allowAllAccess );
    }

    public static void executeJavascriptWithWriter (
        @NotNull String script,
        @NotNull Writer writer,
        boolean allowAllAccess ) {

        executeJavascriptWithBindings ( script, Map.of ( "writer", writer ), allowAllAccess );
    }
*/
}
