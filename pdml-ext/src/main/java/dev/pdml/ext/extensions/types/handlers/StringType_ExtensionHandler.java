package dev.pdml.ext.extensions.types.handlers;

import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.types.PDMLStringType;
import dev.pp.basics.annotations.NotNull;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.error.TextErrorException;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;

import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;

import java.io.IOException;

public class StringType_ExtensionHandler implements PDMLExtensionNodeHandler {


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.TYPE_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "string";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final StringType_ExtensionHandler INSTANCE = new StringType_ExtensionHandler();


    private StringType_ExtensionHandler(){}


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }


    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException, DataValidatorException {

        // TODO read parameters

        PDMLStringType type = new PDMLStringType ();
        TypeExtensionHandlerHelper.handleTypeNode ( context, type, nodeName );
    }
}
