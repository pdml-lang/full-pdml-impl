package dev.pdml.utils.parser.eventhandlers;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.parser.eventhandler.*;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidatorException;

import java.util.HashMap;
import java.util.Map;

public class AppendToStringMap_ParserEventHandler implements PdmlParserEventHandler<NodeStartEvent, Map<String,String>> {


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
        @NotNull NodeStartEvent startEvent,
        @NotNull NodeStartEvent parent ) throws DataValidatorException {

        if ( parent != rootNode ) {
            throw new DataValidatorException (
                "Node '" + parent + "' cannot have child nodes.",
                "CHILD_NODE_NOT_ALLOWED",
                startEvent.nameToken(), null );
        }

        @NotNull String key = startEvent.name().qualifiedName();
        if ( map.containsKey ( key ) ) {
            throw new DataValidatorException (
                "Node '" + key + "' has already been defined.",
                "KEY_EXISTS_ALREADY",
                startEvent.nameToken(), null );
        }

        currentKey = key;

        addAttributes ( startEvent.attributes(), parent );

        return startEvent;
    }

    public void addAttributes (
        @Nullable MutableNodeAttributes attributes,
        @NotNull NodeStartEvent parent ) throws DataValidatorException {

        if ( attributes == null ) return;

        // Allow attributes only in root node

        if ( parent != rootNode ) {
            throw new DataValidatorException (
                "Node '" + parent + "' cannot have attributes.",
                "ATTRIBUTES_NOT_ALLOWED",
                attributes.getStartToken(), null );
        }

        attributes.forEach ( attribute -> map.put ( attribute.getName(), attribute.getValue() ) );
    }

    public void onNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {

        if ( currentKey != null ) {
            // It's an empty node (no text defined)
            map.put ( currentKey, null );
        }
    }

    public void onText (
        @NotNull NodeTextEvent event,
        @NotNull NodeStartEvent parent ) throws DataValidatorException {

        String text = event.text();

        if ( parent == rootNode ) {
            if ( text.trim().isEmpty() ) {
                return;
            } else {
                throw new DataValidatorException (
                    "Node '" + parent + "' cannot contain text.",
                    "TEXT_NOT_ALLOWED",
                    parent.nameToken(), null );
            }
        }

        map.put ( currentKey, text );
        currentKey = null;
    }

    public void onComment ( @NotNull NodeCommentEvent event, @NotNull NodeStartEvent start ) {}

    public @Nullable Map<String,String> getResult() {
        return map.isEmpty() ? null : map;
    }
}
