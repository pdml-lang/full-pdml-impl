package dev.pdml.commands.scriptingapidoc;

import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.parameters.parameterspecs.ParameterSpecs;

import java.nio.file.Path;

public class ScriptingAPIDocParameters {

    public static final @NotNull ParameterSpec<Path> SOURCE_DIRECTORY = ParameterSpec.builder (
        "sources_dir", CommonDataTypes.DIRECTORY_PATH )
        .alternativeName ( "s" )
        .documentation ( "Source Code Directory", "Directory of the source code files.", null )
        .build();

    public static final @NotNull ParameterSpec<Path> OUTPUT_FILE = ParameterSpec.builder (
        "output", CommonDataTypes.FILE_PATH )
        .alternativeName ( "o" )
        .documentation ( "Output File", null, null )
        .build();

    public static final ParameterSpecs<Path> SPECS = new MutableParameterSpecs<Path>()
        .add ( SOURCE_DIRECTORY )
        .add ( OUTPUT_FILE )
        .makeImmutable();
}
