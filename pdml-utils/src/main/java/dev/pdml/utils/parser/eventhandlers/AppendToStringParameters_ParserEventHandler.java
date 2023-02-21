package dev.pdml.utils.parser.eventhandlers;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.parser.eventhandler.*;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.parameters.parameter.StringParameterUtils;
import dev.pp.parameters.parameters.MutableParameters;
import dev.pp.text.token.TextToken;

public class AppendToStringParameters_ParserEventHandler
    implements PdmlParserEventHandler<NodeStartEvent, MutableParameters<String>> {


    private final @NotNull MutableParameters<String> stringParameters;
    private @Nullable NodeStartEvent rootNode;
    private @Nullable TextToken currentNameToken;


    public AppendToStringParameters_ParserEventHandler (
        @NotNull MutableParameters<String> stringParameters ) {

        this.stringParameters = stringParameters;
        this.rootNode = null;
        this.currentNameToken = null;
    }

    public void onStart() {}

    public void onEnd() {}

    public @NotNull NodeStartEvent onRootNodeStart ( @NotNull NodeStartEvent startEvent )
        throws DataValidatorException {

        rootNode = startEvent;
        addAttributes ( startEvent.attributes(), startEvent );
        return rootNode;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {}

    public @NotNull NodeStartEvent onNodeStart (
        @NotNull NodeStartEvent startEvent,
        @NotNull NodeStartEvent parentStartEvent ) throws DataValidatorException {

        if ( parentStartEvent != rootNode ) {
            throw new DataValidatorException (
                "Node '" + parentStartEvent.nameToken().getText() + "' cannot have child nodes.",
                "CHILD_NODE_NOT_ALLOWED",
                startEvent.nameToken(), null );
        }

        @NotNull String name = startEvent.name().qualifiedName();
        if ( stringParameters.containsName ( name ) ) {
            throw new DataValidatorException (
                "Node '" + name + "' has already been defined.",
                "KEY_EXISTS_ALREADY",
                startEvent.nameToken(), null );
        }

        currentNameToken = startEvent.nameToken();

        addAttributes ( startEvent.attributes(), parentStartEvent );

        return startEvent;
    }

    public void addAttributes (
        @Nullable MutableNodeAttributes attributes,
        @NotNull NodeStartEvent nodeStartEvent ) throws DataValidatorException {

        if ( attributes == null ) return;

        // Allow attributes only in root node

        if ( nodeStartEvent != rootNode ) {
            throw new DataValidatorException (
                "Node '" + nodeStartEvent.nameToken().getText() + "' cannot have attributes.",
                "ATTRIBUTES_NOT_ALLOWED",
                attributes.getStartToken(), null );
        }

        // attributes.forEach ( stringParameters::add );
        attributes.forEach ( ( Parameter<String> attribute ) ->
            stringParameters.add ( StringParameterUtils.emptyValueToNull ( attribute ) ) );
    }

    public void onNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {

        if ( currentNameToken != null ) {
            // It's an empty node (no text defined)
            stringParameters.add (
                currentNameToken.getText(), null, null, currentNameToken.getLocation(), null );
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

        assert currentNameToken != null;
        stringParameters.add (
            currentNameToken.getText(), text,
            null,
            currentNameToken.getLocation(), event.location() );

        currentNameToken = null;
    }

    public void onComment ( @NotNull NodeCommentEvent comment, @NotNull NodeStartEvent start ) {}

    public @Nullable MutableParameters<String> getResult() {
        return stringParameters.isEmpty() ? null : stringParameters;
    }
}
