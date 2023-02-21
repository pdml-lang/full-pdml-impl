package dev.pdml.parser.eventhandler;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

public interface PdmlParserEventHandler<N, R> {

    // TODO ? throw PdmlParserEventsHandlerException, instead of Exception

    void onStart() throws Exception;
    void onEnd () throws Exception;

    @Nullable N onRootNodeStart ( @NotNull NodeStartEvent event ) throws Exception;
    void onRootNodeEnd ( @NotNull NodeEndEvent event, @Nullable N rootNode ) throws Exception;

    @Nullable N onNodeStart ( @NotNull NodeStartEvent event, @Nullable N parentNode ) throws Exception;
    void onNodeEnd ( @NotNull NodeEndEvent event, @Nullable N node ) throws Exception;

    void onText ( @NotNull NodeTextEvent event, @Nullable N node ) throws Exception;

    // [- and -] is included in comment
    void onComment ( @NotNull NodeCommentEvent event, @Nullable N node ) throws Exception;

    @Nullable R getResult() throws Exception;
}
