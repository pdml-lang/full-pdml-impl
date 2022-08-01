package dev.pdml.core.data.AST.namespace;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

public interface ASTNamespaceGetter {

    @Nullable ASTNamespace getByPrefix ( @NotNull String prefix );
}
