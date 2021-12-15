package dev.pp.text.utilities;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DirectoryUtilities {

    public static final String DIRECTORY_NAME_EXCLUDE_PREFIX = "---";
    public static final String FILE_NAME_EXCLUDE_PREFIX = "---";

    public static @NotNull File currentWorkingDirectory() {
        return new File ( System.getProperty ( "user.dir" ) ); }

    public static void consumeNonExcludedFilesWithExtensionInTree (
        @NotNull File rootDirectory,
        @NotNull String fileNameExtension,
        @NotNull Consumer<File> fileConsumer ) throws IOException {

        String fileNameEnd = "." + fileNameExtension;

        consumeFileTree (
            rootDirectory,
            fileConsumer,
            directory -> ! directory.getName().startsWith ( DIRECTORY_NAME_EXCLUDE_PREFIX ),
            file -> {
                String fileName = file.getName();
                if ( fileName.startsWith ( FILE_NAME_EXCLUDE_PREFIX ) ) return false;
                return fileName.endsWith ( fileNameEnd );
            } );
    }

    public static void consumeFileTree (
        @NotNull File rootDirectory,
        @NotNull Consumer<File> fileConsumer,
        @Nullable Predicate<File> directoryPredicate,
        @Nullable Predicate<File> filePredicate ) throws IOException {

        Files.walkFileTree ( rootDirectory.toPath(), EnumSet.of ( FileVisitOption.FOLLOW_LINKS ), Integer.MAX_VALUE,
            new SimpleFileVisitor<Path> () {

                @Override
                public FileVisitResult preVisitDirectory ( Path directoryPath, BasicFileAttributes attributes )
                    throws IOException {

                    boolean includeDirectory =
                        directoryPredicate == null || directoryPredicate.test ( directoryPath.toFile() );
                    return includeDirectory ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile ( Path filePath, BasicFileAttributes attributes ) throws IOException {

                    File file = filePath.toFile();
                    boolean includeFile = filePredicate == null || filePredicate.test ( file );
                    if ( includeFile ) fileConsumer.accept ( file );
                    return FileVisitResult.CONTINUE;
                }
            });
    }
}
