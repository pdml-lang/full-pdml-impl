package dev.pp.pdml.ext.scripting;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.pdml.ext.InsertStringFormat;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.basics.utilities.string.StringTruncator;
import dev.pp.core.scriptingbase.env.ScriptingEnvironment;
import dev.pp.core.scriptingbase.env.ScriptingException;

import java.io.IOException;

public class InsertTextHandler implements ExtensionNodeHandler {

    public static final @NotNull String NAME = "itext";
    public static final InsertTextHandler INSTANCE = new InsertTextHandler();


    private InsertTextHandler() {}


    @Override
    public @NotNull String getExtensionKind() {
        return ScriptingHandlerUtil.EXTENSION_KIND;
    }

    public @NotNull String getExtensionName() {
        return NAME;
    }

    @Override
    public @Nullable InsertStringExtensionResult handleNode (
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws IOException, PdmlException {

        return handleNode ( context, nodeName, true );
    }

    protected static @Nullable InsertStringExtensionResult handleNode (
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName,
        boolean escapeResult )
        throws IOException, PdmlException {

        // now positioned right after the node tag

        @Nullable String expression = ScriptingHandlerUtil.CODE_TYPE.parseObject ( context.getPdmlParser() ).object();
        context.requireExtensionNodeEnd ( nodeName );

        if ( expression != null ) expression = expression.trim();
        if ( expression == null || expression.isEmpty () ) {
            throw context.error (
                "An expression cannot be empty.",
                "EMPTY_EXPRESSION",
                nodeName.tagToken () );
        }

        // @Nullable String result = evaluateJavaScriptExpression ( expression, context, nodeName );
        @Nullable String result = evaluateJavaExpression ( expression, context, nodeName );
        /*
        if ( result != null && ! result.isEmpty() ) {
            // if ( escapeResult ) {
            //    result = PdmlEscapeUtil.escapeNodeText ( result );
            // }
            // context.getPdmlReader().insertStringToRead ( result );
            if ( escapeResult ) {
                return result;
            } else {
                context.getPdmlReader ().insertStringToRead ( result );
                return null;
            }
        } else {
            return null;
        }
         */
        return new InsertStringExtensionResult (
            result, escapeResult ? InsertStringFormat.TEXT : InsertStringFormat.AS_IS );
    }

    private static @Nullable String evaluateJavaExpression (
        @NotNull String expression,
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeTag nodeName ) throws PdmlException {

        try {
            ScriptingEnvironment scriptingEnvironment = context.requireScriptingEnvironment ( nodeName.qualifiedTagToken () );
            return scriptingEnvironment.evaluateExpressionAsString ( expression );
        } catch ( ScriptingException e ) {
            // scriptingError ( e, expression, context, nodeName );
            ScriptingHandlerUtil.scriptingError (
                "Expression '" + StringTruncator.truncateWithEllipses ( expression ) +
                    "' is invalid. Reason:" + StringConstants.OS_LINE_BREAK + e.getMessage(),
                "INVALID_EXPRESSION",
                nodeName, e, context );
            return null;
        }
    }

/*
    private @Nullable String evaluateJavaScriptExpression (
        @NotNull String expression,
        @NotNull ExtensionNodeHandlerContext context,
        @NotNull NodeName nodeName ) throws PdmlException {

        try {
            // ScriptingEnvironment scriptingEnvironment = requireScriptingEnvironment ( context, nodeName.localNameToken() );
            @NotNull ScriptingEnvironment scriptingEnvironment = context.requireScriptingEnvironment ( nodeName.token() );
//            Map<String, Object> bindings = BindingsCreator.createMap ( reader, nodeName.getToken() );
//            return scriptingEnvironment.evaluateExpressionToString (
//                ScriptingConstants.JAVASCRIPT_LANGUAGE_ID, expression, bindings, true );
            return scriptingEnvironment.evaluateExpressionToString ( expression );

        // } catch ( Exception e ) {
        } catch ( ScriptingException e ) {
            // } catch ( PolyglotException e ) {
            // TODO See if a more precise error position can be reported, using methods in PolyglotException
            // https://www.graalvm.org/sdk/javadoc/org/graalvm/polyglot/Context.html#parse-org.graalvm.polyglot.Source-
            scriptingError ( e, expression, context, nodeName );
            return null;
        }
    }
 */
}
