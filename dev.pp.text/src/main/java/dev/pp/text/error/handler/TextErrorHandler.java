package dev.pp.text.error.handler;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.TextError;
import dev.pp.text.error.TextWarning;
import dev.pp.text.token.TextToken;

public interface TextErrorHandler {

//    void start();
    
//    void stop();

    void handleError ( @NotNull TextError error );

    void handleError ( @Nullable String id, @NotNull String message, @Nullable TextToken token );

    void handleWarning ( @NotNull TextWarning warning );

    void handleWarning ( @Nullable String id, @NotNull String message, @Nullable TextToken token );

    @Nullable TextError firstError();

    /*
    setMark()
    @Nullable TextError firstErrorAfterMark()
    */
}
