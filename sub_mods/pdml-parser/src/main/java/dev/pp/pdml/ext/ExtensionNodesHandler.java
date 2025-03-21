package dev.pp.pdml.ext;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

import java.io.IOException;

public interface ExtensionNodesHandler {

    // void handleExtensionNode (
    @Nullable InsertStringExtensionResult handleExtensionNode (
        @NotNull PdmlReader pdmlReader,
        @NotNull PdmlParser pdmlParser ) throws IOException, PdmlException;
}
