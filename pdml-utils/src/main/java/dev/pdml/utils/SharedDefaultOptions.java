package dev.pdml.utils;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.resource.TextResource;
import dev.pp.texttable.writer.pretty.utilities.TextInspectionMessage_FormWriter;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public class SharedDefaultOptions {

    public static final @NotNull Reader INPUT_READER = OSIO.standardInputUTF8Reader();

    public static final @Nullable TextResource INPUT_TEXT_RESOURCE = null;

    public static final @NotNull Path OUTPUT_DIRECTORY = Path.of ( "output" );

    public static final @NotNull Writer OUTPUT_WRITER = OSIO.standardOutputUTF8Writer();

    public static @NotNull TextInspectionMessageHandler createErrorHandler () {
        return TextInspectionMessage_FormWriter.createLogMessageHandler();
    }

    public static final @NotNull SimpleLogger.LogLevel VERBOSITY = SimpleLogger.LogLevel.INFO;

    public static final @Nullable String OPEN_FILE_OS_COMMAND_TEMPLATE = null;
}

