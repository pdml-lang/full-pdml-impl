package dev.pdml.ext.extensions.scripting;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.FormalPDMLNode;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.types.PDMLTextBlockType;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.text.error.TextErrorException;

import java.io.IOException;

public class Definitions_ExtensionHandler implements PDMLExtensionNodeHandler {

    public static final @NotNull String NODE_NAME = "def";


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.SCRIPT_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "def";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final Definitions_ExtensionHandler INSTANCE = new Definitions_ExtensionHandler();


    private Definitions_ExtensionHandler(){}


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }


    public static final FormalPDMLNode<String> FORMAL_NODE = new FormalPDMLNode<> (
        QUALIFIED_NODE_NAME,
        new PDMLTextBlockType (),
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

    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PDMLReader reader = context.getPDMLReader ();

        @Nullable String definitions = FORMAL_NODE.getType().readPDMLObject ( reader, nodeName );
        if ( definitions != null ) definitions = definitions.trim();
        if ( definitions == null || definitions.isEmpty () ) {
            context.handleNonAbortingError (
                "EMPTY_DEFINITION",
                "A definition node cannot be empty.",
                nodeName.getToken() );
            PDMLParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
            return;
        }

        PDMLParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

//        Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );

        try {
            ScriptingEnvironment scriptingEnvironment =
                Expression_ExtensionHandler.requireScriptingEnvironment ( context, nodeName.getToken() );
//            scriptingEnvironment.addDefinitions (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, definitions, bindings, true );
            scriptingEnvironment.addDefinitions ( definitions );
        // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
        } catch ( Exception e ) {
            context.handleCancelingError (
                "INVALID_DEFINITION",
                "Invalid code. Reason:" + StringConstants.OS_NEW_LINE + e.getMessage(),
                nodeName.getToken() );
        }
    }
}
