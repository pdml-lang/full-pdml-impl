package dev.pp.pdml.data.validation;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.core.text.token.TextToken;
import dev.pp.core.texttable.writer.pretty.utilities.TextInspectionMessage_FormWriter;

public class TaggedNodeValidatorContext {


    public static @NotNull TextInspectionMessageHandler createDefaultMessageHandler() {
        return TextInspectionMessage_FormWriter.createLogMessageHandler();
    }


    private final @NotNull TextInspectionMessageHandler messageHandler;
    public @NotNull TextInspectionMessageHandler getMessageHandler() { return messageHandler; }


    public TaggedNodeValidatorContext ( @NotNull TextInspectionMessageHandler messageHandler ) {
        this.messageHandler = messageHandler;
    }

    public TaggedNodeValidatorContext () {
        this ( createDefaultMessageHandler() );
    }


    public boolean checkHasChildNodes ( @NotNull TaggedNode taggedNode ) {

        boolean success = taggedNode.hasChildNodes();
        if ( ! success ) {
            errorDetected (
                taggedNode,
                "Node '" + taggedNode.qualifiedTag () + "' must have child nodes.",
                "CHILD_NODES_REQUIRED" );

        }
        return success;
    }

    public boolean checkParentName ( @NotNull TaggedNode taggedNode, @NotNull NodeTag parentNodeName ) {

        @Nullable TaggedNode parentNode = taggedNode.getParent();
        if ( parentNode == null ) {
            errorDetected (
                taggedNode,
                "Node '" + taggedNode.qualifiedTag () + "' must have a parent node.",
                "PARENT_NODE_REQUIRED" );
            return false;
        }

        boolean success = parentNode.getTag ().equals ( parentNodeName );
        if ( ! success ) {
            errorDetected (
                taggedNode,
                "Invalid parent node '" + parentNode.qualifiedTag () + "'. Node '"
                    + taggedNode.getTag ().qualifiedTag () + "' must be a child node of '" + parentNodeName.qualifiedTag () + "'.",
                "INVALID_PARENT_NODE" );

        }
        return success;
    }

    public void errorDetected (
        @NotNull String message, @NotNull String id, @Nullable TextToken token ) {

        messageHandler.handleError ( message, id, token );
    }

    public void errorDetected (
        @NotNull TaggedNode taggedNode, @NotNull String message, @NotNull String id ) {

        errorDetected ( message, id, taggedNode.qualifiedTagToken () );
    }
}
