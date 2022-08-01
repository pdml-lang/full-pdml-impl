package dev.pdml.core.data.formalNode;

import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pdml.core.data.node.name.NodeName;
import dev.pp.text.documentation.SimpleDocumentation;

import java.util.function.Supplier;

public class FormalPDMLNode<T> {

    private final @NotNull NodeName name;

    private final @Nullable PDMLType<T> type;

    private final @Nullable FormalParameters attributes;
    // private final @Nullable NodeName defaultAttributeName;
    private final boolean hasOnlyAttributes;
    private final boolean hasAllAttributesOnTagLine;

    private final @Nullable Supplier<SimpleDocumentation> documentation;

    // private final @Nullable PXMLExtensionHandler extensionHandler;
    // private final @Nullable Function<ASTNode, NodeValidationError> validator;
    // private final boolean attributesAllowed = true;


    public FormalPDMLNode (
        @NotNull NodeName name,
        @Nullable PDMLType<T> type,
        @Nullable FormalParameters attributes,
        // @Nullable NodeName defaultAttributeName,
        boolean hasOnlyAttributes,
        boolean hasAllAttributesOnTagLine,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this.name = name;
        this.type = type;
        this.attributes = attributes;
        // this.defaultAttributeName = defaultAttributeName;
        this.hasOnlyAttributes = hasOnlyAttributes;
        this.hasAllAttributesOnTagLine = hasAllAttributesOnTagLine;
        this.documentation = documentation;
    }

    public FormalPDMLNode (
        @NotNull NodeName name,
        @Nullable PDMLType<T> type,
        @Nullable FormalParameters attributes,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this ( name, type, attributes, false, false, documentation );
    }



    public @NotNull NodeName getName() { return name; }
    public @NotNull String getNameAsString() { return name.toString(); }

    public @Nullable PDMLType<T> getType() { return type; }

    public @Nullable FormalParameters getAttributes () { return attributes; }

    public @Nullable FormalParameter<?> getFirstPositionalAttributeOrNull () {

        return attributes != null ? attributes.getFirstPositionalParameterOrNull() : null;
    }

    public @Nullable String getFirstPositionalAttributeNameOrNull () {

        @Nullable FormalParameter<?> firstPositional = getFirstPositionalAttributeOrNull();
        return firstPositional != null ? firstPositional.getName() : null;
    }

    public boolean hasOnlyAttributes () { return hasOnlyAttributes; }

    public boolean hasAllAttributesOnTagLine () { return hasAllAttributesOnTagLine; }

    public @Nullable Supplier<SimpleDocumentation> getDocumentationSupplier() { return documentation; }
    public @Nullable SimpleDocumentation getDocumentation() {
        Supplier<SimpleDocumentation> supplier = getDocumentationSupplier();
        return supplier != null ? supplier.get() : null;
    }

    public @Nullable String getDocumentationTitle() {
        SimpleDocumentation doc = getDocumentation();
        return doc != null ? doc.getTitle() : null;
    }

    public @Nullable String getDocumentationDescription() {
        SimpleDocumentation doc = getDocumentation();
        return doc != null ? doc.getDescription() : null;
    }

    public @Nullable String getDocumentationExamples() {
        SimpleDocumentation doc = getDocumentation();
        return doc != null ? doc.getExamples() : null;
    }

    // public @Nullable PXMLExtensionHandler getExtensionHandler() { return extensionHandler; }

    @Override public String toString() { return name.toString(); }
}
