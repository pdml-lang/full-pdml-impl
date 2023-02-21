package dev.pdml.ext;

import dev.pdml.extscripting.Definitions_ExtensionHandler;
import dev.pdml.extscripting.Expression_ExtensionHandler;
import dev.pdml.extscripting.Script_ExtensionHandler;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.exttypes.handlers.DateType_ExtensionHandler;
import dev.pdml.exttypes.handlers.RawTextBlockType_ExtensionHandler;
import dev.pdml.exttypes.handlers.StringType_ExtensionHandler;
import dev.pdml.extutils.GetConstant_ExtensionHandler;
import dev.pdml.extutils.InsertFile_ExtensionHandler;
import dev.pdml.extutils.SetConstant_ExtensionHandler;
import dev.pp.basics.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class StandardExtensionsHandlers {


    protected static final @NotNull Map<String, PdmlExtensionNodeHandler> MAP = createMap();


    private static Map<String, PdmlExtensionNodeHandler> createMap() {

        Map<String, PdmlExtensionNodeHandler> map = new HashMap<>();

        // utilities
        add ( SetConstant_ExtensionHandler.INSTANCE, map );
        add ( GetConstant_ExtensionHandler.INSTANCE, map );
        add ( InsertFile_ExtensionHandler.INSTANCE, map );

        // types
        add ( DateType_ExtensionHandler.INSTANCE, map );
        add ( RawTextBlockType_ExtensionHandler.INSTANCE, map );
        add ( StringType_ExtensionHandler.INSTANCE, map );

        // scripting
        add ( Expression_ExtensionHandler.INSTANCE, map );
        add ( Script_ExtensionHandler.INSTANCE, map );
        add ( Definitions_ExtensionHandler.INSTANCE, map );

        return map;
    }

    private static void add ( PdmlExtensionNodeHandler handler, Map<String, PdmlExtensionNodeHandler> map ) {
        map.put ( handler.getQualifiedNodeName(), handler );
    }


    private StandardExtensionsHandlers() {}
}
