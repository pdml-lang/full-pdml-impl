package dev.pdml.core.data.AST.children;

import dev.pp.text.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ASTNodeChildren {

    private final @NotNull List<ASTNodeChild> list;


    public ASTNodeChildren () {

        this.list = new ArrayList<>();
    }


    public void append ( @NotNull ASTNodeChild child ) {

        list.add ( child );
    }

    public @NotNull Collection<ASTNodeChild> getList() {

        return Collections.unmodifiableList ( list );
    }

    public @NotNull String toString() {

        int size = list.size();
        return size + ( size == 1 ? " child" : " children" );
    }

}
