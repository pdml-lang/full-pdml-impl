package dev.pp.pdml.ext.utils;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.ext.ExtensionNodeHandler;
import dev.pp.pdml.ext.ExtensionNodeHandlerContext;
import dev.pp.pdml.ext.InsertStringExtensionResult;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.file.FilePathUtils;
import dev.pp.core.basics.utilities.os.OSDirectories;
import dev.pp.core.datatype.nonunion.scalar.impls.booleantype.BooleanDataType;
import dev.pp.core.datatype.nonunion.scalar.impls.filesystempath.DirectoryOrFilePathDataType;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.parameterspec.ParameterSpec;
import dev.pp.core.parameters.parameterspecs.MutableParameterSpecs;
import dev.pp.core.parameters.parameterspecs.ParameterSpecs;
import dev.pp.core.text.resource.File_TextResource;
import dev.pp.core.text.resource.TextResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class InsertFileHandler implements ExtensionNodeHandler {


    public static final @NotNull String NAME = "ins_file";
    public static final InsertFileHandler INSTANCE = new InsertFileHandler();


    private InsertFileHandler(){}


    @Override
    public @NotNull String getExtensionKind() {
        return UtilityHandlerConstants.EXTENSION_KIND;
    }

    public @NotNull String getExtensionName() {
        return NAME;
    }

    public static final ParameterSpec<Path> PATH_PARAMETER = new ParameterSpec.Builder<> (
        "path", DirectoryOrFilePathDataType.DEFAULT )
        .sortIndex ( 1 )
        .position ( 0 )
        .documentation ( "File Path",
            "Absolute or relative path of the file to be inserted. In case of a relative path, it is relative to the directory of the file in which the file is inserted.",
            "path = chapters/conclusion.pml")
        .build ();

    // TODO replace by InsertStringFormat
    public static final ParameterSpec<Boolean> PARSE_AS_TEXT_PARAMETER = new ParameterSpec.Builder<> (
        "parse_as_text", BooleanDataType.INSTANCE )
        .defaultValue ( false )
        .sortIndex ( 2 )
        .documentation ( "Parse as Raw Text",
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
        .add ( PATH_PARAMETER ).makeImmutable();
        // TODO .add ( PARSE_AS_TEXT_PARAMETER ).makeImmutable();

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

    @Override
    public @Nullable InsertStringExtensionResult handleNode ( @NotNull ExtensionNodeHandlerContext context, @NotNull NodeTag nodeName )
        throws IOException, PdmlException {

        // now positioned right after the node tag

        context.skipWhitespaceAndComments ();

        @Nullable Parameters<?> parameters = context.parseParametersWithOptionalParenthesis (
            PARAMETER_SPECS );
        assert parameters != null;

        context.skipWhitespaceAndComments ();
        context.requireExtensionNodeEnd ( nodeName );

        PdmlReader reader = context.getPdmlReader();
        @Nullable Path existingFilePath = getExistingFilePath (
            parameters, reader, PATH_PARAMETER.getName(), context );
        if ( existingFilePath == null ) return null;

        /*
        @Nullable String string = TextFileReaderUtil.readTextFromUTF8File ( existingFilePath );

        // TODO InsertStringFormat
        return new InsertStringExtensionResult ( string, InsertStringFormat.AS_IS );
         */

        reader.insertFileToRead ( existingFilePath );

        return null;
    }

    private static @Nullable Path getExistingFilePath (
        @NotNull Parameters<?> parameters,
        @NotNull PdmlReader reader,
        @NotNull String pathParameterName,
        @NotNull ExtensionNodeHandlerContext context ) throws IOException, PdmlException {

        @NotNull Path filePath = parameters.nonNullCastedValue ( pathParameterName );
        @NotNull Path rootDirectory = getRootDirectoryFromResource ( reader.currentResource() );
        try {
            return FilePathUtils.toExistingOSPath ( filePath, rootDirectory );
        } catch ( FileNotFoundException e ) {
            throw context.error (
                e.getMessage(),
                "FILE_DOES_NOT_EXIST",
                parameters.valueToken ( pathParameterName ) );
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

    private static @NotNull Path getRootDirectoryFromResource (
        @Nullable TextResource resource ) {

        return getRootDirectoryFromResource (
            resource, OSDirectories.currentWorkingDirectory() );
    }
}
