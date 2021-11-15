package dev.pp.text.utilities;

import dev.pp.text.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class OSProcess {

    // Thanks to https://www.baeldung.com/run-shell-command-in-java
    private static class InputStreamGobbler implements Runnable {

        private final @NotNull InputStream inputStream;
        private final @NotNull Consumer<String> lineConsumer;

        public InputStreamGobbler (
            final @NotNull InputStream inputStream,
            final @NotNull Consumer<String> consumer ) {

            this.inputStream = inputStream;
            this.lineConsumer = consumer;
        }

        public void consume() {
            new BufferedReader ( new InputStreamReader ( inputStream ) )
                .lines()
                .forEach ( lineConsumer );
        }

        @Override
        public void run() {
            consume();
        }
    }

    public static void startOSCommandAndContinue ( @NotNull String OSCommand ) throws IOException {

        startProcess ( OSCommand );
    }

    public static int runOSCommandAndWait ( @NotNull String OSCommand ) throws IOException, InterruptedException {

        Process process = startProcess ( OSCommand );
        return process.waitFor();
    }

    private static Process startProcess ( @NotNull String OSCommand ) throws IOException {

        // DebugUtils.printNameValue ( "OSCommand", OSCommand );
        Process process = Runtime.getRuntime().exec ( OSCommand );
        redirectStreams ( process );
        return process;
    }

    private static void redirectStreams ( Process process ) {

        InputStreamGobbler outGobbler = new InputStreamGobbler ( process.getInputStream(), System.out::println );
        InputStreamGobbler errGobbler = new InputStreamGobbler ( process.getErrorStream(), System.err::println );

        Executors.newSingleThreadExecutor().submit ( outGobbler );
        Executors.newSingleThreadExecutor().submit ( errGobbler );
    }
}
