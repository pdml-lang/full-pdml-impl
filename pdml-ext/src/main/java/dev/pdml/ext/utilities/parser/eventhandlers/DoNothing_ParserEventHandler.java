package dev.pdml.ext.utilities.parser.eventhandlers;

import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;

public class DoNothing_ParserEventHandler implements PDMLParserEventHandler<NodeStartEvent, String> {


    public DoNothing_ParserEventHandler() {}


    public void onStart() {}

    public void onEnd () {}

    public @NotNull NodeStartEvent onRootNodeStart ( @NotNull NodeStartEvent event ) { return event; }

    public void onRootNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {}

    public @NotNull NodeStartEvent onNodeStart ( @NotNull NodeStartEvent start, @NotNull NodeStartEvent parent ) { return start; }

    public void onNodeEnd ( @NotNull NodeEndEvent end, @NotNull NodeStartEvent start ) {}

    public void onAttributes ( @Nullable ASTNodeAttributes attributes, @NotNull NodeStartEvent parent ) {}

    public void onText ( @NotNull TextToken text, @NotNull NodeStartEvent start ) {}

    public void onComment ( @NotNull TextToken comment, @NotNull NodeStartEvent start ) {}

    public @NotNull String getResult() { return "nothing"; }
}
