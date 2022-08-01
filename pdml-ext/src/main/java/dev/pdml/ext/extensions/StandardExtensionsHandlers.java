package dev.pdml.ext.extensions;

import dev.pdml.ext.extensions.scripting.Definitions_ExtensionHandler;
import dev.pdml.ext.extensions.scripting.Expression_ExtensionHandler;
import dev.pdml.ext.extensions.scripting.Script_ExtensionHandler;
import dev.pdml.ext.extensions.types.handlers.DateType_ExtensionHandler;
import dev.pdml.ext.extensions.utilities.InsertFile_ExtensionHandler;
import dev.pdml.ext.extensions.utilities.SetConstant_ExtensionHandler;
import dev.pdml.ext.extensions.utilities.GetConstant_ExtensionHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pdml.ext.extensions.types.handlers.RawTextBlockType_ExtensionHandler;
import dev.pdml.ext.extensions.types.handlers.StringType_ExtensionHandler;

import java.util.HashMap;
import java.util.Map;

public class StandardExtensionsHandlers {


    public static final @NotNull Map<String, PDMLExtensionNodeHandler> MAP = createMap();


    private static Map<String, PDMLExtensionNodeHandler> createMap() {

        Map<String, PDMLExtensionNodeHandler> map = new HashMap<>();

        // utilities
        addDeprecated ( SetConstant_ExtensionHandler.INSTANCE, map );
        addDeprecated ( GetConstant_ExtensionHandler.INSTANCE, map );
        add ( InsertFile_ExtensionHandler.INSTANCE, map );
        // addDeprecated ( DeprecatedInsertFile_ExtensionHandler.INSTANCE, map );

        // types
        add ( DateType_ExtensionHandler.INSTANCE, map );
        add ( RawTextBlockType_ExtensionHandler.INSTANCE, map );
        add ( StringType_ExtensionHandler.INSTANCE, map );

        // scripting
        addDeprecated ( Expression_ExtensionHandler.INSTANCE, map );
        addDeprecated ( Script_ExtensionHandler.INSTANCE, map );
        addDeprecated ( Definitions_ExtensionHandler.INSTANCE, map );

        return map;
    }

    // TODO remove when no more used in PML
    @Deprecated
    private static void addDeprecated ( PDMLExtensionNodeHandler handler, Map<String, PDMLExtensionNodeHandler> map ) {

        add ( handler, map );

        map.put ( handler.getLocalNodeName(), handler );
    }

    private static void add ( PDMLExtensionNodeHandler handler, Map<String, PDMLExtensionNodeHandler> map ) {

        map.put ( handler.getQualifiedNodeName().toString(), handler );
    }


    private StandardExtensionsHandlers() {}
}
