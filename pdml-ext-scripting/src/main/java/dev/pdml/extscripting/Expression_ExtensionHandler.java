package dev.pdml.extscripting;

import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.exttypes.PdmlTextBlockType;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.parser.nodespec.PdmlNodeSpec;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pdml.shared.exception.PdmlDocumentSemanticException;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.basics.utilities.string.StringTruncator;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class Expression_ExtensionHandler implements PdmlExtensionNodeHandler {


    public static final @NotNull NodeNamespace NAMESPACE = ScriptingConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "exp";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final Expression_ExtensionHandler INSTANCE = new Expression_ExtensionHandler();


    private Expression_ExtensionHandler(){}


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public static final PdmlNodeSpec<String> NODE_SPEC = new PdmlNodeSpec<> (
        NODE_NAME,
        new PdmlTextBlockType (),
        null,
        () -> new SimpleDocumentation (
            "Evaluate Expression",
            "Evaluate an expression, convert it to a string, and insert the result at the current position.",
            "[!exp 60 * 60]" ) );

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PdmlReader reader = context.getPdmlReader();
        @Nullable String expression = NODE_SPEC.getType().readPDMLObject ( reader, nodeName );
        if ( expression != null ) expression = expression.trim();
        if ( expression == null || expression.isEmpty () ) {
            context.handleNonAbortingError (
                "An expression cannot be empty.",
                "EMPTY_EXPRESSION",
                nodeName.localNameToken() );
            PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
            return;
        }

        PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

//        Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );

        @Nullable String result = null;
        try {
            ScriptingEnvironment scriptingEnvironment = requireScriptingEnvironment ( context, nodeName.localNameToken() );
//            result = scriptingEnvironment.evaluateExpressionToString (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, expression, bindings, true );
            result = scriptingEnvironment.evaluateExpressionToString ( expression );
        } catch ( Exception e ) {
        // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
            // https://www.graalvm.org/sdk/javadoc/org/graalvm/polyglot/Context.html#parse-org.graalvm.polyglot.Source-
            context.handleCancelingError (
                "Expression '" + StringTruncator.truncateWithEllipses ( expression ) +
                    "' is invalid. Reason:" + StringConstants.OS_NEW_LINE + e.getMessage(),
                "INVALID_EXPRESSION",
                nodeName.localNameToken() );
        }
        if ( result != null && ! result.isEmpty() ) {
            reader.insertStringToRead ( result );
        }
    }

    public static @NotNull ScriptingEnvironment requireScriptingEnvironment (
        @NotNull PdmlExtensionsContext context, @Nullable TextToken errorToken ) throws PdmlDocumentSemanticException {

        ScriptingEnvironment scriptingEnvironment = context.getScriptingEnvironment();
        if ( scriptingEnvironment == null ) throw new PdmlDocumentSemanticException (
            "Scripting is not supported in this context (scriptingEnvironment == null)",
            "SCRIPTING_NOT_SUPPORTED",
            errorToken );

        return scriptingEnvironment;
    }
}
