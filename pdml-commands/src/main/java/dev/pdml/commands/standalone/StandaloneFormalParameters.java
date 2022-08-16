package dev.pdml.commands.standalone;

import dev.pdml.commands.SharedFormalParameters;
import dev.pp.basics.annotations.NotNull;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.FormalParameters;

import java.nio.file.Path;

public class StandaloneFormalParameters {

    public static final @NotNull FormalParameter<Path> PDML_INPUT_FILE = SharedFormalParameters.positionalInputFileOrNull (
        "PDML Input File",
        "The path of the PDML file to be converted to a standalone file. The path can be absolute or relative.",
        "input/book.pml" );

    public static final @NotNull FormalParameter<Path> PDML_OUTPUT_FILE = SharedFormalParameters.positionalOutputFileOrNull (
        "PDML Output File",
        "The path of the standalone PDML file to be created. The path can be absolute or relative.",
        "output/standalone_book.pml" );

    public static final FormalParameters FORMAL_PARAMETERS = new FormalParameters()
        .add ( PDML_INPUT_FILE )
        .add ( PDML_OUTPUT_FILE );
}
