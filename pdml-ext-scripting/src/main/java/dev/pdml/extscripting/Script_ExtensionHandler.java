package dev.pdml.extscripting;

import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.exttypes.PdmlTextBlockType;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.parser.nodespec.PdmlNodeSpec;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.inspection.TextErrorException;

import java.io.IOException;

public class Script_ExtensionHandler implements PdmlExtensionNodeHandler {


    public static final @NotNull NodeNamespace NAMESPACE = ScriptingConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "script";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final Script_ExtensionHandler INSTANCE = new Script_ExtensionHandler();


    private Script_ExtensionHandler(){}


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public static final PdmlNodeSpec<String> NODE_SPEC = new PdmlNodeSpec<> (
        NODE_NAME,
        new PdmlTextBlockType (),
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

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PdmlReader reader = context.getPdmlReader();

        @Nullable String script = NODE_SPEC.getType().readPDMLObject ( reader, nodeName );
        if ( script != null ) script = script.trim();
        if ( script == null || script.isEmpty () ) {
            context.handleNonAbortingError (
                "A script cannot be empty. It must contain one or more instructions.",
                "EMPTY_SCRIPT",
                nodeName.localNameToken() );
            PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
            return;
        }

        PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

//        Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );

        try {
            ScriptingEnvironment scriptingEnvironment =
                Expression_ExtensionHandler.requireScriptingEnvironment ( context, nodeName.localNameToken() );
//            scriptingEnvironment.executeScript (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, script, bindings, true );
            scriptingEnvironment.executeScript ( script );
        // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
        } catch ( Exception e ) {
            context.handleCancelingError (
                "Invalid script. Reason:" + StringConstants.OS_NEW_LINE + e.getMessage(),
                "INVALID_SCRIPT",
                nodeName.localNameToken() );
        }
    }
}
