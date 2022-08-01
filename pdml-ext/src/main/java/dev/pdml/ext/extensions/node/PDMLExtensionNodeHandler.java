package dev.pdml.ext.extensions.node;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.error.TextErrorException;

import java.io.IOException;

public interface PDMLExtensionNodeHandler {

    @NotNull QualifiedNodeName getQualifiedNodeName();

    default @NotNull Namespace getNamespace() {
        return getQualifiedNodeName().getNamespace();
    }

    default @NotNull String getLocalNodeName() {
        return getQualifiedNodeName().getLocalName();
    }

    // when this method is called, the node name and a space after the node name are consumed already by the textReader
    void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException;
}
