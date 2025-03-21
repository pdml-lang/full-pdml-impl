package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.scriptingbase.env.ScriptingEnvironment;
import dev.pp.core.scriptingbase.env.ScriptingException;

import java.io.IOException;

public class DefinitionHandler implements ExtensionNodeHandler {


    public static final @NotNull String NAME = "def";
    public static final DefinitionHandler INSTANCE = new DefinitionHandler ();

/*
    public static final PdmlNodeSpec NODE_SPEC = new PdmlNodeSpec (
        NODE_NAME,
        // new PdmlTextBlockType(),
        TYPE.getName(),
        null,
        () -> new SimpleDocumentation (
            "Definitions",
            "A text block to define constants, variables, and functions that can be used in 'exp' (expression) and 'script' nodes",
            """
                    [!def
                        ~~~
                        const PI = 3.1415926;

                        function foo() {
                            return "foo";
                        }
                        ~~~
                    ]
                    """ ) );
 */


    private DefinitionHandler (){}


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
        @Nullable String definitions = ScriptingHandlerUtil.CODE_TYPE.parseObject ( context.getPdmlParser() ).object();
        context.requireExtensionNodeEnd ( nodeName );

        if ( definitions != null ) definitions = definitions.trim();
        if ( definitions == null || definitions.isEmpty () ) {
            throw context.error (
                "A definition node cannot be empty.",
                "EMPTY_DEFINITION",
                nodeName.tagToken () );
        }

        try {
            @NotNull ScriptingEnvironment scriptingEnvironment = context.requireScriptingEnvironment ( nodeName.qualifiedTagToken () );
            scriptingEnvironment.addDefinitions ( definitions );
        // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
        } catch ( ScriptingException e ) {
            scriptingError ( e, context, nodeName );
        }
        return null;
    }

    private void scriptingError (
        @NotNull ScriptingException scriptingException,
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws PdmlException {

        ScriptingHandlerUtil.scriptingError (
            "Invalid code. Reason:" + StringConstants.OS_LINE_BREAK + scriptingException.getMessage(),
            "INVALID_DEFINITION",
            nodeName,
            scriptingException,
            context );
    }
}
