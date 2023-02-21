package dev.pdml.commands.standalone;

import dev.pdml.commands.SharedParameterSpecs;
import dev.pp.basics.annotations.NotNull;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.parameters.parameterspecs.ParameterSpecs;

import java.nio.file.Path;

public class StandaloneParameters {

    public static final @NotNull ParameterSpec<Path> PDML_INPUT_FILE = SharedParameterSpecs.positionalInputFileOrNull (
        "PDML Input File",
        "The path of the PDML file to be converted to a standalone file. The path can be absolute or relative.",
        "input/book.pml" );

    public static final @NotNull ParameterSpec<Path> PDML_OUTPUT_FILE = SharedParameterSpecs.positionalOutputFileOrNull (
        "PDML Output File",
        "The path of the standalone PDML file to be created. The path can be absolute or relative.",
        "output/standalone_book.pml" );

    public static final @NotNull ParameterSpecs<Path> SPECS = new MutableParameterSpecs<Path>()
        .add ( PDML_INPUT_FILE )
        .add ( PDML_OUTPUT_FILE )
        .makeImmutable();
}
