package dev.pdml.data.node.branch;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class MutableChildNodes {


    private final @NotNull List<MutableChildNode> list;


    public MutableChildNodes () {
        this.list = new ArrayList<>();
    }


    // Queries

    public boolean isEmpty() { return list.isEmpty(); }

    public boolean isNotEmpty() { return ! list.isEmpty(); }

    public long count() { return list.size(); }

    public void forEachChild ( Consumer<MutableChildNode> consumer ) {

        for ( MutableChildNode childNode : list ) {
            consumer.accept ( childNode );
        }
    }

    public @NotNull Iterator<MutableChildNode> iterator() { return list.iterator(); }

    public @Nullable List<MutableChildNode> list() { return list.isEmpty() ? null : Collections.unmodifiableList ( list ); }

    public @NotNull MutableChildNode firstNode() { return list.get ( 0 ); }

    public @NotNull MutableChildNode lastNode() { return list.get ( list.size() - 1 ); }

    public @NotNull MutableChildNode nodeAtIndex ( long index ) { return list.get ( (int) index ); }



    /* TODO
    public boolean containsNode ( @NotNull NonRootNode node ) {}
    public @Nullable Long indexOf ( NonRootNode node ) {}
    public @Nullable NonRootNode previousSiblingOf ( NonRootNode node ) {}
    public @Nullable NonRootNode nextSiblingOf ( NonRootNode node ) {}
     */


    // Add

    public void append ( @NotNull MutableRootOrBranchNode parentNode, @NotNull MutableChildNode childNode ) {

        childNode.setParent ( parentNode );
        list.add ( childNode );
    }

    /*TODO
    public void prepend ( @NotNull RootOrChildNode parentNode, @NotNull NonRootNode childNode ) {}
    public void insertBeforeIndex ( @NotNull RootOrChildNode parentNode, @NotNull NonRootNode childNode, long index ) {}
    public void insertAfterIndex ( @NotNull RootOrChildNode parentNode, @NotNull NonRootNode childNode, long index ) {}
     */


    // Remove
    /* TODO
    public void removeAtIndex ( long index ) {}
    public void removeFirst() {}
    public void removeLast() {}
     */


    // Replace
    /* TODO
    public void replaceAtIndex ( long index, @NonNull @NotNull NonRootNode newNode ) {}
    public void replaceFirst() {}
    public void replaceLast() {}
     */
}
