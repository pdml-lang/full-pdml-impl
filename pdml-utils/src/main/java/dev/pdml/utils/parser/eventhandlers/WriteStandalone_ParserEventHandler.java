package dev.pdml.utils.parser.eventhandlers;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.eventhandler.*;
import dev.pdml.writer.PdmlWriter;
import dev.pdml.writer.data.PdmlDataWriter;
import dev.pp.basics.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

public class WriteStandalone_ParserEventHandler implements PdmlParserEventHandler<NodeName, NodeName> {

    private final PdmlDataWriter writer;
    private final PdmlWriter basicWriter;

    private NodeName rootNodeName;


    public WriteStandalone_ParserEventHandler ( PdmlDataWriter writer ) {

        this.writer = writer;
        this.basicWriter = writer.getWriter();
        this.rootNodeName = null;
    }

    public WriteStandalone_ParserEventHandler ( Writer writer ) {

        this ( new PdmlDataWriter ( writer ) );
    }


    public void onStart() {}

    public void onEnd () {}

    public @NotNull NodeName onRootNodeStart ( @NotNull NodeStartEvent event ) throws IOException {

        rootNodeName = event.name();
        return onNodeStart_ ( event );
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull NodeName rootNode ) throws IOException {
        onNodeEnd ( event, rootNode );
    }

    public @NotNull NodeName onNodeStart (
        @NotNull NodeStartEvent event, @NotNull NodeName parentName ) throws IOException {

        return onNodeStart_ ( event );
    }

    private @NotNull NodeName onNodeStart_ ( @NotNull NodeStartEvent event ) throws IOException {

        NodeName name = event.name();
        writer.writeNodeStart ( event.name(), event.declaredNamespaces(), event.attributes(), event.isEmptyNode() );
        return name;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull NodeName name ) throws IOException {
        // if ( ! event.isEmptyNode() ) writer.getWriter().writeNodeEndSymbol();
        basicWriter.writeNodeEndSymbol();
    }

    public void onText ( @NotNull NodeTextEvent event, @NotNull NodeName parentNode ) throws IOException {
        basicWriter.writeText ( event.text(), true );
    }

    public void onComment ( @NotNull NodeCommentEvent event, @NotNull NodeName parentNode ) throws IOException {
        basicWriter.writeComment ( event.comment() );
    }

    public @NotNull NodeName getResult() { return rootNodeName; }
}
