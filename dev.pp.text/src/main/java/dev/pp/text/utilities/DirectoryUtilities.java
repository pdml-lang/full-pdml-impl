package dev.pp.text.utilities;

import dev.pp.text.annotations.NotNull;

import java.io.File;

public class DirectoryUtilities {

    public static @NotNull File currentWorkingDirectory() {
        return new File ( System.getProperty ( "user.dir" ) ); }
}
