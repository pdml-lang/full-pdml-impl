package dev.pp.pdml.data.nodespec;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.validation.TaggedNodeValidator;
import dev.pp.pdml.data.validation.TaggedNodeValidatorContext;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameterspec.ParameterSpec;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.documentation.SimpleDocumentation;

import java.util.function.Supplier;

public class PdmlNodeSpec {

    private final @NotNull NodeTag name;
    public @NotNull NodeTag getName() { return name; }

    // TODO? private final @Nullable Set<NodeName> alternativeNames;

    // private final @Nullable PdmlType<T> type;
    // public @Nullable PdmlType<T> getType() { return type; }
    private final @Nullable String typeName;
    public @Nullable String getTypeName() { return typeName; }

    private final @Nullable ParameterSpecs<?> attributeSpecs;
    public @Nullable ParameterSpecs<?> getAttributeSpecs () { return attributeSpecs; }

    private final boolean hasOnlyAttributes;
    public boolean hasOnlyAttributes () { return hasOnlyAttributes; }

    // private final boolean hasAllAttributesOnTagLine;
    // public boolean hasAllAttributesOnTagLine () { return hasAllAttributesOnTagLine; }

    private final @Nullable TaggedNodeValidator validator;
    public @Nullable TaggedNodeValidator getValidator() { return validator; }

    private final @Nullable Supplier<SimpleDocumentation> documentation;
    public @Nullable Supplier<SimpleDocumentation> getDocumentationSupplier() { return documentation; }

    // private final @Nullable PXMLExtensionHandler extensionHandler;
    // private final @Nullable Function<ASTNode, NodeValidationError> validator;
    // private final boolean attributesAllowed = true;


    public PdmlNodeSpec (
        @NotNull NodeTag name,
        // @Nullable PdmlType<T> type,
        @Nullable String typeName,
        @Nullable ParameterSpecs<?> attributeSpecs,
        boolean hasOnlyAttributes,
        // boolean hasAllAttributesOnTagLine,
        @Nullable TaggedNodeValidator validator,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this.name = name;
        // this.type = type;
        this.typeName = typeName;
        this.attributeSpecs = attributeSpecs;
        this.hasOnlyAttributes = hasOnlyAttributes;
        // this.hasAllAttributesOnTagLine = hasAllAttributesOnTagLine;
        this.validator = validator;
        this.documentation = documentation;
    }

    public PdmlNodeSpec (
        @NotNull NodeTag name,
        // @Nullable PdmlType<T> type,
        @Nullable String typeName,
        @Nullable ParameterSpecs<?> attributeSpecs,
        @Nullable Supplier<SimpleDocumentation> documentation ) {

        this ( name, typeName, attributeSpecs, false, null, documentation );
    }


    public boolean validate (
        @NotNull TaggedNode taggedNode,
        @NotNull TaggedNodeValidatorContext context ) {

        if ( validator == null ) {
            return true;
        } else {
            return validator.validate ( this, taggedNode, context );
        }
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

    public @NotNull String qualifiedName() { return name.qualifiedTag (); }

    @Override public String toString() { return name.toString(); }
}
