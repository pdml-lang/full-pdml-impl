package dev.pdml.ext.utilities.parser.eventhandlers;

import dev.pdml.core.data.AST.PDMLNodeAST;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class CreateAST_ParserEventHandler implements PDMLParserEventHandler<PDMLNodeAST, PDMLNodeAST> {

    private PDMLNodeAST rootNode;


    public CreateAST_ParserEventHandler() {}


    public void onStart() {}

    public void onEnd () {}

    public @NotNull PDMLNodeAST onRootNodeStart ( @NotNull NodeStartEvent event ) {

        // TODO declared namespaces

        rootNode = newNode ( event.getName() );
        return rootNode;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull PDMLNodeAST rootNode ) {}

    public @NotNull PDMLNodeAST onNodeStart ( @NotNull NodeStartEvent event, @NotNull PDMLNodeAST parentNode ) {

        // TODO declared namespaces

        PDMLNodeAST node = newNode ( event.getName() );
        parentNode.appendNodeChild ( node );
        return node;
    }

    private @NotNull PDMLNodeAST newNode ( @NotNull ASTNodeName name ) {

        return new PDMLNodeAST ( name, null, null, null );
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull PDMLNodeAST node ) {}

    public void onAttributes ( @Nullable ASTNodeAttributes attributes, @NotNull PDMLNodeAST parentNode ) {

        parentNode.setAttributes ( attributes );
    }

    public void onText ( @NotNull TextToken text, @NotNull PDMLNodeAST parentNode ) {

        parentNode.appendTextChild ( text );
    }

    public void onComment ( @NotNull TextToken comment, @NotNull PDMLNodeAST parentNode ) {

        parentNode.appendCommentChild ( comment );
    }

    public @NotNull PDMLNodeAST getResult() { return rootNode; }
}
