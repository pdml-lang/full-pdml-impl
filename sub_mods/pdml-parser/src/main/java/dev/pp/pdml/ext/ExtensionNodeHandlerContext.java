package dev.pp.pdml.ext;

import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.exception.InvalidPdmlDataException;
import dev.pp.pdml.data.exception.MalformedPdmlException;
import dev.pp.pdml.data.attribute.NodeAttributes;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameters.ParametersCreator;
import dev.pp.core.parameters.parameterspecs.MutableOrImmutableParameterSpecs;
import dev.pp.core.scriptingbase.env.ScriptingEnvironment;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.token.TextToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtensionNodeHandlerContext {


    private final @NotNull PdmlReader pdmlReader;
    public @NotNull PdmlReader getPdmlReader() { return pdmlReader; }

    private final @NotNull PdmlParser pdmlParser;
    public @NotNull PdmlParser getPdmlParser() { return pdmlParser; }

    private final @Nullable ScriptingEnvironment scriptingEnvironment;
    public @Nullable ScriptingEnvironment getScriptingEnvironment() { return scriptingEnvironment; }

    public @NotNull ScriptingEnvironment requireScriptingEnvironment (
        @Nullable TextToken errorToken ) throws PdmlException {

        if ( scriptingEnvironment != null ) {
            return scriptingEnvironment;
        } else {
            throw error (
                "Scripting is not supported in this context (scriptingEnvironment == null)",
                "SCRIPTING_NOT_SUPPORTED",
                errorToken );
        }
    }


    private final @NotNull Map<String, String> declaredConstants;
    public @NotNull Map<String, String> getDeclaredConstants() { return declaredConstants; }


    public ExtensionNodeHandlerContext (
        @NotNull PdmlReader pdmlReader,
        @NotNull PdmlParser pdmlParser,
        @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.pdmlReader = pdmlReader;
        this.pdmlParser = pdmlParser;
        this.scriptingEnvironment = scriptingEnvironment;

        this.declaredConstants = new HashMap<>();
    }


    public void skipSpacesAndTabsAndLineBreaksAndComments () throws IOException, MalformedPdmlException {
        pdmlReader.skipSpacesAndTabsAndLineBreaksAndComments();
    }

    public void requireExtensionNodeEnd ( @NotNull NodeTag nodeName ) throws IOException, MalformedPdmlException {

        if ( ! pdmlReader.readNodeEnd() ) {
            // TODO use nodeName to improve error message
            throw new MalformedPdmlException (
                "Expecting '" + CorePdmlConstants.NODE_END_CHAR + "'.",
                "EXTENSION_NODE_END_REQUIRED",
                pdmlReader.currentToken() );
        }
/*
            String message = "Expecting '" + CorePdmlConstants.NODE_END_CHAR + "' to close node '" + taggedNode.getName() + "'";
            @Nullable TextLocation nodeStartLocation = taggedNode.getName().location();
            if ( nodeStartLocation != null ) {
                message = message + " at position: " + nodeStartLocation;
            }
            message = message + ".";

            throw errorAtCurrentLocation (
                message,
                "EXPECTING_NODE_END" );
 */
    }

    public <V> @Nullable Parameters<V> parseParametersWithOptionalParenthesis (
        @Nullable MutableOrImmutableParameterSpecs<V> parameterSpecs ) throws IOException, PdmlException {

        @Nullable NodeAttributes attributes = pdmlParser.parseAttributesWithOptionalParenthesis();

        try {
            return ParametersCreator.createFromStringParameters (
                attributes, attributes == null ? null : attributes.getStartToken(), parameterSpecs );
        } catch ( InvalidTextException e ) {
            throw new InvalidPdmlDataException ( e );
        }
    }

    public @NotNull PdmlException error (
        @NotNull String message, @NotNull String id, @Nullable TextToken token ) {

        return new PdmlException ( message, id, token );
    }

    public @NotNull PdmlException errorAtCurrentLocation (
        @NotNull String message, @NotNull String id ) {

        return error ( message, id, pdmlReader.currentToken() );
    }
}
