package dev.pdml.ext.extensions;

import dev.pdml.core.data.AST.namespace.ASTNamespaceGetter;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pp.basics.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class ExtensionsNamespaces {

    public static final Namespace SCRIPT_NAMESPACE = new Namespace (
        "s", createURI ( "https://www.pdml-lang.dev/extensions/scripting" ) );
    public static final Namespace TYPE_NAMESPACE = new Namespace (
        "t", createURI ( "https://www.pdml-lang.dev/extensions/types" ) );
    public static final Namespace UTILITY_NAMESPACE = new Namespace (
        "u", createURI ( "https://www.pdml-lang.dev/extensions/utilities" ) );

    public static final ASTNamespaces EXTENSION_NAMESPACES = new ASTNamespaces()
        .add ( SCRIPT_NAMESPACE )
        .add ( TYPE_NAMESPACE )
        .add ( UTILITY_NAMESPACE );

    public static final ASTNamespaceGetter EXTENSION_NAMESPACE_GETTER = EXTENSION_NAMESPACES::get;

    private static @NotNull URI createURI ( @NotNull String uri ) {
        try {
            return new URI ( uri );
        } catch ( URISyntaxException e ) {
            throw new RuntimeException ( e );
        }
    }
}
