package dev.pdml.commands.pdmltoxml;

import dev.pdml.commands.SharedFormalParameters;
import dev.pp.basics.annotations.NotNull;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;

import java.nio.file.Path;

public class PDMLToXMLFormalParameters {

    public static final @NotNull FormalParameter<Path> PDML_INPUT_FILE = SharedFormalParameters.positionalInputFileOrNull (
        "PDML Input File",
        "The path of the PDML file to be converted to XML. The path can be absolute or relative.",
        "text/input/index.pml" );

    public static final @NotNull FormalParameter<Path> XML_OUTPUT_FILE = SharedFormalParameters.positionalOutputFileOrNull (
        "XML Output File",
        "The path of the XML file to be created. The path can be absolute or relative.",
        "output/index.xml" );

    public static final FormalParameters FORMAL_PARAMETERS = new FormalParameters()
        .add ( PDML_INPUT_FILE )
        .add ( XML_OUTPUT_FILE );
}
