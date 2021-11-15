package dev.pp.datatype.parser;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

public interface DataParser<T> {

    // TODO  @NotNull T parse ( @NotNull Reader reader string, @Nullable TextToken token ) throws DataParserException;
    @NotNull T parse ( @NotNull String string, @Nullable TextToken token ) throws DataParserException;
}
