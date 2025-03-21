package dev.pp.pdml.utils.scripting;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pjse.util.interfaces.FailableConsumer;

public class TreeExplorerScriptExample implements FailableConsumer<TaggedNode> {

    public void accept ( TaggedNode rootNode ) throws Exception {

        System.out.println ( "Hi from explorer" );

        System.out.println ( "rootNode: " + rootNode.toString() );

        System.out.println ( "All tagged nodes" );
        rootNode.treeTaggedNodeStream ( true ).forEach ( node -> {
            System.out.println ( node.toString() );
        });

        System.out.println ( "concatenated text: " + rootNode.concatenateTreeTexts() );
    }
}
