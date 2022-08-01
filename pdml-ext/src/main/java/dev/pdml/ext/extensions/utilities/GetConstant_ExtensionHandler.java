package dev.pdml.ext.extensions.utilities;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.TextErrorException;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class GetConstant_ExtensionHandler implements PDMLExtensionNodeHandler {


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.UTILITY_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "get";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final GetConstant_ExtensionHandler INSTANCE = new GetConstant_ExtensionHandler();


    private GetConstant_ExtensionHandler(){}


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }

    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PDMLReader reader = context.getPDMLReader ();
        TextErrorHandler errorHandler = context.getErrorHandler();

        // reader.skipOptionalSpacesAndTabsAndNewLines ();
        reader.skipWhitespaceAndComments();

        TextToken text = reader.readAttributeValueToken();
        if ( text == null ) {
            throw PDMLParserHelper.abortingSyntaxErrorAtCurrentLocation (
                "EXPECTING_CONSTANT_NAME",
                "Expecting the name of a previously declared constant.",
                reader, context.getErrorHandler() );
        }

        String name = text.getText();
        String value = context.getDeclaredConstants().get ( name );
        if ( value == null ) {
            throw PDMLParserHelper.abortingSyntaxError (
                "INVALID_CONSTANT_NAME",
                "Constant '" + name + "' has never been declared.",
                text, context.getErrorHandler() );
        }

        // reader.skipOptionalSpacesAndTabsAndNewLines ();
        reader.skipWhitespaceAndComments();

        PDMLParserHelper.requireNodeEnd ( reader, nodeName, errorHandler );
        // ParserHelper.requireIsAtNodeEnd ( reader );
        // assert reader.skipChar ( Constants.NODE_END );

        reader.insertStringToRead ( value );
    }
}
