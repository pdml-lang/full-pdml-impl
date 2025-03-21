package dev.pp.pdml.ext;

import dev.pp.pdml.ext.scripting.*;
import dev.pp.pdml.ext.types.PdmlTypes;
import dev.pp.pdml.ext.types.TypeNodeHandler;
import dev.pp.pdml.ext.utils.GetConstantHandler;
import dev.pp.pdml.ext.utils.InsertFileHandler;
import dev.pp.pdml.ext.utils.SetConstantHandler;
import dev.pp.pdml.ext.types.PdmlType;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO add as parameter to parser config (default = STANDARD_HANDLERS)
public class ExtensionNodeHandlers {


    public static final @NotNull ExtensionNodeHandlers STANDARD_HANDLERS =
        new ExtensionNodeHandlers().addStandardHandlers();


    private final @NotNull Map<String, ExtensionNodeHandler> map;


    public ExtensionNodeHandlers () {
        this.map = new HashMap<> ();
    }


    /*
    public @Nullable ExtensionNodeHandlerDelegate getOrNull (
        @NotNull String extensionKind,
        @NotNull String extensionName ) {

        return getOrNull ( ExtensionNodeHandlerDelegate.createIdentifier (
            extensionKind, extensionName ) );
    }
     */

    public @Nullable ExtensionNodeHandler getOrNull ( @NotNull String identifier ) {
        return map.get ( identifier );
    }

    /*
    public @Nullable Collection<ExtensionNodeHandler> getAll() {

        if ( ! map.isEmpty() ) {
            return map.values();
        } else {
            return null;
        }
    }
     */

    public @Nullable PdmlTypes getTypes() {

        PdmlTypes types = new PdmlTypes();
        for ( ExtensionNodeHandler handler : map.values() ) {
            if ( handler instanceof TypeNodeHandler<?> td ) {
                types.add ( td.getType() );
            }
        }

        return types.isEmpty() ? null : types;
    }

    public ExtensionNodeHandlers add ( @NotNull ExtensionNodeHandler handler ) {

        String identifier = handler.getIdentifier();
        if ( map.containsKey ( identifier ) ) {
            throw new IllegalStateException ( "Extension node '" + identifier + "' exists already." );
        }

        map.put ( identifier, handler );
        return this;
    }

    public ExtensionNodeHandlers addStandardHandlers() {

        addStandardUtilityHandlers ();
        addStandardTypesHandlers ();
        addStandardScriptingHandlers ();

        return this;
    }

    public ExtensionNodeHandlers addStandardUtilityHandlers() {

        add ( SetConstantHandler.INSTANCE );
        add ( GetConstantHandler.INSTANCE );
        add ( InsertFileHandler.INSTANCE );

        return this;
    }

    public ExtensionNodeHandlers addStandardTypesHandlers() {

        Collection<PdmlType<?>> types = PdmlTypes.STANDARD_TYPES.getAll();
        if ( types != null ) {
            for ( PdmlType<?> type : types ) {
                add ( new TypeNodeHandler<> ( type ) );
            }
        }

        return this;
    }

    public ExtensionNodeHandlers addStandardScriptingHandlers() {

        // TODO remove
        add ( ExpressionHandler.INSTANCE );
        add ( InsertTextHandler.INSTANCE );
        add ( InsertCodeHandler.INSTANCE );
        add ( ScriptHandler.INSTANCE );
        add ( DefinitionHandler.INSTANCE );

        return this;
    }
}
