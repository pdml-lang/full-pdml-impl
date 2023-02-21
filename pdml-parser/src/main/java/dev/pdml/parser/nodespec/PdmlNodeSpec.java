package dev.pdml.parser.nodespec;

import dev.pdml.data.node.NodeName;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.parameters.parameterspecs.ParameterSpecs;
import dev.pp.text.documentation.SimpleDocumentation;

import java.util.function.Supplier;

public class PdmlNodeSpec<T> {

    private final @NotNull NodeName name;
    public @NotNull NodeName getName() { return name; }

    // TODO? private final @Nullable Set<NodeName> alternativeNames;

    private final @Nullable PdmlType<T> type;
    public @Nullable PdmlType<T> getType() { return type; }

    private final @Nullable ParameterSpecs<?> attributeSpecs;
    public @Nullable ParameterSpecs<?> getAttributeSpecs () { return attributeSpecs; }

    private final boolean hasOnlyAttributes;
    public boolean hasOnlyAttributes () { return hasOnlyAttributes; }

    private final boolean hasAllAttributesOnTagLine;
    public boolean hasAllAttributesOnTagLine () { return hasAllAttributesOnTagLine; }

    private final @Nullable Supplier<SimpleDocumentation> documentation;
    public @Nullable Supplier<SimpleDocumentation> getDocumentationSupplier() { return documentation; }

    // private final @Nullable PXMLExtensionHandler extensionHandler;
    // private final @Nullable Function<ASTNode, NodeValidationError> validator;
    // private final boolean attributesAllowed = true;


    public PdmlNodeSpec (
        @NotNull NodeName name,
        @Nullable PdmlType<T> type,
        @Nullable ParameterSpecs<?> attributeSpecs,
        boolean hasOnlyAttributes,
        boolean hasAllAttributesOnTagLine,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this.name = name;
        this.type = type;
        this.attributeSpecs = attributeSpecs;
        this.hasOnlyAttributes = hasOnlyAttributes;
        this.hasAllAttributesOnTagLine = hasAllAttributesOnTagLine;
        this.documentation = documentation;
    }

    public PdmlNodeSpec (
        @NotNull NodeName name,
        @Nullable PdmlType<T> type,
        @Nullable ParameterSpecs<?> attributeSpecs,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this ( name, type, attributeSpecs, false, false, documentation );
    }


    public @Nullable ParameterSpec<?> getFirstPositionalAttributeOrNull () {
        return attributeSpecs != null ? attributeSpecs.firstPositionalParameterOrNull () : null;
    }

    public @Nullable String getFirstPositionalAttributeNameOrNull () {
        @Nullable ParameterSpec<?> firstPositional = getFirstPositionalAttributeOrNull();
        return firstPositional != null ? firstPositional.getName() : null;
    }

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

    public @NotNull String qualifiedName() { return name.qualifiedName(); }

    @Override public String toString() { return name.toString(); }
}
