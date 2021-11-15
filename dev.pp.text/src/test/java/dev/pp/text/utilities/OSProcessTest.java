package dev.pp.text.utilities;

import dev.pp.text.utilities.string.StringConstants;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OSProcessTest {

    @Test
    public void testStartOSCommandAndContinue() throws IOException {

        File file = FileUtilities.createNonEmptyTempTextFile (
        "line 1" + StringConstants.OS_NEW_LINE + "line 2", false );

        // open file in VSCode at line 2, column 4
        String OSCommand = "cmd.exe /c code --goto \"" + file.getCanonicalPath() + ":2:4\"";
        // DebugUtils.printNameValue ( "OSCommand", OSCommand );

        if ( false ) OSProcess.startOSCommandAndContinue ( OSCommand );
    }
}