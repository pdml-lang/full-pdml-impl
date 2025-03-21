package dev.pp.pdml.ext.utils;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.attribute.NodeAttributes;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameter.Parameter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SetConstantHandler implements ExtensionNodeHandler {


    public static final @NotNull String NAME = "set";
    public static final SetConstantHandler INSTANCE = new SetConstantHandler();


    private SetConstantHandler (){}



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

        context.skipSpacesAndTabsAndLineBreaksAndComments();

        PdmlParser parser = context.getPdmlParser();
        NodeAttributes attributes = parser.parseAttributesWithOptionalParenthesis ();

        if ( attributes == null || attributes.isEmpty() ) {
            throw context.errorAtCurrentLocation (
                "Expecting one or more constant declarations (e.g. c1 = v1 ...).",
                "MISSING_CONSTANT_DECLARATION" );
        }

        context.skipSpacesAndTabsAndLineBreaksAndComments();
        context.requireExtensionNodeEnd ( nodeName );

        Map<String, String> declaredConstants = context.getDeclaredConstants();

        List<Parameter<String>> list = attributes.list();
        assert list != null;

        for ( Parameter<String> attribute : list ) {

            String name = attribute.getName();

            if ( ! declaredConstants.containsKey ( name ) ) {
                declaredConstants.put ( name, attribute.getValue () );
            } else {
                throw context.error (
                    "Constant '" + name + "' has already been defined.",
                    "CONSTANT_DEFINED_TWICE",
                    attribute.nameToken() );
            }
        }

        return null;
    }
}
