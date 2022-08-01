package dev.pdml.ext.extensions.types.handlers;

import dev.pdml.core.data.formalNode.PDMLType;
import dev.pp.basics.annotations.NotNull;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.error.TextErrorException;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pp.text.token.TextToken;

import java.io.IOException;

public class TypeExtensionHandlerHelper {

    public static <T> void handleTypeNode (
        @NotNull PDMLExtensionsContext context,
        @NotNull PDMLType<T> type,
        @NotNull ASTNodeName nodeName ) throws IOException, TextErrorException, DataValidatorException {

        PDMLReader reader = context.getPDMLReader ();

        TextToken objectToken = reader.currentToken();
        T object = type.readPDMLObject ( reader, nodeName );
        PDMLParserHelper.requireNodeEnd ( reader, nodeName, context.getErrorHandler() );
        type.validate ( object, objectToken );
        type.insertPDMLObject ( object, reader, objectToken );
    }
}
