package dev.pdml.commands.pdmltoxml;

import dev.pdml.commands.SharedParameterSpecs;
import dev.pp.basics.annotations.NotNull;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.parameters.parameterspecs.ParameterSpecs;

import java.nio.file.Path;

public class PdmlToXMLParameters {

    public static final @NotNull ParameterSpec<Path> PDML_INPUT_FILE = SharedParameterSpecs.positionalInputFileOrNull (
        "PDML Input File",
        "The path of the PDML file to be converted to XML. The path can be absolute or relative.",
        "text/input/index.pml" );

    public static final @NotNull ParameterSpec<Path> XML_OUTPUT_FILE = SharedParameterSpecs.positionalOutputFileOrNull (
        "XML Output File",
        "The path of the XML file to be created. The path can be absolute or relative.",
        "output/index.xml" );

    public static final @NotNull ParameterSpecs<Path> SPECS = new MutableParameterSpecs<Path>()
        .add ( PDML_INPUT_FILE )
        .add ( XML_OUTPUT_FILE )
        .makeImmutable();
}
