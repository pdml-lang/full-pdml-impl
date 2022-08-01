package dev.pdml.ext.extensions.types.handlers;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pdml.ext.extensions.types.PDMLDateType;
import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.error.TextErrorException;

import java.io.IOException;

public class DateType_ExtensionHandler implements PDMLExtensionNodeHandler {


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.TYPE_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "date";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final DateType_ExtensionHandler INSTANCE = new DateType_ExtensionHandler();


    private DateType_ExtensionHandler(){}


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }

    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException, DataValidatorException {

        // TODO read parameters

        PDMLDateType type = new PDMLDateType ();
        TypeExtensionHandlerHelper.handleTypeNode ( context, type, nodeName );
    }
}
