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

public class Definitions_ExtensionHandler implements PdmlExtensionNodeHandler {


    public static final @NotNull NodeNamespace NAMESPACE = ScriptingConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "def";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final Definitions_ExtensionHandler INSTANCE = new Definitions_ExtensionHandler();


    private Definitions_ExtensionHandler(){}


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public static final PdmlNodeSpec<String> NODE_SPEC = new PdmlNodeSpec<> (
        NODE_NAME,
        new PdmlTextBlockType (),
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

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PdmlReader reader = context.getPdmlReader();

        @Nullable String definitions = NODE_SPEC.getType().readPDMLObject ( reader, nodeName );
        if ( definitions != null ) definitions = definitions.trim();
        if ( definitions == null || definitions.isEmpty () ) {
            context.handleNonAbortingError (
                "A definition node cannot be empty.",
                "EMPTY_DEFINITION",
                nodeName.localNameToken() );
            PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
            return;
        }

        PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

//        Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );

        try {
            ScriptingEnvironment scriptingEnvironment =
                Expression_ExtensionHandler.requireScriptingEnvironment ( context, nodeName.localNameToken() );
//            scriptingEnvironment.addDefinitions (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, definitions, bindings, true );
            scriptingEnvironment.addDefinitions ( definitions );
        // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
        } catch ( Exception e ) {
            context.handleCancelingError (
                "Invalid code. Reason:" + StringConstants.OS_NEW_LINE + e.getMessage(),
                "INVALID_DEFINITION",
                nodeName.localNameToken() );
        }
    }
}
