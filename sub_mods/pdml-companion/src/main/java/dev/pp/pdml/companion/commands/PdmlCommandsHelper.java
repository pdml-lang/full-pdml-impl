package dev.pp.pdml.companion.commands;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.text.resource.writer.TextResourceWriter;

import java.nio.file.Path;

public class PdmlCommandsHelper {

    public static void fileCreatedMessageToStdout (
        @NotNull TextResourceWriter textResourceWriter ) {

        Path filePath = textResourceWriter.getResourceAsFilePath();
        if ( filePath != null ) {
            System.out.println ( "The following file has been created:\n" +
                filePath.normalize().toAbsolutePath() );
        }
    }
}
