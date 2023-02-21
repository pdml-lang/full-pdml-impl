package dev.pdml.reader.extensions;

import dev.pp.basics.annotations.NotNull;
import dev.pp.text.inspection.TextErrorException;

import java.io.IOException;

public interface PdmlExtensionsHandler {

    boolean handleExtension ( @NotNull PdmlExtensionsContext context ) throws IOException, TextErrorException;
}
