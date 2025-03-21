package dev.pp.pdml.ext;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

public record InsertStringExtensionResult (
    @Nullable String string,
    @NotNull InsertStringFormat format ) {
}
