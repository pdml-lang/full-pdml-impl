package dev.pdml.core.data.AST.children;

import dev.pdml.core.data.AST.PDMLNodeAST;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ASTNodeChildren {

    private final @NotNull List<ASTNodeChild> list;


    public ASTNodeChildren () {

        this.list = new ArrayList<>();
    }


    // list operations

    public @NotNull Collection<ASTNodeChild> getList() {

        return Collections.unmodifiableList ( list );
    }

    public void forEachElement ( Consumer<ASTNodeChild> consumer ) {

        for ( ASTNodeChild element : list ) {
            consumer.accept ( element );
        }
    }

    public void forEachNodeElement ( Consumer<PDMLNodeAST> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Node_ASTNodeChild ) {
                consumer.accept ( ((Node_ASTNodeChild) element).getNode() );
            }
        });
    }

    public void forEachTextElement ( Consumer<String> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Text_ASTNodeChild ) {
                consumer.accept ( ((Text_ASTNodeChild) element).getText() );
            }
        });
    }

    public void forEachCommentElement ( Consumer<String> consumer ) {

        forEachElement ( element -> {
            if ( element instanceof Comment_ASTNodeChild ) {
                consumer.accept ( ((Comment_ASTNodeChild) element).getText() );
            }
        });
    }


    // queries

    public int elementCount() { return list.size(); }
    /* TODO
    public int nodeElementCount() {  }
    public int textElementCount() {  }
    public int commentElementCount() {  }
    */

    public @Nullable ASTNodeChild firstElement() {

        return list.isEmpty() ? null : list.get ( 0 );
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
        forEachTextElement ( sb::append );
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
