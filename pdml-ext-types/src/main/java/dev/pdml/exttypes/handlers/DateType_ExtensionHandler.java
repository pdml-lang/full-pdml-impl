package dev.pdml.exttypes.handlers;

import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.exttypes.PdmlDateType;
import dev.pdml.exttypes.TypeConstants;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.inspection.TextErrorException;

import java.io.IOException;

public class DateType_ExtensionHandler implements PdmlExtensionNodeHandler {

    public static final @NotNull NodeNamespace NAMESPACE = TypeConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "date";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final DateType_ExtensionHandler INSTANCE = new DateType_ExtensionHandler();


    private DateType_ExtensionHandler(){}


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException, DataValidatorException {

        // TODO read parameters

        PdmlDateType type = new PdmlDateType ();
        TypeExtensionHandlerHelper.handleTypeNode ( context, type, nodeName );
    }
}
