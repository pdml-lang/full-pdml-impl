package dev.pp.pdml.utils;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.SimpleLogger;
import dev.pp.core.basics.utilities.os.OSIO;
import dev.pp.core.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.texttable.writer.pretty.utilities.TextInspectionMessage_FormWriter;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public class SharedDefaultOptions {

    public static final @NotNull Reader INPUT_READER = OSIO.standardInputUTF8Reader();

    public static final @Nullable TextResource INPUT_TEXT_RESOURCE = null;

    public static final @NotNull Path OUTPUT_DIRECTORY = Path.of ( "output" );

    public static final @NotNull Writer OUTPUT_WRITER = OSIO.standardOutputUTF8Writer();

    public static @NotNull TextInspectionMessageHandler createMessageHandler() {
        return TextInspectionMessage_FormWriter.createLogMessageHandler();
    }

    public static final @NotNull SimpleLogger.LogLevel VERBOSITY = SimpleLogger.LogLevel.INFO;

    public static final @Nullable String OPEN_FILE_OS_COMMAND_TEMPLATE = null;
}

