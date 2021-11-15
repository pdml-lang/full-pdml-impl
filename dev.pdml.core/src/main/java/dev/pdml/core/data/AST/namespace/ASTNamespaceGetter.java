package dev.pdml.core.data.AST.namespace;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

public interface ASTNamespaceGetter {

    @Nullable ASTNamespace getByPrefix ( @NotNull String prefix );
}
