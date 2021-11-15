package dev.pdml.ext.extensions.node.standard;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.ParserHelper;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pp.text.annotations.NotNull;
import dev.pdml.ext.extensions.node.PXMLExtensionHandler;

import java.util.Map;

public class ConstantDeclaration_ExtensionHandler implements PXMLExtensionHandler {

    public static final @NotNull String NODE_NAME = "set";

    public void handleNode ( @NotNull ExtensionsContext context, @NotNull ASTNodeName nodeName ) throws TextReaderException {

        // now positioned right after the node name

        PXMLReader reader = context.getPXMLReader();
        reader.skipWhitespaceAndComments();

        ASTNodeAttributes attributes = ParserHelper.parseAttributesWithOptionalParenthesis (
            reader, null,null, false,true, context.getErrorHandler() );

        if ( attributes == null || attributes.isEmpty() ) {
            ParserHelper.nonCancelingErrorAtCurrentLocation (
                "MISSING_CONSTANT_DECLARATION",
                "Expecting one or more constant declarations.",
                reader,
                context.getErrorHandler() );
            return;
        }

        reader.skipWhitespaceAndComments();
        ParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );

        Map<String, String> declaredConstants = context.getDeclaredConstants();
        // map.forEach ( (name, value) -> {
        for ( ASTNodeAttribute attribute : attributes.getList() ) {

// TODO namespace not allowed

            String name = attribute.getLocalNameText();

            if ( declaredConstants.containsKey ( name ) ) {
// TODO                ParserHelper.nonCancelingErrorAtLocation (  );
                ParserHelper.nonCancelingErrorAtCurrentLocation (
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
