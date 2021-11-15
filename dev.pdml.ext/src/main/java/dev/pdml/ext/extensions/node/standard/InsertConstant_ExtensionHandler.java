package dev.pdml.ext.extensions.node.standard;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.ParserHelper;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pp.text.annotations.NotNull;
import dev.pdml.ext.extensions.node.PXMLExtensionHandler;
import dev.pp.text.token.TextToken;

public class InsertConstant_ExtensionHandler implements PXMLExtensionHandler {

    public static final @NotNull String NODE_NAME = "get";

    public void handleNode ( @NotNull ExtensionsContext context, @NotNull ASTNodeName nodeName ) throws TextReaderException {

        // now positioned right after the node name

        PXMLReader reader = context.getPXMLReader();
        TextErrorHandler errorHandler = context.getErrorHandler();

        // reader.skipOptionalSpacesAndTabsAndNewLines ();
        reader.skipWhitespaceAndComments();

        TextToken text = reader.readAttributeValueToken();
        if ( text == null ) {
            throw ParserHelper.cancelingErrorAtCurrentLocation (
                "EXPECTING_CONSTANT_NAME",
                "Expecting the name of a previously declared constant.",
                reader,
                errorHandler );
        }

        String name = text.getText();
        String value = context.getDeclaredConstants().get ( name );
        if ( value == null ) {
            throw ParserHelper.cancelingError (
                "INVALID_CONSTANT_NAME",
                "Constant '" + name + "' has never been declared.",
                text,
                errorHandler );
        }

        // reader.skipOptionalSpacesAndTabsAndNewLines ();
        reader.skipWhitespaceAndComments();

        ParserHelper.requireNodeEnd ( reader, nodeName, errorHandler );
        // ParserHelper.requireIsAtNodeEnd ( reader );
        // assert reader.skipChar ( Constants.NODE_END );

        reader.insertStringToRead ( value, text );
    }
}
