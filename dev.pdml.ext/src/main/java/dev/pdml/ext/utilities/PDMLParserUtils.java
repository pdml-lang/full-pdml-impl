package dev.pdml.ext.utilities;

import dev.pdml.core.data.AST.ASTNode;
import dev.pdml.core.data.formalNode.FormalNodes;
import dev.pdml.core.reader.parser.EventStreamParserBuilder;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pdml.core.reader.parser.eventHandler.impls.CreateAST_ParserEventHandler;
import dev.pdml.ext.extensions.DefaultPXMLExtensionsHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;

import java.io.File;

public class PDMLParserUtils {

    public static @NotNull ASTNode parseFileToAST (
        @NotNull File file,
        @NotNull TextErrorHandler errorHandler,
        @Nullable FormalNodes formalNodes ) throws Exception {

        ParserEventHandler<ASTNode, ASTNode> eventHandler = new CreateAST_ParserEventHandler();

        new EventStreamParserBuilder<> ( eventHandler )
            .setErrorHandler ( errorHandler )
            .setExtensionsHandler ( new DefaultPXMLExtensionsHandler() )
            .setFormalNodes ( formalNodes )
            .parseFile ( file );

        return eventHandler.getResult();
    }
}
