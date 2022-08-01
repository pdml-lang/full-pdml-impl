package dev.pdml.ext.extensions.scripting;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.FormalPDMLNode;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.core.exception.PDMLDocumentSemanticException;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.types.PDMLTextBlockType;
import dev.pdml.core.exception.PDMLDocumentException;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.token.TextToken;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.basics.utilities.string.StringTruncator;

import java.io.IOException;

public class Expression_ExtensionHandler implements PDMLExtensionNodeHandler {


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.SCRIPT_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "exp";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final Expression_ExtensionHandler INSTANCE = new Expression_ExtensionHandler();


    private Expression_ExtensionHandler(){}


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }

    public static final FormalPDMLNode<String> FORMAL_NODE = new FormalPDMLNode<> (
        QUALIFIED_NODE_NAME,
        new PDMLTextBlockType (),
        null,
        () -> new SimpleDocumentation (
            "Evaluate Expression",
            "Evaluate an expression, convert it to a string, and insert the result at the current position.",
            "[!exp 60 * 60]" ) );

    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PDMLReader reader = context.getPDMLReader ();
        @Nullable String expression = FORMAL_NODE.getType().readPDMLObject ( reader, nodeName );
        if ( expression != null ) expression = expression.trim();
        if ( expression == null || expression.isEmpty () ) {
            context.handleNonAbortingError (
                "EMPTY_EXPRESSION",
                "An expression cannot be empty.",
                nodeName.getToken() );
            PDMLParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
            return;
        }

        PDMLParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

//        Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );

        @Nullable String result = null;
        try {
            ScriptingEnvironment scriptingEnvironment = requireScriptingEnvironment ( context, nodeName.getToken() );
//            result = scriptingEnvironment.evaluateExpressionToString (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, expression, bindings, true );
            result = scriptingEnvironment.evaluateExpressionToString ( expression );
        } catch ( Exception e ) {
        // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
            // https://www.graalvm.org/sdk/javadoc/org/graalvm/polyglot/Context.html#parse-org.graalvm.polyglot.Source-
            context.handleCancelingError (
                "INVALID_EXPRESSION",
                "Expression '" + StringTruncator.truncateWithEllipses ( expression ) +
                    "' is invalid. Reason:" + StringConstants.OS_NEW_LINE + e.getMessage(),
                nodeName.getToken() );
        }
        if ( result != null && ! result.isEmpty() ) {
            reader.insertStringToRead ( result );
        }
    }

    public static @NotNull ScriptingEnvironment requireScriptingEnvironment (
        @NotNull PDMLExtensionsContext context, @Nullable TextToken errorToken ) throws PDMLDocumentException {

        ScriptingEnvironment scriptingEnvironment = context.getScriptingEnvironment();
        if ( scriptingEnvironment == null ) throw new PDMLDocumentSemanticException (
            "SCRIPTING_NOT_SUPPORTED",
            "Scripting is not supported in this context (scriptingEnvironment == null)",
            errorToken );

        return scriptingEnvironment;
    }
}
