package dev.pdml.ext.utilities;

import dev.pdml.ext.utilities.parser.PDMLParserBuilder;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pdml.ext.utilities.parser.eventhandlers.WriteStandalone_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StandaloneUtils {

    public static void createStandalone (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull Writer writer ) throws IOException, TextErrorException {

        new PDMLParserBuilder<> ( new WriteStandalone_ParserEventHandler ( writer ) )
            .parseReader ( reader, textResource );
    }
}
