package dev.pdml.core.reader.parser.eventHandler.impls;

import dev.pdml.core.data.AST.ASTNode;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.reader.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.token.TextToken;

public class CreateAST_ParserEventHandler implements ParserEventHandler<ASTNode, ASTNode> {

    private ASTNode rootNode;


    public CreateAST_ParserEventHandler() {}


    public void onStart() {}

    public void onEnd () {}

    public @NotNull ASTNode onRootNodeStart ( @NotNull NodeStartEvent event ) {

        // TODO declared namespaces

        rootNode = newNode ( event.getName() );
        return rootNode;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNode rootNode ) {}

    public @NotNull ASTNode onNodeStart ( @NotNull NodeStartEvent event, @NotNull ASTNode parentNode ) {

        // TODO declared namespaces

        ASTNode node = newNode ( event.getName() );
        parentNode.appendNodeChild ( node );
        return node;
    }

    private @NotNull ASTNode newNode ( @NotNull ASTNodeName name ) {

        return new ASTNode ( name, null, null, null );
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNode node ) {}

    public void onAttributes ( @NotNull ASTNodeAttributes attributes, @NotNull ASTNode parentNode ) {

        parentNode.setAttributes ( attributes );
    }

    public void onText ( @NotNull TextToken text, @NotNull ASTNode parentNode ) {

        parentNode.appendTextChild ( text );
    }

    public void onComment ( @NotNull TextToken comment, @NotNull ASTNode parentNode ) {

        parentNode.appendCommentChild ( comment );
    }

    public @NotNull ASTNode getResult() { return rootNode; }
}
