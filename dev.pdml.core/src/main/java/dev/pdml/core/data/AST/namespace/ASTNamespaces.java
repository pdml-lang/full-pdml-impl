package dev.pdml.core.data.AST.namespace;

import dev.pdml.core.PDMLConstants;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ASTNamespaces {

    // TODO default namespace

    public static ASTNamespace getInvalidNamespace ( @NotNull TextToken prefix, @Nullable TextLocation location ) {

        return new ASTNamespace (
            prefix,
            new TextToken ( PDMLConstants.UNKNOWN_URI.toString(), location ),
            PDMLConstants.UNKNOWN_URI );
    }


    private final @NotNull Map<String, ASTNamespace> map;


    public ASTNamespaces () {

        this.map = new LinkedHashMap<>();
    }


    public boolean isEmpty() { return map.isEmpty(); }

    public boolean containsPrefix ( String prefix ) { return map.containsKey ( prefix ); }

    public @Nullable ASTNamespace get ( String prefix ) { return map.get ( prefix ); }

    public void add ( @NotNull ASTNamespace namespace ) {

        if ( containsPrefix ( namespace.getPrefixText() ) ) throw new IllegalArgumentException (
            "Namespace '" + namespace.getPrefixText() + "' exists already." );

        addOrReplace ( namespace );
    }

    public void addOrReplace ( @NotNull ASTNamespace namespace ) {

        map.put ( namespace.getPrefixText(), namespace );
    }

    public void remove ( @NotNull ASTNamespace namespace ) {

        map.remove ( namespace.getPrefixText() );
    }

    public @NotNull Collection<ASTNamespace> getList() {

        return map.values();
    }

    public @NotNull String toString() { return map.size() + "namespace(s)"; }
}
