package dev.pdml.data.node.branch;

import dev.pdml.data.node.MutablePdmlNode;
import dev.pdml.data.node.root.MutableRootNode;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

public interface MutableChildNode extends MutablePdmlNode {

    @Nullable MutableRootOrBranchNode getParent();
    void setParent ( @Nullable MutableRootOrBranchNode parent );


    default @NotNull MutableRootNode root() {

        MutableRootOrBranchNode parent = getParent();
        assert parent != null;

        /* Use this code when this is no more a preview feature in Java
        return switch ( parent ) {
            case MutableRootNode r -> r;
            case MutableBranchNode b -> b.root();
            default -> throw new IllegalStateException ( "Unexpected value: " + parent );
        };
         */

        if ( parent instanceof MutableRootNode rootNode ) {
            return rootNode;
        } else if ( parent instanceof MutableBranchNode branchNode ) {
            return branchNode.root();
        } else {
            throw new IllegalStateException ( "Unexpected value: " + parent );
        }
    }
}
