package dev.pp.pdml.companion.commands;

import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.parameters.parameterspec.CommonParameterSpecs;
import dev.pp.core.parameters.parameterspec.ParameterSpec;

import java.nio.file.Path;
import java.util.List;

public class SharedParameterSpecs {

    public static final @NotNull ParameterSpec<Path> OPTIONAL_PDML_INPUT_FILE =
        CommonParameterSpecs.optionalInputFile (
            "PDML Input File",
            "The path of the PDML input file.\n" + CommonParameterSpecs.OPTIONAL_INPUT_FILE_STDIN_DESCR,
            "-i input/data.pdml" );

    public static final @NotNull ParameterSpec<List<Path>> OPTIONAL_PDML_INPUT_FILES =
        CommonParameterSpecs.optionalInputFiles (
            "PDML Input Files",
            "The paths of one or more PDML input file, separated by a comma.\n" + CommonParameterSpecs.OPTIONAL_INPUT_FILE_STDIN_DESCR,
            """
                -i input/data.pdml
                -i dir1/data1.pdml,dir2/data2.pdml""" );

    public static final @NotNull ParameterSpec<Path> OPTIONAL_PDML_OUTPUT_FILE =
        CommonParameterSpecs.optionalOutputFile (
            "PDML Output File",
            "The path of the PDML output file.\n" + CommonParameterSpecs.OPTIONAL_OUTPUT_FILE_STDOUT_DESCR,
            "-o output/doc.pdml" );

    public static final @NotNull ParameterSpec<Path> OPTIONAL_TEXT_OUTPUT_FILE =
        CommonParameterSpecs.optionalOutputFile (
            "Text Output File",
            "The path of the text output file.\n" + CommonParameterSpecs.OPTIONAL_OUTPUT_FILE_STDOUT_DESCR,
            "-o output/result.txt" );
}
