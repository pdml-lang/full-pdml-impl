package dev.pdml.exttypes.handlers;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.parser.nodespec.PdmlType;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class TypeExtensionHandlerHelper {

    public static <T> void handleTypeNode (
        @NotNull PdmlExtensionsContext context,
        @NotNull PdmlType<T> type,
        @NotNull NodeName nodeName ) throws IOException, TextErrorException {

        PdmlReader reader = context.getPdmlReader();

        TextToken objectToken = reader.currentToken();
        T object = type.readPDMLObject ( reader, nodeName );
        PdmlParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
        type.validate ( object, objectToken );
        type.insertPDMLObject ( object, reader, objectToken );
    }
}
