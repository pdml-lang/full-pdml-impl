package dev.pdml.data.node.branch;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.attribute.NodeAttribute;
import dev.pdml.data.node.MutablePdmlNode;
import dev.pdml.data.node.NodeName;
import dev.pdml.data.node.leaf.MutableCommentNode;
import dev.pdml.data.node.leaf.MutableTextNode;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.shared.constants.PdmlExtensionsConstants;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.List;
import java.util.function.Consumer;

public abstract class MutableRootOrBranchNode implements MutablePdmlNode {

    protected @NotNull NodeName name;
    @NotNull public NodeName getName() { return name; }
    public void setName ( @NotNull NodeName name ) { this.name = name; }

    protected @Nullable MutableChildNodes childNodes;
    public @Nullable MutableChildNodes getChildren() { return childNodes; }
    public void setChildren ( @Nullable MutableChildNodes childNodes ) { this.childNodes = childNodes; }

    protected @Nullable MutableNodeAttributes attributes;
    public @Nullable MutableNodeAttributes getAttributes() { return attributes; }
    public void setAttributes ( @Nullable MutableNodeAttributes attributes ) { this.attributes = attributes; }

    protected @Nullable MutableNodeNamespaces namespaceDefinitions;
    public @Nullable MutableNodeNamespaces getNamespaceDefinitions() { return namespaceDefinitions; }
    public void setNamespaceDefinitions ( @Nullable MutableNodeNamespaces namespaces ) { this.namespaceDefinitions = namespaces; }


    protected MutableRootOrBranchNode (
        @NotNull NodeName name,
        @Nullable MutableChildNodes childNodes,
        @Nullable MutableNodeAttributes attributes,
        @Nullable MutableNodeNamespaces namespaceDefinitions ) {

        this.name = name;
        this.childNodes = childNodes;
        this.attributes = attributes;
        this.namespaceDefinitions = namespaceDefinitions;
    }

    protected MutableRootOrBranchNode (
        @NotNull NodeName name,
        @Nullable MutableNodeAttributes attributes,
        @Nullable MutableNodeNamespaces namespaceDefinitions ) {

        this ( name, null, attributes, namespaceDefinitions);
    }

    protected MutableRootOrBranchNode (
        @NotNull NodeName name ) {

        this ( name, null, null, null );
    }

    protected MutableRootOrBranchNode (
        @NotNull String localName ) {

        this ( new NodeName ( localName ), null, null, null );
    }

    protected MutableRootOrBranchNode (
        @NotNull TextToken nameToken ) {

        this ( new NodeName ( nameToken ), null, null, null );
    }


    // Queries

    public boolean isEmpty() { return hasNoAttributes() && hasNoChildNodes(); }

    public boolean isNotEmpty() { return ! isEmpty(); }

    public boolean hasAttributes() { return attributes != null && attributes.isNotEmpty(); }

    public boolean hasNoAttributes() { return ! hasAttributes(); }

    public boolean hasChildNodes() { return childNodes != null && childNodes.isNotEmpty(); }

    public boolean hasNoChildNodes() { return ! hasChildNodes(); }

    public @NotNull TextToken nameToken() { return name.localNameToken (); }

    public @Nullable TextToken namespacePrefixToken() { return name.namespacePrefixToken(); }

    public void forEachChildNode ( @NotNull Consumer<MutableChildNode> consumer ) {
        if ( childNodes == null ) return;
        childNodes.forEachChild ( consumer );
    }

    public @Nullable List<MutableChildNode> childNodesList() {
        return childNodes == null ? null : childNodes.list();
    }

    public abstract @NotNull List<NodeName> pathNames();

    public @NotNull List<String> pathNameStrings() {
        return pathNames().stream().map ( NodeName::toString ).toList();
    }

    public @NotNull String path () {
        return String.join ( PdmlExtensionsConstants.PATH_SEPARATOR_AS_STRING , pathNameStrings() );
    }


    // Add Attributes

    public MutableRootOrBranchNode addAttribute ( @NotNull NodeAttribute attribute ) {

        if ( attributes == null ) attributes = new MutableNodeAttributes ( null );
        attributes.add ( attribute );

        return this;
    }

    public MutableRootOrBranchNode addAttribute ( @NotNull String name, @Nullable String value ) {
        return addAttribute ( new NodeAttribute ( name, value ) );
    }


    // Append Child Nodes

    public MutableRootOrBranchNode appendChild ( @NotNull MutableChildNode child ) {

        if ( childNodes == null ) childNodes = new MutableChildNodes ();
        childNodes.append ( this, child );

        return this;
    }

    public MutableRootOrBranchNode appendBranchNode ( @NotNull MutableBranchNode branchNode ) {
        return appendChild ( branchNode );
    }

    public MutableRootOrBranchNode appendTextNode ( @NotNull MutableTextNode textNode ) {
        return appendChild ( textNode );
    }

    public MutableRootOrBranchNode appendText ( @NotNull String text ) {
        return appendTextNode ( new MutableTextNode ( text ) );
    }

    public MutableRootOrBranchNode appendCommentNode ( @NotNull MutableCommentNode commentNode ) {
        return appendChild ( commentNode );
    }

    public MutableRootOrBranchNode appendComment ( @NotNull String text ) {
        return appendCommentNode ( new MutableCommentNode ( text ) );
    }


    // Add Namespace Definitions

    public MutableRootOrBranchNode addNamespaceDefinition ( @NotNull NodeNamespace namespaceDefinition ) {

        if ( namespaceDefinitions == null ) namespaceDefinitions = new MutableNodeNamespaces ();
        namespaceDefinitions.add ( namespaceDefinition );

        return this;
    }
}
