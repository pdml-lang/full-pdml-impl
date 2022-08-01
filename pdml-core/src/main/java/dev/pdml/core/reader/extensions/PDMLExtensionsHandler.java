package dev.pdml.core.reader.extensions;

import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.utils.validator.DataValidatorException;
import dev.pp.text.error.TextErrorException;

import java.io.IOException;

public interface PDMLExtensionsHandler {

    boolean handleExtension ( @NotNull PDMLExtensionsContext context )
        throws IOException, TextErrorException;
}
