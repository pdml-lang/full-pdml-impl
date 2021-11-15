package dev.pdml.core.data.formalNode;

import dev.pdml.core.data.formalNode.types.PDMLType;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.core.data.node.name.NodeName;
import dev.pp.text.documentation.SimpleDocumentation;

import java.util.function.Supplier;

public class FormalNode<T> {


    private final @NotNull NodeName name;

    private @Nullable PDMLType<T> type = null;

    private @Nullable FormalParameters<T> formalAttributes = null;
    private boolean hasOnlyAttributes = false;
    private boolean hasAllAttributesOnTagLine = false;
    private @Nullable NodeName defaultAttributeName = null;

    private @Nullable Supplier<SimpleDocumentation> documentation = null;

    // private final @Nullable PXMLExtensionHandler extensionHandler;
    // private final @Nullable Function<ASTNode, NodeValidationError> validator;
    // private final boolean attributesAllowed = true;


    public FormalNode (
        @NotNull NodeName name,
        @Nullable PDMLType<T> type,
        @Nullable FormalParameters<T> formalAttributes,
        @Nullable NodeName defaultAttributeName,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this.name = name;
        this.type = type;
        this.formalAttributes = formalAttributes;
        this.defaultAttributeName = defaultAttributeName;
        this.documentation = documentation;
    }

    public FormalNode ( @NotNull NodeName name ) {

        this.name = name;
    }


    public @NotNull NodeName getName() { return name; }

    public @Nullable PDMLType<?> getType() { return type; }

    public @Nullable FormalParameters<T> getFormalAttributes () { return formalAttributes; }

    public boolean hasOnlyAttributes () { return hasOnlyAttributes; }

    public boolean hasAllAttributesOnTagLine () { return hasAllAttributesOnTagLine; }

    public @Nullable NodeName getDefaultAttributeName() { return defaultAttributeName; }

    public @Nullable Supplier<SimpleDocumentation> getDocumentation () { return documentation; }

    // public @Nullable PXMLExtensionHandler getExtensionHandler() { return extensionHandler; }


    public void setType ( @Nullable PDMLType<T> type ) {
        this.type = type;
    }

    public void setFormalAttributes ( @Nullable FormalParameters<T> formalAttributes ) {
        this.formalAttributes = formalAttributes;
    }

    public void setHasOnlyAttributes ( boolean hasOnlyAttributes ) {
        this.hasOnlyAttributes = hasOnlyAttributes;
    }

    public void setHasAllAttributesOnTagLine ( boolean hasAllAttributesOnTagLine ) {
        this.hasAllAttributesOnTagLine = hasAllAttributesOnTagLine;
    }

    public void setDefaultAttributeName ( @Nullable NodeName defaultAttributeName ) {
        this.defaultAttributeName = defaultAttributeName;
    }

    public void setDocumentation ( @Nullable Supplier<SimpleDocumentation> documentation ) {
        this.documentation = documentation;
    }


    @Override public String toString() { return name.toString(); }
}
