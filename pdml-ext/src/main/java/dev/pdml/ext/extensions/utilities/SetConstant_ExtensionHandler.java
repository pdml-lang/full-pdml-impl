package dev.pdml.ext.extensions.utilities;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pp.text.error.TextErrorException;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;

import java.io.IOException;
import java.util.Map;

public class SetConstant_ExtensionHandler implements PDMLExtensionNodeHandler {


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.UTILITY_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "set";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final SetConstant_ExtensionHandler INSTANCE = new SetConstant_ExtensionHandler();


    private SetConstant_ExtensionHandler(){}


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }

    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PDMLReader reader = context.getPDMLReader ();
        reader.skipWhitespaceAndComments();

        ASTNodeAttributes attributes = PDMLParserHelper.parseAttributesWithOptionalParenthesis (
            reader, null,null, false,true, context.getErrorHandler() );

        if ( attributes == null || attributes.isEmpty() ) {
            PDMLParserHelper.nonAbortingErrorAtCurrentLocation (
                "MISSING_CONSTANT_DECLARATION",
                "Expecting one or more constant declarations.",
                reader,
                context.getErrorHandler() );
            return;
        }

        reader.skipWhitespaceAndComments();
        PDMLParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

        Map<String, String> declaredConstants = context.getDeclaredConstants();
        // map.forEach ( (name, value) -> {
        for ( ASTNodeAttribute attribute : attributes.getList() ) {

// TODO namespace not allowed

            String name = attribute.getLocalNameText();

            if ( declaredConstants.containsKey ( name ) ) {
// TODO                ParserHelper.nonCancelingErrorAtLocation (  );
                PDMLParserHelper.nonAbortingErrorAtCurrentLocation (
                    "CONSTANT_DEFINED_TWICE",
                    "Constant '" + name + "' has already been defined.",
                    reader,
                    context.getErrorHandler()  );
                continue;
            }
/*
System.out.println ( "name: " + name );
System.out.println ( "value: " + entry.getValue() );
*/

            declaredConstants.put ( name, attribute.getValueText() );
        };
    }
}
