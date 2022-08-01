package dev.pdml.commands.pdmltoxml;

import dev.pdml.ext.commands.SharedDefaultOptions;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public record PDMLToXMLConfig(
    @NotNull Reader PMLInputReader,
    @Nullable TextResource PMLInputTextResource,
    @Nullable Path XMLOutputDirectory,
    @NotNull Writer XMLOutputWriter,
    @NotNull TextErrorHandler errorHandler,
    @NotNull SimpleLogger.LogLevel verbosity,
    @Nullable String openFileOSCommandTemplate ) {


    public static @NotNull Builder builder() { return new Builder(); }


    public static class Builder {

        private @NotNull Reader PMLInputReader = SharedDefaultOptions.INPUT_READER;

        private @Nullable TextResource PMLInputTextResource = SharedDefaultOptions.INPUT_TEXT_RESOURCE;

        private @Nullable Path XMLOutputDirectory = SharedDefaultOptions.OUTPUT_DIRECTORY;

        private @NotNull Writer XMLOutputWriter = SharedDefaultOptions.OUTPUT_WRITER;

        private @NotNull TextErrorHandler errorHandler = SharedDefaultOptions.createErrorHandler ();

        private @NotNull SimpleLogger.LogLevel verbosity = SharedDefaultOptions.VERBOSITY;

        private @Nullable String openFileOSCommandTemplate = SharedDefaultOptions.OPEN_FILE_OS_COMMAND_TEMPLATE;


        public Builder() {}


        public Builder PMLInputReader ( @NotNull Reader reader ) {

            this.PMLInputReader = reader;
            this.PMLInputTextResource = null;
            return this;
        }

        public Builder PMLInputFile ( @NotNull Path path ) throws IOException {

            this.PMLInputReader = TextFileIO.getUTF8FileReader ( path );
            this.PMLInputTextResource = new File_TextResource ( path );
            return this;
        }

        public Builder XMLOutputDirectory ( @Nullable Path path ) {

            this.XMLOutputDirectory = path;
            return this;
        }

        public Builder XMLOutputFile ( @NotNull Path path ) throws IOException {

            this.XMLOutputDirectory = path.getParent();
            this.XMLOutputWriter = TextFileIO.getUTF8FileWriter ( path );
            return this;
        }

        public Builder XMLOutputWriter ( @NotNull Writer writer ) {

            this.XMLOutputWriter = writer;
            return this;
        }

        public Builder errorHandler ( @NotNull TextErrorHandler errorHandler ) {

            this.errorHandler = errorHandler;
            return this;
        }

        public Builder verbosity ( @NotNull SimpleLogger.LogLevel verbosity ) {

            this.verbosity = verbosity;
            return this;
        }

        public Builder openFileOSCommandTemplate ( @Nullable String openFileOSCommandTemplate ) {

            this.openFileOSCommandTemplate = openFileOSCommandTemplate;
            return this;
        }


        public @NotNull PDMLToXMLConfig build() {

            return new PDMLToXMLConfig (
                PMLInputReader,
                PMLInputTextResource,
                XMLOutputDirectory,
                XMLOutputWriter,
                errorHandler,
                verbosity,
                openFileOSCommandTemplate );
        }
    }
}

