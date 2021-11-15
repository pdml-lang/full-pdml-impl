package dev.pdml.core.data.AST;

import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.children.*;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.function.Consumer;

public class ASTNode { // implements ASTElement {

    private final @NotNull ASTNodeName name;
    private final @Nullable ASTNamespaces declaredNamespaces;
    private @Nullable ASTNodeAttributes attributes;
    private @Nullable ASTNodeChildren children;


    public ASTNode (
        @NotNull ASTNodeName name,
        @Nullable ASTNamespaces declaredNamespaces,
        @Nullable ASTNodeAttributes attributes,
        @Nullable ASTNodeChildren children ) {

        this.name = name;
        this.declaredNamespaces = declaredNamespaces;
        this.attributes = attributes;
        this.children = children;
    }


    // name

    public @NotNull ASTNodeName getName() { return name; }

    public @Nullable TextLocation getLocation() { return name.getLocation(); }


    // namespace

    public @Nullable
    ASTNamespace getNamespace() { return name.getNamespace(); }

    public boolean hasNamespace() { return getNamespace() != null; }

    public @NotNull String getLocalName() { return name.getLocalNameText (); }

    public @Nullable String getNamespacePrefix() { return name.getNamespacePrefixText (); }

    public @Nullable String getNamespaceURIText() { return name.getNamespaceURIText (); }

    public @Nullable ASTNamespaces getDeclaredNamespaces() { return declaredNamespaces; }


    // attributes

    public @Nullable ASTNodeAttributes getAttributes() { return attributes; }

    public void setAttributes ( @Nullable ASTNodeAttributes attributes ) { this.attributes = attributes; }


    // children

    public @Nullable ASTNodeChildren getChildren() { return children; }

    public void setChildren ( @Nullable ASTNodeChildren children ) { this.children = children; }

    public void appendChildNode ( ASTNode childNode ) {

        ensureHasChildren();

        children.append ( new Node_ASTNodeChild ( childNode ) );
    }

    public void appendTextChild ( TextToken text ) {

        ensureHasChildren();

        children.append ( new Text_ASTNodeChild ( text ) );
    }

    public void appendCommentChild ( TextToken comment ) {

        ensureHasChildren();

        children.append ( new Comment_ASTNodeChild ( comment ) );
    }

    public void visitTree ( @NotNull Consumer<ASTNodeChild> consumer ) {

        visitChildren ( new Node_ASTNodeChild ( this ), true, consumer );
    }

    public void visitChildren ( @NotNull Consumer<ASTNodeChild> consumer ) {

        visitChildren ( new Node_ASTNodeChild ( this ), false, consumer );
    }

    private void visitChildren (
        @NotNull ASTNodeChild parent,
        boolean includeParentNode,
        @NotNull Consumer<ASTNodeChild> consumer ) {

        if ( includeParentNode ) consumer.accept ( parent );

        if ( ! ( parent instanceof Node_ASTNodeChild ) ) return;

        ASTNodeChildren children = ((Node_ASTNodeChild) parent).getNode().getChildren();
        if ( children == null ) return;

        for ( ASTNodeChild child : children.getList() ) {
            visitChildren ( child, true, consumer );
        }
    }

    public @Nullable String concatenateTextInChildNodes() {

        StringBuilder sb = new StringBuilder();

        visitChildren ( child -> {
            if ( child instanceof Text_ASTNodeChild ) {
                Text_ASTNodeChild textChild = (Text_ASTNodeChild) child;
                sb.append ( textChild.getText() );
            }
        } );

        return sb.length() == 0 ? null : sb.toString();
    }

/*
        visit_tree__only_nodes ( node_consumer object_consumer<mutable_PDML_AST_node>, include_this_node yes_no )

        visit_tree__only_nodes

            visit_tree__only_nodes_ (
                parent_node = this
                i_node_consumer
                include_parent_node = i_include_this_node )
        .

        visit_tree__only_nodes_ access:private (
            parent_node mutable_PDML_AST_node, node_consumer object_consumer<mutable_PDML_AST_node>, include_parent_node yes_no )

            if i_include_parent_node then
                i_node_consumer.consume ( i_parent_node )
            .

            const direct_child_nodes = i_parent_node.direct_child_nodes__only_nodes on_null:return
            repeat for each direct_child_node in direct_child_nodes
                visit_tree__only_nodes_ ( parent_node = direct_child_node, i_node_consumer, include_parent_node = yes )
            .
        .

 */
    private void ensureHasChildren() {

        if ( children == null ) this.children = new ASTNodeChildren ();
    }



    /*
    public @Nullable URI getNamespaceURI() { return name.getNamespaceURI(); }

    public boolean hasNamespace() { return name.hasNamespace(); }

    public int getLineNumber() {

        if ( location != null ) {
            return location.getLineNumber();
        } else {
            return 0;
        }
    }

    public int getColumnNumber() {

        if ( location != null ) {
            return location.getColumnNumber();
        } else {
            return 0;
        }
    }
*/
    public @NotNull String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append ( name );

        TextLocation location = getLocation();
        if ( location != null ) {
            sb.append ( " at " );
            sb.append ( location );
        }

        return sb.toString();
    }
}
