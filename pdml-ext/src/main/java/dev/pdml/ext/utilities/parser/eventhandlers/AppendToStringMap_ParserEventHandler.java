package dev.pdml.ext.utilities.parser.eventhandlers;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.token.TextToken;

import java.util.HashMap;
import java.util.Map;

public class AppendToStringMap_ParserEventHandler implements PDMLParserEventHandler<NodeStartEvent, Map<String,String>> {


    private final @NotNull Map<String,String> map;
    private @Nullable NodeStartEvent rootNode;
    private @Nullable String currentKey;


    public AppendToStringMap_ParserEventHandler ( @NotNull Map<String,String> map ) {

        this.map = map;
        this.rootNode = null;
        this.currentKey = null;
    }

    public AppendToStringMap_ParserEventHandler () {

        this ( new HashMap<>() );
    }


    public void onStart() {}

    public void onEnd () {}

    public @NotNull NodeStartEvent onRootNodeStart ( @NotNull NodeStartEvent event ) {

        rootNode = event;
        return rootNode;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {}

    public @NotNull NodeStartEvent onNodeStart (
        @NotNull NodeStartEvent start,
        @NotNull NodeStartEvent parent ) throws DataValidatorException {

        if ( parent != rootNode ) {
            throw new DataValidatorException (
                "CHILD_NODE_NOT_ALLOWED",
                "Node '" + parent + "' cannot have child nodes.",
                start.getToken(), null );
        }

        @NotNull String key = start.getName().qualifiedName();
        if ( map.containsKey ( key ) ) {
            throw new DataValidatorException (
                "KEY_EXISTS_ALREADY",
                "Node '" + key + "' has already been defined.",
                start.getToken(), null );
        }

        currentKey = key;

        return start;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {

        if ( currentKey != null ) {
            // It's an empty node (no text defined)
            map.put ( currentKey, null );
        }
    }

    public void onAttributes (
        @Nullable ASTNodeAttributes attributes,
        @NotNull NodeStartEvent parent ) throws DataValidatorException {

        if ( attributes == null ) return;

        // Allow attributes only in root node

        if ( parent != rootNode ) {
            throw new DataValidatorException (
                "ATTRIBUTES_NOT_ALLOWED",
                "Node '" + parent + "' cannot have attributes.",
                attributes.getStartToken(), null );
        }

        for ( ASTNodeAttribute attribute : attributes.getList () ) {
            map.put ( attribute.getName().qualifiedName(), attribute.getValue().getText() );
        }
    }

    public void onText (
        @NotNull TextToken textToken,
        @NotNull NodeStartEvent parent ) throws DataValidatorException {

        String text = textToken.getText();

        if ( parent == rootNode ) {
            if ( text.trim().isEmpty() ) {
                return;
            } else {
                throw new DataValidatorException (
                    "TEXT_NOT_ALLOWED",
                    "Node '" + parent + "' cannot contain text.",
                    parent.getToken(), null );
            }
        }

        map.put ( currentKey, text );
        currentKey = null;
    }

    public void onComment ( @NotNull TextToken comment, @NotNull NodeStartEvent start ) {}

    public @Nullable Map<String,String> getResult() {

        return map.isEmpty() ? null : map;
    }
}
