package dev.pp.pdml.ext.utils;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.pdml.ext.InsertStringFormat;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.token.TextToken;

import java.io.IOException;
import java.util.Map;

public class GetConstantHandler implements ExtensionNodeHandler {

    public static final @NotNull String NAME = "get";
    public static final GetConstantHandler INSTANCE = new GetConstantHandler();


    private GetConstantHandler(){}


    @Override
    public @NotNull String getExtensionKind() {
        return UtilityHandlerConstants.EXTENSION_KIND;
    }

    public @NotNull String getExtensionName() {
        return NAME;
    }

    @Override
    public @Nullable InsertStringExtensionResult handleNode ( @NotNull ExtensionNodeHandlerContext context, @NotNull NodeTag nodeName )
        throws IOException, PdmlException {

        // now positioned right after the node tag

        context.skipWhitespaceAndComments ();

        PdmlReader reader = context.getPdmlReader();
        TextToken nameToken = reader.readTagToken ();
        if ( nameToken == null ) {
            throw reader.errorAtCurrentLocation (
                "Expecting the tag of a previously declared constant.",
                "EXPECTING_CONSTANT_NAME" );
        }
        String name = nameToken.getText();

        Map<String, String> constants = context.getDeclaredConstants();
        String value = constants.get ( name );
        if ( value == null ) {
            String message = "A constant with tag '" + name + "' doesn't exist.";
            if ( constants.isEmpty() ) {
                message = message + " No constants have been defined.";
            } else {
                message = message + " The following constants have been defined: " + constants.keySet();
            }
            throw context.error (
                message,
                "INVALID_CONSTANT_NAME",
                nameToken );
        }

        context.skipWhitespaceAndComments ();
        context.requireExtensionNodeEnd ( nodeName );

        return new InsertStringExtensionResult ( value, InsertStringFormat.AS_IS );

        // reader.insertStringToRead ( value );
        // return null;

        /* TODO
        if ( asText ) {
            return value;
        } else {
            reader.insertStringToRead ( value );
            return null;
        }
         */
    }
}
