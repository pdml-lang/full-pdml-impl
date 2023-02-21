package dev.pdml.utils.parser.eventhandlers;

import dev.pdml.parser.eventhandler.*;
import dev.pp.basics.annotations.NotNull;

public class DoNothing_ParserEventHandler implements PdmlParserEventHandler<NodeStartEvent, String> {


    public DoNothing_ParserEventHandler() {}


    public void onStart() {}

    public void onEnd () {}

    public @NotNull NodeStartEvent onRootNodeStart ( @NotNull NodeStartEvent event ) { return event; }

    public void onRootNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {}

    public @NotNull NodeStartEvent onNodeStart ( @NotNull NodeStartEvent start, @NotNull NodeStartEvent parent ) { return start; }

    public void onNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {}

    public void onText ( @NotNull NodeTextEvent text, @NotNull NodeStartEvent start ) {}

    public void onComment ( @NotNull NodeCommentEvent comment, @NotNull NodeStartEvent start ) {}

    public @NotNull String getResult() { return "nothing"; }
}
