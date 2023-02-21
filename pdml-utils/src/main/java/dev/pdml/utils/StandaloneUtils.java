package dev.pdml.utils;

import dev.pdml.utils.parser.PdmlParserBuilder;
import dev.pdml.utils.parser.eventhandlers.WriteStandalone_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StandaloneUtils {

    public static void createStandalone (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull Writer writer ) throws IOException, TextErrorException {

        new PdmlParserBuilder<> ( new WriteStandalone_ParserEventHandler ( writer ) )
            .allowAlternativeAttributesStartSyntax ( true )
            .parseReader ( reader, textResource, null, null );
    }
}
