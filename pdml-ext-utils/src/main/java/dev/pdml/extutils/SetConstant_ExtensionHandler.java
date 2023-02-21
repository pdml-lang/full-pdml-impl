package dev.pdml.extutils;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.text.inspection.TextErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SetConstant_ExtensionHandler implements PdmlExtensionNodeHandler {


    public static final @NotNull NodeNamespace NAMESPACE = UtilityConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "set";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final SetConstant_ExtensionHandler INSTANCE = new SetConstant_ExtensionHandler();


    private SetConstant_ExtensionHandler(){}


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PdmlReader reader = context.getPdmlReader();
        reader.skipWhitespaceAndComments();

        MutableNodeAttributes attributes = PdmlParserHelper.parseAttributesWithOptionalParenthesis (
            reader,
            false,true, true, true,
            context.getErrorHandler() );

        if ( attributes == null || attributes.isEmpty() ) {
            PdmlParserHelper.nonAbortingErrorAtCurrentLocation (
                "Expecting one or more constant declarations.",
                "MISSING_CONSTANT_DECLARATION",
                reader,
                context.getErrorHandler() );
            return;
        }

        reader.skipWhitespaceAndComments();
        PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

        Map<String, String> declaredConstants = context.getDeclaredConstants();

        List<Parameter<String>> list = attributes.list();
        assert list != null;

        for ( Parameter<String> attribute : list ) {

            String name = attribute.getName();

            if ( declaredConstants.containsKey ( name ) ) {
                PdmlParserHelper.nonAbortingError (
                    "Constant '" + name + "' has already been defined.",
                    "CONSTANT_DEFINED_TWICE",
                    attribute.nameToken(),
                    context.getErrorHandler()  );
                continue;
            }

            declaredConstants.put ( name, attribute.getValue() );
        }
    }
}
