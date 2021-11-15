package dev.pdml.ext.extensions.node.types;

import dev.pdml.core.data.formalNode.types.PDMLType;
import dev.pp.text.annotations.NotNull;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pdml.core.reader.parser.ParserHelper;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;
import dev.pp.text.token.TextToken;

public class TypeExtensionHandlerHelper {

    public static <T> void handleTypeNode (
        @NotNull ExtensionsContext context,
        @NotNull PDMLType<T> type,
        @NotNull ASTNodeName nodeName ) throws TextReaderException {

        PXMLReader reader = context.getPXMLReader();

        TextToken objectToken = reader.currentToken();
        T object = type.readPDMLObject ( reader, nodeName );
        ParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
        type.validate ( object, objectToken );
        type.insertPDMLObject ( object, reader, objectToken );
    }
}
