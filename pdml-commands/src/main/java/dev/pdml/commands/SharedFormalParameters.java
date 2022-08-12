package dev.pdml.commands;

import dev.pdml.ext.commands.SharedDefaultOptions;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.SimpleLogger;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.datatype.nonUnion.scalar.impls.Enum.EnumDataType;
import dev.pp.parameters.formalParameter.FormalParameter;


import java.nio.file.Path;

public class SharedFormalParameters {

    public static @NotNull FormalParameter<Path> positionalInputFileOrNull (
        @NotNull String title,
        @NotNull String description,
        @Nullable String examples ) {

        return FormalParameter.builder (
            "input", CommonDataTypes.FILE_PATH_OR_NULL )
            .alternativeName ( "i" )
            .defaultValue ( null )
            .positionalParameterIndex ( 1 )
            .documentation ( title, description, examples )
            .build();
    }

    public static @NotNull FormalParameter<Path> positionalOutputFileOrNull (
        @NotNull String title,
        @NotNull String description,
        @Nullable String examples ) {

        return FormalParameter.builder (
            "output", CommonDataTypes.FILE_PATH_OR_NULL )
            .alternativeName ( "o" )
            .defaultValue ( null )
            .positionalParameterIndex ( 2 )
            .documentation ( title, description, examples )
            .build();
    }

    public static @NotNull FormalParameter<Path> outputFileOrNull (
        @NotNull String title,
        @NotNull String description,
        @Nullable String examples ) {

        return FormalParameter.builder (
            "output", CommonDataTypes.FILE_PATH_OR_NULL )
            .alternativeName ( "o" )
            .defaultValue ( null )
            .documentation ( title, description, examples )
            .build ();
    }

    private static final @NotNull EnumDataType<SimpleLogger.LogLevel> VERBOSITY_TYPE =
        new EnumDataType<> ( SimpleLogger.LogLevel.class );

    public static final @NotNull FormalParameter<SimpleLogger.LogLevel> VERBOSITY = new FormalParameter.Builder<> (
        "verbosity", VERBOSITY_TYPE )
        .alternativeName ( "vb" )
        .defaultValue ( SharedDefaultOptions.VERBOSITY )
        .documentation ( "Verbosity",
            """
                The level of verbosity.
                Defines which kinds of messages are displayed when the command is executed.
                Valid values are: """ +
                VERBOSITY_TYPE.validValuesAsString().toLowerCase(),
            "all" )
        .build();

    public static final @NotNull FormalParameter<String> OPEN_FILE_OS_COMMAND_TEMPLATE = new FormalParameter.Builder<> (
        "open_file_cmd", CommonDataTypes.STRING_OR_NULL )
        .alternativeName ( "ofc" )
        .defaultValue ( null )
        .documentation ( "Open File OS Command",
            """
            This parameter specifies an OS command template to open an editor for the first file in which an error was detected.
            The following placeholders can be used:
            [[file]]
                the full path of the file in which the error was detected
            [[line]]
                the line number of the error (first line = 1)
            [[column]]
                the column number of the error (first column = 1)

            Note:
            Be careful to correctly quote file paths that include spaces.
            Different OS environments have different rules. Please consult your OS documentation for further information.
            """,
            """
            // open VSCode in Windows:
            --open_file_cmd "cmd.exe /c code --goto \\"[[file]]:[[line]]:[[column]]\\""
            // alternative (replace {username} with the name on your machine):
            --open_file_cmd "\\"C:\\Users\\{username}\\AppData\\Local\\Programs\\Microsoft VS Code\\Code.exe\\" --goto \\"[[file]]:[[line]]:[[column]]\\""

            // open Sublime Text in Windows (subl must be on the OS path)
            --open_file_cmd "cmd.exe /c subl \\"[[file]]:[[line]]:[[column]]\\""
            """ )
        .build();
}
