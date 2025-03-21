package dev.pp.pdml.data.util;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.leaf.TextLeaf;
import dev.pp.core.basics.annotations.NotNull;

public class TextNode {

    private final @NotNull NodeTag name;
    public @NotNull NodeTag getName() { return name; }

    private final @NotNull TextLeaf textLeaf;
    public @NotNull TextLeaf getTextLeaf() { return textLeaf; }


    public TextNode (
        @NotNull NodeTag name,
        @NotNull TextLeaf textLeaf ) {

        this.name = name;
        this.textLeaf = textLeaf;
    }

    public TextNode (
        @NotNull String qualifiedName,
        @NotNull String text ) {

        this ( NodeTag.create ( qualifiedName ), new TextLeaf ( text, null ) );
    }


    public @NotNull String getText() { return textLeaf.getText(); }

    /*
    public @NotNull BranchNode toBranchNode() {

        BranchNode branchNode = new BranchNode ( tag );
        branchNode.appendChild ( textLeaf );
        return branchNode;
    }
     */


    // TODO? add asXXX methods from textLeaf (e.g. int asInt())
}
