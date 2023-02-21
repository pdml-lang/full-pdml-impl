package dev.pdml.utils.parser.eventhandlers;

import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pdml.data.node.branch.MutableBranchNode;
import dev.pdml.data.node.root.MutableRootNode;
import dev.pdml.parser.eventhandler.*;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

public class CreateTree_ParserEventHandler implements PdmlParserEventHandler<MutableRootOrBranchNode, MutableRootNode> {

    private MutableRootNode rootNode;


    public CreateTree_ParserEventHandler () {}


    public void onStart() {}

    public void onEnd () {}

    public @NotNull MutableRootNode onRootNodeStart ( @NotNull NodeStartEvent event ) {

        rootNode = new MutableRootNode ( event.name(), event.attributes(), event.declaredNamespaces() );
        return rootNode;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @Nullable MutableRootOrBranchNode rootNode ) {}

    public @NotNull MutableBranchNode onNodeStart (
        @NotNull NodeStartEvent event, @NotNull MutableRootOrBranchNode parentNode ) {

        MutableBranchNode node = new MutableBranchNode ( event.name(), event.attributes(), event.declaredNamespaces() );
        parentNode.appendChild ( node );
        return node;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @Nullable MutableRootOrBranchNode node ) {}

    public void onText ( @NotNull NodeTextEvent event, @NotNull MutableRootOrBranchNode parentNode ) {
        parentNode.appendText ( event.text() );
    }

    public void onComment ( @NotNull NodeCommentEvent event, @NotNull MutableRootOrBranchNode parentNode ) {
        parentNode.appendComment ( event.comment() );
    }

    public @NotNull MutableRootNode getResult() { return rootNode; }
}
