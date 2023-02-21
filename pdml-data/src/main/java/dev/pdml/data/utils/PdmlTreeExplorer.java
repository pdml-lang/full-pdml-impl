package dev.pdml.data.utils;

/*
import dev.pdml.core.data.AST.PDMLNodeAST;
import dev.pdml.core.data.AST.children.*;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;

import java.util.HashMap;
import java.util.Map;
 */

public class PdmlTreeExplorer {

/* Currently not used
    // Single text content
    public static @Nullable String getSingleTextContentOrNull ( @NotNull PDMLNodeAST node ) throws TextErrorException {

        ASTNodeChildren children = node.getChildren();

        if ( children == null ) return null;

        if ( children.hasNodeElements() ) {
            PDMLNodeAST first = children.firstNodeElement();
            assert first != null;
            throw new TextErrorException (
                "Node '" + node + "' can only contain text.",
                "INVALID_TEXT_NODE",
                first.getName().getToken() );
        }

        return children.concatenateTextElements();
    }

    public static @NotNull String getSingleTextContent ( @NotNull PDMLNodeAST node ) throws TextErrorException {

        String result = getSingleTextContentOrNull ( node );
        if ( result != null ) {
            return result;
        } else {
            throw new TextErrorException (
                "Node '" + node + "' must contain text.",
                "INVALID_TEXT_NODE",
                node.getName().getToken() );
        }
    }
 */

    // TODO needed?
    /*
    public static @Nullable String getSingleTextContent (
        @NotNull PDMLNodeAST node,
        @NotNull TextErrorHandler errorHandler ) {

        try {
            return getSingleTextContent ( node );
        } catch ( TextErrorException e ) {
            errorHandler.handleNonAbortingError ( e.getId(), e.getMessage(), e.getToken() );
            return null;
        }
    }

     */


/* Currently not used
    // String map content

    public static @Nullable Map<String, String> getTextMapContentOrNull (
        @NotNull PDMLNodeAST node ) throws TextErrorException {

        ASTNodeChildren children = node.getChildren();
        if ( children == null ) return null;

        Map<String, String> map = new HashMap<>();
        for ( ASTNodeChild childNode : children.elements () ) {

            switch ( childNode ) {

                case Text_ASTNodeChild textChild:
                    if ( textChild.getText().isBlank() ) {
                        break;
                    } else {
                        throw new TextErrorException (
                            "Node '" + node.getName().toString() + "' cannot contain text.",
                            "INVALID_TEXT",
                            node.getTextToken() );
                    }

                case Comment_ASTNodeChild unused:
                    continue;

                case Node_ASTNodeChild nodeChild:
                    String name = nodeChild.getNode().getLocalName();
                    if ( map.containsKey ( name ) ) {
                        throw new TextErrorException (
                            "Node '" + name + "' has already been defined.",
                            "DUPLICATE_VALUE",
                            node.getTextToken() );
                    }
                    String text = getSingleTextContentOrNull ( nodeChild.getNode() );
                    map.put ( name, text );
                    break;

                default:
                    throw new RuntimeException ( "Unexpected child." );
            }
        }

        return map;
    }
 */
}

// TODO Code below copied from old version. Should be adapted to be used here

/*
package dev.pdml.core.data.AST.children;

import dev.pdml.core.data.AST.PDMLNodeAST;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ASTNodeChildren {

    private final @NotNull List<ASTNodeChild> list;


    public ASTNodeChildren () {

        this.list = new ArrayList<>();
    }


    // Lists

    public @NotNull List<ASTNodeChild> elements () {

        return list.stream().toList();
    }

    public @Nullable List<PDMLNodeAST> nodeElements () {

        List<PDMLNodeAST> nodeElements = list.stream()
            .filter ( child -> child instanceof Node_ASTNodeChild )
            .map ( child -> ( (Node_ASTNodeChild) child ).getNode() )
            .toList();

        return nodeElements.isEmpty() ? null : nodeElements;
    }

    public @Nullable List<Text_ASTNodeChild> textElements () {

        List<Text_ASTNodeChild> textElements = list.stream()
            .filter ( child -> child instanceof Text_ASTNodeChild )
            .map ( child -> (Text_ASTNodeChild) child )
            .toList();

        return textElements.isEmpty() ? null : textElements;
    }

    public @Nullable List<String> texts() {

        List<String> texts = list.stream()
            .filter ( child -> child instanceof Text_ASTNodeChild )
            .map ( child -> ((Text_ASTNodeChild) child).getText() )
            .toList();

        return texts.isEmpty() ? null : texts;
    }

    public @Nullable List<Comment_ASTNodeChild> commentElements () {

        List<Comment_ASTNodeChild> commentElements = list.stream()
            .filter ( child -> child instanceof Comment_ASTNodeChild )
            .map ( child -> (Comment_ASTNodeChild) child )
            .toList();

        return commentElements.isEmpty() ? null : commentElements;
    }

    public @Nullable List<ASTNodeChild> nonCommentElements () {

        List<ASTNodeChild> nonCommentElements = list.stream()
            .filter ( child -> ! ( child instanceof Comment_ASTNodeChild ) )
            .toList();

        return nonCommentElements.isEmpty() ? null : nonCommentElements;
    }


    // forEach

    public void forEachElement ( Consumer<ASTNodeChild> consumer ) {

        for ( ASTNodeChild element : list ) {
            consumer.accept ( element );
        }
    }

    public void forEachNodeElement ( Consumer<PDMLNodeAST> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Node_ASTNodeChild nodeChild ) {
                consumer.accept ( nodeChild.getNode() );
            }
        });
    }

    public void forEachTextElement ( Consumer<Text_ASTNodeChild> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Text_ASTNodeChild textChild ) {
                consumer.accept ( textChild );
            }
        });
    }

    public void forEachText ( Consumer<String> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Text_ASTNodeChild textChild ) {
                consumer.accept ( textChild.getText() );
            }
        });
    }

    public void forEachCommentElement ( Consumer<Comment_ASTNodeChild> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Comment_ASTNodeChild commentChild ) {
                consumer.accept ( commentChild );
            }
        });
    }

    public void forEachNonCommentElement ( Consumer<ASTNodeChild> consumer ) {

        forEachElement ( element -> {
            if ( ! ( element instanceof Comment_ASTNodeChild ) ) {
                consumer.accept ( element );
            }
        });
    }

    public void forEachComment ( Consumer<String> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Comment_ASTNodeChild commentChild ) {
                consumer.accept ( commentChild.getText() );
            }
        });
    }


    // Counts

    public int elementCount() { return list.size(); }

    public int nodeElementCount() {

        List<PDMLNodeAST> nodes = nodeElements ();
        return nodes == null ? 0 : nodes.size ();
    }

    public int textElementCount() {

        List<Text_ASTNodeChild> texts = textElements ();
        return texts == null ? 0 : texts.size();
    }

    public int commentElementCount() {

        List<Comment_ASTNodeChild> comments = commentElements ();
        return comments == null ? 0 : comments.size();
    }

    public int nonCommentElementCount() {

        List<ASTNodeChild> nonComments = nonCommentElements();
        return nonComments == null ? 0 : nonComments.size();
    }


    // Has

    public boolean hasElements() { return ! list.isEmpty(); }

    public boolean hasNodeElements() { return nodeElements () != null; }

    public boolean hasTextElements() { return textElements () != null; }

    public boolean hasCommentElements() { return commentElements () != null; }


    // First / Last

    public @Nullable ASTNodeChild firstElement() {

        return list.isEmpty() ? null : list.get ( 0 );
    }

    public @Nullable PDMLNodeAST firstNodeElement() {

        List<PDMLNodeAST> nodes = nodeElements ();
        return nodes == null ? null : nodes.get ( 0 );
    }

    public @Nullable Text_ASTNodeChild firstTextElement() {

        List<Text_ASTNodeChild> texts = textElements ();
        return texts == null ? null : texts.get ( 0 );
    }

    public @Nullable Comment_ASTNodeChild firstCommentElement() {

        List<Comment_ASTNodeChild> comments = commentElements ();
        return comments == null ? null : comments.get ( 0 );
    }

    public @Nullable ASTNodeChild firstNonCommentElement() {

        List<ASTNodeChild> nonComments = nonCommentElements ();
        return nonComments == null ? null : nonComments.get ( 0 );
    }

    public @Nullable ASTNodeChild lastElement() {

        return list.isEmpty() ? null : list.get ( list.size() - 1 );
    }


    public @Nullable List<PDMLNodeAST> nodeElementsByLocalName ( String localName ) {

        List<PDMLNodeAST> result = new ArrayList<>();
        forEachNodeElement ( node -> {
            if ( node.getLocalName().equals ( localName ) ) {
                result.add ( node );
            }
        });

        return result.isEmpty() ? null : result;
    }

    public @Nullable String concatenateTextElements() {

        StringBuilder sb = new StringBuilder();
        forEachText ( sb::append );
        return sb.length() == 0 ? null : sb.toString();
    }

    // append

    public void append ( @NotNull ASTNodeChild child ) {

        list.add ( child );
    }

    // TODO remove
    // TODO replace


    public @NotNull String toString() {

        int size = list.size();
        return size + ( size == 1 ? " child" : " children" );
    }

}

 */
