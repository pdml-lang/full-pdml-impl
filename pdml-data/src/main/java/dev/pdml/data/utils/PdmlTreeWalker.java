package dev.pdml.data.utils;

import dev.pdml.data.node.MutablePdmlNode;
import dev.pdml.data.node.branch.MutableBranchNode;
import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pdml.data.node.leaf.MutableCommentNode;
import dev.pdml.data.node.leaf.MutableTextNode;
import dev.pp.basics.annotations.NotNull;

import java.util.function.Consumer;

public class PdmlTreeWalker {

    /**
     * Traverse a PDML tree in depth-first manner, and call a consumer on each node
     * @param tree the tree (root node) to be traversed
     * @param includeRootNode <code>true</code> if the root node is consumed too, <code>false</code> otherwise
     * @param nodeConsumer the consumer to be called for each node
     */
    public static void forEachNodeInTree (
        @NotNull MutableRootOrBranchNode tree,
        boolean includeRootNode,
        @NotNull Consumer<MutablePdmlNode> nodeConsumer ) {

        if ( includeRootNode ) nodeConsumer.accept ( tree );

        tree.forEachChildNode ( child -> {

            /* Use this code when this is no more a preview feature in Java
            switch ( child ) {
                case MutableBranchNode bn -> forEachNodeInTree ( bn, true, nodeConsumer );
                case MutableTextNode tn -> nodeConsumer.accept ( tn );
                case MutableCommentNode cn -> nodeConsumer.accept ( cn );
                default -> throw new IllegalStateException ( "Unexpected value: " + child );
            }
             */

            if ( child instanceof MutableBranchNode branchNode ) {
                forEachNodeInTree ( branchNode, true, nodeConsumer );
            } else if ( child instanceof MutableTextNode textNode ) {
                nodeConsumer.accept ( textNode );
            } else if ( child instanceof MutableCommentNode commentNode ) {
                nodeConsumer.accept ( commentNode );
            } else {
                throw new IllegalStateException ( "Unexpected value: " + child );
            }
        } );
    }

    /**
     * Traverse a PDML tree in depth-first manner, and call a consumer on each text node
     * @param tree the tree to be traversed
     * @param textConsumer the consumer to be called for each text
     */
    public static void forEachTextInTree (
        @NotNull MutableRootOrBranchNode tree,
        @NotNull Consumer<String> textConsumer ) {

        forEachNodeInTree ( tree, false, node -> {
            if ( node instanceof MutableTextNode tn ) {
                textConsumer.accept ( tn.getText() );
            }
        });
    }
}
