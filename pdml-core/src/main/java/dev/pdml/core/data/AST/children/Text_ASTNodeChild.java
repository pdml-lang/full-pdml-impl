package dev.pdml.core.data.AST.children;

import dev.pp.basics.annotations.NotNull;
import dev.pp.text.token.TextToken;

public class Text_ASTNodeChild extends ASTNodeChild {

    private final @NotNull TextToken textToken;


    public Text_ASTNodeChild ( @NotNull TextToken textToken ) {

        this.textToken = textToken;
    }


    public @NotNull TextToken getTextToken() { return textToken; }

    public @NotNull String getText() { return textToken.getText(); }


    public @NotNull String toString() { return textToken.toString(); }
}
