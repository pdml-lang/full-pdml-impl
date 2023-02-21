package dev.pdml.extutils;

import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class GetConstant_ExtensionHandler implements PdmlExtensionNodeHandler {

    public static final @NotNull NodeNamespace NAMESPACE = UtilityConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "get";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final GetConstant_ExtensionHandler INSTANCE = new GetConstant_ExtensionHandler();


    private GetConstant_ExtensionHandler(){}


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PdmlReader reader = context.getPdmlReader();
        TextInspectionMessageHandler errorHandler = context.getErrorHandler();

        reader.skipWhitespaceAndComments();

        TextToken text = reader.readAttributeValueToken();
        if ( text == null ) {
            throw PdmlParserHelper.abortingSyntaxErrorAtCurrentLocation (
                "Expecting the name of a previously declared constant.",
                "EXPECTING_CONSTANT_NAME",
                reader, context.getErrorHandler() );
        }

        String name = text.getText();
        String value = context.getDeclaredConstants().get ( name );
        if ( value == null ) {
            throw PdmlParserHelper.abortingSyntaxError (
                "Constant '" + name + "' has never been declared.",
                "INVALID_CONSTANT_NAME",
                text, context.getErrorHandler() );
        }

        reader.skipWhitespaceAndComments();

        PdmlParserHelper.requireNodeEnd ( reader, nodeName, errorHandler );

        reader.insertStringToRead ( value );
    }
}
