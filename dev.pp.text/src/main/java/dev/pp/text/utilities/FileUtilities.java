package dev.pp.text.utilities;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.text.TextLines;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtilities {

    public static FileReader getUTF8FileReader ( File file ) throws IOException {

        throwIfFileNotExists ( file );
        throwIfFileIsDirectory ( file );

        return new FileReader ( file, StandardCharsets.UTF_8 );
    }

    public static FileWriter getUTF8FileWriter ( File file ) throws IOException {

        throwIfFileIsDirectory ( file );

        return new FileWriter ( file, StandardCharsets.UTF_8 );
    }

    public static @Nullable File filePathToExistingFile (
        @NotNull Path filePath,
        @NotNull File rootDirectoryForRelativeFile,
        @NotNull TextErrorHandler errorHandler,
        @Nullable TextToken textToken ) {

        File file = filePathToFile ( filePath, rootDirectoryForRelativeFile );

        if ( checkFileExistsAndIsNotDirectory ( file, errorHandler, textToken ) ) {
            return file;
        } else {
            return null;
        }
    }

    public static @NotNull File filePathToFile (
        @NotNull Path filePath,
        @NotNull File rootDirectoryForRelativeFile ) {

        if ( filePath.isAbsolute() ) {
            return filePath.toFile();
        } else {
            // return Path.of ( rootDirectoryForRelativeFile, filePath ).toFile();
            return rootDirectoryForRelativeFile.toPath().resolve ( filePath ).toFile();
        }
    }

    public static @NotNull File filePathToFile (
        @NotNull String filePath,
        @NotNull File rootDirectoryForRelativeFile ) {

        return filePathToFile ( Path.of ( filePath ), rootDirectoryForRelativeFile );
    }

    public static boolean checkFileExistsAndIsNotDirectory (
        @NotNull File file,
        @NotNull TextErrorHandler errorHandler,
        @Nullable TextToken textToken ) {

        return checkFileExists ( file, errorHandler, textToken ) &&
            checkFileIsNotDirectory ( file, errorHandler, textToken );
    }

    public static boolean checkFileExists (
        @NotNull File file,
        @NotNull TextErrorHandler errorHandler,
        @Nullable TextToken textToken ) {

        if ( file.exists() ) {
            return true;
        } else {
            errorHandler.handleError (
                "FILE_DOES_NOT_EXIST",
                fileNotExistsMessage ( file ),
                textToken);
            return false;
        }
    }

    public static void throwIfFileNotExists ( File file ) throws FileNotFoundException {

        if ( ! file.exists() ) {
            throw new FileNotFoundException ( fileNotExistsMessage ( file ) ) {
                public String toString() {
                    return getMessage();
                }
            };
        }
    }

    private static String fileNotExistsMessage ( @NotNull File file ) {
        return "File '" + getAbsoluteOSPath ( file ) + "' does not exist."; }

    public static boolean checkFileIsNotDirectory (
        @NotNull File file,
        @NotNull TextErrorHandler errorHandler,
        @Nullable TextToken textToken ) {

        if ( ! file.isDirectory() ) {
            return true;
        } else {
            errorHandler.handleError (
                "DIRECTORY_NOT_ALLOWED",
                fileIsDirectoryErrorMessage ( file ),
                textToken);
            return false;
        }
    }

    public static void throwIfFileIsDirectory ( File file ) throws IOException {

        if ( file.isDirectory() ) {
            throw new IOException ( fileIsDirectoryErrorMessage ( file ) ) {
                public String toString() {
                    return getMessage();
                }
            };
        }
    }

    private static String fileIsDirectoryErrorMessage ( @NotNull File file ) {
        return "'" + getAbsoluteOSPath( file ) + "' is a directory. But a file is required."; }

    public static String getAbsoluteOSPath ( File file ) {

        try {
            return file.getCanonicalPath();
        } catch ( IOException e ) {
            return file.getAbsolutePath();
        }
    }

    public static @NotNull String getNthLineInFile ( @NotNull File file, long n ) throws IOException {

        try ( BufferedReader br = new BufferedReader ( getUTF8FileReader ( file ) ) ) {
            return TextLines.getNthLineInReader ( br, n );
        }
    }

/*
    public static @Nullable String getLineWithMarkerInFile (
        @NotNull File file, long lineNumber, int columnNumber, @NotNull String marker ) throws IOException {

        String line = getNthLineInFile ( file, lineNumber );
        if ( line == null ) return null;

        if ( columnNumber <= 1 ) {
            return marker + line;
        } else if ( columnNumber > line.length() ) {
            return line + marker;
        } else {
            return line.substring ( 0, columnNumber - 1 ) + marker + line.substring ( columnNumber - 1 );
        }
    }
*/

    public static @Nullable String readTextFromUTF8File ( File file ) throws IOException {

        return Files.readString ( file.toPath() );
    }

    public static @NotNull File createEmptyTempFile ( boolean deleteOnExit ) throws IOException {

        File file = Files.createTempFile ( null, null ).toFile();

        if ( deleteOnExit ) file.deleteOnExit();

        return file;
    }

    public static @NotNull File createNonEmptyTempTextFile (
        @NotNull String content,
        boolean deleteOnExit ) throws IOException {

        File file = createEmptyTempFile ( deleteOnExit );

        try ( FileWriter writer = getUTF8FileWriter ( file ) ) {
            writer.append ( content );
            writer.flush();
        }

        return file;
    }
}
