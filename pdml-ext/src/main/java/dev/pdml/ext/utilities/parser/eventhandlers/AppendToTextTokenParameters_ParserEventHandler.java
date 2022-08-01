package dev.pdml.ext.utilities.parser.eventhandlers;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.token.TextToken;

public class AppendToTextTokenParameters_ParserEventHandler
    implements PDMLParserEventHandler<NodeStartEvent, TextTokenParameters> {


    private final @NotNull TextTokenParameters textTokenParameters;
    private @Nullable NodeStartEvent rootNode;
    private @Nullable TextToken currentNameToken;


    public AppendToTextTokenParameters_ParserEventHandler (
        @NotNull TextTokenParameters textTokenParameters ) {

        this.textTokenParameters = textTokenParameters;
        this.rootNode = null;
        this.currentNameToken = null;
    }

    public void onStart() {}

    public void onEnd() {}

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

        @NotNull String name = start.getName().qualifiedName();
        if ( textTokenParameters.containsName ( name ) ) {
            throw new DataValidatorException (
                "KEY_EXISTS_ALREADY",
                "Node '" + name + "' has already been defined.",
                start.getToken(), null );
        }

        currentNameToken = start.getName().getToken();

        return start;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {

        if ( currentNameToken != null ) {
            // It's an empty node (no text defined)
            textTokenParameters.add ( currentNameToken, null );
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
            textTokenParameters.add ( attribute.getName().getToken(), attribute.getValue() );
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

        assert currentNameToken != null;
        textTokenParameters.add ( currentNameToken, textToken );

        currentNameToken = null;
    }

    public void onComment ( @NotNull TextToken comment, @NotNull NodeStartEvent start ) {}

    public @Nullable TextTokenParameters getResult() {

        return textTokenParameters.isEmpty() ? null : textTokenParameters;
    }
}
