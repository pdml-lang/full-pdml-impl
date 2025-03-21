package dev.pp.pdml.utils;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.utils.treewalker.PdmlCodeWalker;
import dev.pp.pdml.utils.treewalker.handler.impl.WriteStandalone_ParserEventHandler;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

@Deprecated
public class StandaloneUtil {

    public static void createStandalone (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull Writer writer ) throws IOException, PdmlException {

        /*
        new PdmlParserBuilder<> ( new WriteStandalone_ParserEventHandler ( writer ) )
            .allowAlternativeAttributesStartSyntax ( true )
            .parseReader ( reader, textResource, null, null );
         */
        // PdmlParser parser = new PdmlParser ( reader, textResource, parserConfig );
        PdmlCodeWalker<NodeTag, Void> parser = new PdmlCodeWalker<> (
            reader, textResource, parserConfig, new WriteStandalone_ParserEventHandler ( writer ) );
        parser.walk ();
    }
}
