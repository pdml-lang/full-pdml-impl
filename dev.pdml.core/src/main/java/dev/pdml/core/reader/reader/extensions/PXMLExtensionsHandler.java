package dev.pdml.core.reader.reader.extensions;

import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.annotations.NotNull;

public interface PXMLExtensionsHandler {

    boolean handleExtension ( @NotNull ExtensionsContext context ) throws TextReaderException;
}
