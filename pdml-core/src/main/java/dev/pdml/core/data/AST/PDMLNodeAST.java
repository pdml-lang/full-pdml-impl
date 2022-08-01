package dev.pdml.core.data.AST;

import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.children.*;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.exception.PDMLDocumentException;
import dev.pdml.core.exception.PDMLDocumentSemanticException;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.List;

public class PDMLNodeAST { // implements ASTElement {

    private final @NotNull ASTNodeName name;
    private final @Nullable ASTNamespaces declaredNamespaces;
    private @Nullable ASTNodeAttributes attributes;
    private @Nullable ASTNodeChildren children;


    public PDMLNodeAST (
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

    public @Nullable ASTNamespace getNamespace() { return name.getNamespace(); }

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

    public void appendNodeChild ( PDMLNodeAST childNode ) {

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

/*
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
*/

    public @Nullable List<PDMLNodeAST> getNodeChildrenByLocalNameOrNull ( String localName ) {

        return children == null ? null : children.nodeElementsByLocalName ( localName );
    }

    public @Nullable PDMLNodeAST getUniqueNodeChildByLocalNameOrNull ( String localName ) {

        @Nullable List<PDMLNodeAST> list = getNodeChildrenByLocalNameOrNull ( localName );
        if ( list == null ) {
            return null;
        } else if ( list.size() == 1 ) {
            return list.get ( 0 );
        } else {
            return null;
        }
    }

    public @NotNull PDMLNodeAST getUniqueNodeChildByLocalName ( String localName ) throws PDMLDocumentException {

        PDMLNodeAST result = getUniqueNodeChildByLocalNameOrNull ( localName );
        if ( result != null ) {
            return result;
        } else {
            throw new PDMLDocumentSemanticException (
                "MISSING_UNIQUE_CHILD_NODE",
                "Node '" + getName().qualifiedName () + "' must have exactly one child node with name '" + localName + "'.",
                getName().getToken() );
        }
    }

    public @Nullable PDMLNodeAST getUniqueNodeChildByLocalName (
        String localName, TextErrorHandler errorHandler ) {

        try {
            return getUniqueNodeChildByLocalName ( localName );
        } catch ( PDMLDocumentException e ) {
            errorHandler.handleAbortingError ( e.getId(), e.getMessage(), e.getToken() );
            return null;
        }
    }

    public @Nullable String getTextOfUniqueTextChildNodeByLocalNameOrNull ( String localName ) {

        PDMLNodeAST childNode = getUniqueNodeChildByLocalNameOrNull ( localName );

        return childNode == null ? null : childNode.getSingleTextContentOrNull();
    }

    public @NotNull String getTextOfUniqueTextChildNodeByLocalName ( String localName ) throws PDMLDocumentException {

        String result = getTextOfUniqueTextChildNodeByLocalNameOrNull ( localName );
        if ( result != null ) {
            return result;
        } else {
            throw new PDMLDocumentSemanticException (
                "MISSING_UNIQUE_TEXT_CHILD_NODE",
                "Node '" + getName().qualifiedName () + "' must have exactly one text child node with name '" + localName + "'.",
                getName().getToken() );
        }
    }

    public @Nullable String getTextOfUniqueTextChildNodeByLocalName (
        String localName, TextErrorHandler errorHandler ) {

        try {
            return getTextOfUniqueTextChildNodeByLocalName ( localName );
        } catch ( PDMLDocumentException e ) {
            errorHandler.handleAbortingError ( e.getId(), e.getMessage(), e.getToken() );
            return null;
        }
    }

    /*
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
*/
    public @Nullable String concatenateTextInChildNodes() {

        return children == null ? null : children.concatenateTextElements();
    }

/*
    public @NotNull String requireSingleTextContent() throws PDMLDocumentException {

        if ( children == null ) throw new PDMLDocumentException (
            "Node '" + name.toString() + "' cannot be empty.", name.getToken() );

        if ( children.elementCount() != 1 ) throw new PDMLDocumentException (
            "Node '" + name.toString() + "' must contain only text", name.getToken() );

        if ( ! ( children.firstElement() instanceof Text_ASTNodeChild ) ) throw new PDMLDocumentException (
            "Node '" + name.toString() + "' must contain only text", name.getToken() );

        String result = concatenateTextInChildNodes();

        if ( result == null ) throw new PDMLDocumentException (
            "Node '" + name.toString() + "' must contain text", name.getToken() );

        return result;
    }
*/
    public @Nullable String getSingleTextContentOrNull() {

        if ( children == null ) return null;

        if ( children.elementCount() != 1 ) return null;

        ASTNodeChild singleChild = children.firstElement();
        if ( singleChild instanceof Text_ASTNodeChild textChild ) {
            return textChild.getText();
        } else {
            return null;
        }
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
