package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.scriptingbase.env.ScriptingEnvironment;
import dev.pp.core.scriptingbase.env.ScriptingException;

import java.io.IOException;

public class ScriptHandler implements ExtensionNodeHandler {


    public static final @NotNull String NAME = "script";
    public static final ScriptHandler INSTANCE = new ScriptHandler ();

/*
    public static final PdmlNodeSpec NODE_SPEC = new PdmlNodeSpec (
        NODE_NAME,
        // new PdmlTextBlockType(),
        TYPE.getName(),
        null,
        () -> new SimpleDocumentation (
            "Execute Script",
            "Execute a script (a set of instructions).",
            """
                    [!script
                        ~~~
                        if ( flag ) {
                            doc.write ( "yes" );
                        }
                        ~~~
                    ]
                    """ ) );
 */


    private ScriptHandler (){}


    @Override
    public @NotNull String getExtensionKind() {
        return ScriptingHandlerUtil.EXTENSION_KIND;
    }

    public @NotNull String getExtensionName() {
        return NAME;
    }

    @Override
    public @Nullable InsertStringExtensionResult handleNode ( @NotNull ExtensionNodeHandlerContext context, @NotNull NodeTag nodeName )
        throws IOException, PdmlException {

        // now positioned right after the node tag

        @Nullable String script = ScriptingHandlerUtil.CODE_TYPE.parseObject ( context.getPdmlParser() ).object();
        context.requireExtensionNodeEnd ( nodeName );

        if ( script != null ) script = script.trim();
        if ( script == null || script.isEmpty () ) {
            throw context.error (
                "A script cannot be empty. It must contain one or more instructions.",
                "EMPTY_SCRIPT",
                nodeName.tagToken () );
        }

        // executeJavaScriptCode ( script, context, nodeName );
        executeJavaCode ( script, context, nodeName );

        return null;
    }

    private void executeJavaCode (
        @NotNull String code,
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws PdmlException {

        try {
            ScriptingEnvironment scriptingEnvironment = context.requireScriptingEnvironment ( nodeName.qualifiedTagToken () );
            scriptingEnvironment.executeScript ( code );
        } catch ( ScriptingException e ) {
            scriptingError ( e, context, nodeName );
        }
    }

/*
    private void executeJavaScriptCode (
        @NotNull String code,
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeName nodeName ) throws PdmlException {

        try {
//          Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );
            // ScriptingEnvironment scriptingEnvironment =
            //    ExpressionHandler.requireScriptingEnvironment ( context, nodeName.localNameToken() );
            @NotNull ScriptingEnvironment scriptingEnvironment = context.requireScriptingEnvironment ( nodeName.token() );
//            scriptingEnvironment.executeScript (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, script, bindings, true );
            scriptingEnvironment.executeScript ( code );
            // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
        } catch ( ScriptingException e ) {
            scriptingError ( e, context, nodeName );
        }
    }
 */

    private void scriptingError (
        @NotNull ScriptingException exception,
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws PdmlException {

        ScriptingHandlerUtil.scriptingError (
            "Invalid script. Reason:" + StringConstants.OS_LINE_BREAK + exception.getMessage(),
            "INVALID_SCRIPT",
            nodeName,
            exception,
            context );
    }
}
