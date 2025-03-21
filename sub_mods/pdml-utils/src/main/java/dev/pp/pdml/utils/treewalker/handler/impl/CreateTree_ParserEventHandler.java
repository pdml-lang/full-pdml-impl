package dev.pp.pdml.utils.treewalker.handler.impl;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.node.leaf.CommentLeaf;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.pdml.utils.treewalker.handler.*;

public class CreateTree_ParserEventHandler implements PdmlTreeWalkerEventHandler<TaggedNode, TaggedNode> {

    private TaggedNode rootNode;


    public CreateTree_ParserEventHandler() {}


    @Override
    public @NotNull TaggedNode onRootNodeStart ( @NotNull TaggedNodeStartEvent event ) {

        rootNode = new TaggedNode (
            event.tag (), null, event.attributes(), event.declaredNamespaces(), null, event.tag ().qualifiedTagToken ().getLocation(), null );
        return rootNode;
    }

    @Override
    public void onRootNodeEnd ( @NotNull TaggedNodeEndEvent event, @NotNull TaggedNode rootNode ) {
        rootNode.setEndLocation ( event.location() );
    }

    @Override
    public @NotNull TaggedNode onTaggedNodeStart ( @NotNull TaggedNodeStartEvent event, @NotNull TaggedNode parentNode ) {

        TaggedNode node = new TaggedNode ( event.tag (), null, event.attributes(), event.declaredNamespaces(), null, event.tag ().qualifiedTagToken ().getLocation(), null );
        parentNode.appendChild ( node );
        return node;
    }

    @Override
    public void onTaggedNodeEnd ( @NotNull TaggedNodeEndEvent event, @NotNull TaggedNode taggedNode ) {
        taggedNode.setEndLocation ( event.location() );
    }

    @Override
    public void onText ( @NotNull TextEvent event, @NotNull TaggedNode parentNode ) {
        parentNode.appendChild ( new TextLeaf ( event.text(), event.location() ) );
    }

    @Override
    public void onComment ( @NotNull CommentEvent event, @NotNull TaggedNode parentNode ) {
        parentNode.appendChild ( new CommentLeaf ( event.comment(), event.location() ) );
    }

    @Override
    public @NotNull TaggedNode getResult() { return rootNode; }
}
