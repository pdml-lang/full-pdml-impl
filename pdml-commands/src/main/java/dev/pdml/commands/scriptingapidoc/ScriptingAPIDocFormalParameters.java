package dev.pdml.commands.scriptingapidoc;

import dev.pp.basics.annotations.NotNull;
import dev.pp.datatype.CommonDataTypes;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.FormalParameters;

import java.nio.file.Path;

public class ScriptingAPIDocFormalParameters {

    public static final @NotNull FormalParameter<Path> SOURCE_DIRECTORY = FormalParameter.builder (
        "sources_dir", CommonDataTypes.DIRECTORY_PATH )
        .alternativeName ( "s" )
        .documentation ( "Source Code Directory", "Directory of the source code files.", null )
        .build();

    public static final @NotNull FormalParameter<Path> OUTPUT_FILE = FormalParameter.builder (
        "output", CommonDataTypes.FILE_PATH )
        .alternativeName ( "o" )
        .documentation ( "Output File", null, null )
        .build();

    public static final FormalParameters PARAMETERS = new FormalParameters()
        .add ( SOURCE_DIRECTORY )
        .add ( OUTPUT_FILE );
}
