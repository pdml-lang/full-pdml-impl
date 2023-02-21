package dev.pdml.extutils;

import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.extshared.PdmlExtensionNodeHandler;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.extensions.PdmlExtensionsContext;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.FilePathUtils;
import dev.pp.basics.utilities.os.OSDirectories;
import dev.pp.datatype.nonunion.scalar.impls.Boolean.BooleanDataType;
import dev.pp.datatype.nonunion.scalar.impls.filesystempath.DirectoryOrFilePathDataType;
import dev.pp.parameters.parameters.Parameters;
import dev.pp.parameters.parameterspec.ParameterSpec;
import dev.pp.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.parameters.parameterspecs.ParameterSpecs;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.inspection.message.TextError;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class InsertFile_ExtensionHandler implements PdmlExtensionNodeHandler {


    public static final @NotNull NodeNamespace NAMESPACE = UtilityConstants.NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "ins_file";
    public static final @NotNull NodeName NODE_NAME = new NodeName (
        LOCAL_NODE_NAME, NAMESPACE.namePrefix () );
    public static final InsertFile_ExtensionHandler INSTANCE = new InsertFile_ExtensionHandler();


    private InsertFile_ExtensionHandler (){}


    public static final ParameterSpec<Path> PATH_PARAMETER = new ParameterSpec.Builder<> (
        "path", DirectoryOrFilePathDataType.DEFAULT )
        .sortIndex ( 1 )
        .positionalParameterIndex ( 1 )
        .documentation ( "File Path",
            "Absolute or relative path of the file to be inserted. In case of a relative path, it is relative to the directory of the file in which the file is inserted.",
            "path = chapters/conclusion.pml")
        .build ();

    public static final ParameterSpec<Boolean> PARSE_AS_TEXT_PARAMETER = new ParameterSpec.Builder<> (
        "parse_as_text", BooleanDataType.INSTANCE )
        .defaultValue ( false )
        .sortIndex ( 2 )
        .documentation ( "Parse as Text",
            "By default, the file content is parsed as PDML code. To parse it as text, this parameter can be set to true.",
            "parse_as_text = yes")
        .build ();

    /* TODO
    public static final ParameterSpec<String> INCLUDE_LINES_PARAMETER = new ParameterSpec<> (
        "include_lines",
        null,
        new StringOrNull_ParameterType(),
        3,
        new SimpleDocumentation ( "", "", "" ) );
    */

    @SuppressWarnings ( {"unchecked"} )
    public static final ParameterSpecs<?> PARAMETER_SPECS = new MutableParameterSpecs()
        .add ( PATH_PARAMETER )
        .add ( PARSE_AS_TEXT_PARAMETER ).makeImmutable();

/*
    public static final PdmlNodeSpec<Path> NODE_SPEC = new PdmlNodeSpec<> (
        QUALIFIED_NODE_NAME,
        null,
        PARAMETER_SPECS,
        false,
        false,
        () -> new SimpleDocumentation (
            "Insert File",
            "Insert the content of a markup or text file at the current position.",
            "[!insert-file ( path = chapters/conclusion.pml ) ]" ) );
 */


    public @NotNull NodeName getNodeName() { return NODE_NAME; }

    public void handleNode ( @NotNull PdmlExtensionsContext context, @NotNull NodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        PdmlReader reader = context.getPdmlReader();
        @NotNull TextInspectionMessageHandler errorHandler = context.getErrorHandler();

        reader.skipWhitespaceAndComments();

        @NotNull Parameters<?> parameters = PdmlParserHelper.parseParametersWithOptionalParenthesis (
            reader, PARAMETER_SPECS, errorHandler, true );

        reader.skipWhitespaceAndComments();

        PdmlParserHelper.requireNodeEnd ( reader, nodeName, errorHandler );

        @Nullable Path existingFilePath = getExistingFilePath (
            parameters, reader, PATH_PARAMETER.getName(), errorHandler );
        if ( existingFilePath == null ) return;

        // TODO PARSE_AS_TEXT_PARAMETER

        reader.insertFileToRead ( existingFilePath );
    }

    private static @Nullable Path getExistingFilePath (
        @NotNull Parameters<?> parameters,
        @NotNull PdmlReader reader,
        @NotNull String pathParameterName,
        @NotNull TextInspectionMessageHandler errorHandler ) throws IOException {

        @NotNull Path filePath = parameters.nonNullCastedValue ( pathParameterName );
        @NotNull Path rootDirectory = getRootDirectoryFromResource ( reader.currentResource() );
        try {
            return FilePathUtils.toExistingOSPath ( filePath, rootDirectory );
        } catch ( FileNotFoundException e ) {
            errorHandler.handleMessage ( new TextError (
                e.getMessage(),
                "FILE_DOES_NOT_EXIST",
                parameters.valueToken ( pathParameterName ) ) );
            return null;
        }
    }

    private static @NotNull Path getRootDirectoryFromResource (
        @Nullable TextResource resource, @NotNull Path defaultValue ) {

        if ( resource instanceof File_TextResource ftr ) {
            Path path = ftr.getPath();
            return path.toAbsolutePath().getParent();
        } else {
            return defaultValue;
        }
    }

    private static @NotNull Path getRootDirectoryFromResource ( @Nullable TextResource resource ) {

        return getRootDirectoryFromResource ( resource, OSDirectories.currentWorkingDirectory() );
    }
}
