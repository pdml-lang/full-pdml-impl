package dev.pdml.ext.extensions.node.standard;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.FormalNode;
import dev.pdml.core.data.node.name.NodeName;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pdml.core.reader.reader.extensions.ExtensionsContext;

import dev.pdml.ext.extensions.ExtensionsHelper;
import dev.pdml.ext.extensions.node.PXMLExtensionHandler;

import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.datatype.common.Boolean.Boolean_DataType;
import dev.pp.datatype.common.path.Path_DataType;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.utilities.FileUtilities;

import java.io.File;
import java.nio.file.Path;

public class InsertFile_ExtensionHandler implements PXMLExtensionHandler {

    public static final @NotNull String NODE_NAME = "ins-file";

    public static final FormalParameter<Path> PATH_PARAMETER = new FormalParameter<> (
        "path",
        null,
        Path_DataType.createNonNullable ( null, null ),
        1,
        () -> new SimpleDocumentation (
            "File Path",
            "Absolute or relative path of the file to be inserted. In case of a relative path, it is relative to the directory of the file in which the file is inserted.",
            "path = chapters/conclusion.pml") );

    public static final FormalParameter<Boolean> PARSE_AS_TEXT_PARAMETER = new FormalParameter<> (
        "parse_as_text",
        null,
        Boolean_DataType.createNonNullable ( false ),
        2,
        () -> new SimpleDocumentation (
            "Parse as Text",
            "By default, the file content is parsed as PDML code. To parse it as text, this parameter can be set to true.",
            "parse_as_text = yes") );

    /* TODO
    public static final FormalParameter<String> INCLUDE_LINES_PARAMETER = new FormalParameter<> (
        "include_lines",
        null,
        new StringOrNull_ParameterType(),
        3,
        new SimpleDocumentation ( "", "", "" ) );
    */

    @SuppressWarnings ( {"rawtypes", "unchecked"} )
    public static final FormalParameters FORMAL_PARAMETERS = new FormalParameters()
        .add ( PATH_PARAMETER )
        .add ( PARSE_AS_TEXT_PARAMETER );

    @SuppressWarnings ( "unchecked" )
    public static final FormalNode<Path> FORMAL_NODE = new FormalNode<Path> (
        new NodeName ( NODE_NAME ),
        null,
        FORMAL_PARAMETERS,
        new NodeName ( PATH_PARAMETER.getName() ),
        () -> new SimpleDocumentation (
            "Insert File",
            "Insert the content of a markup or text file at the current position.",
            "[!insert-file ( path = chapters/conclusion.pml ) ]" ) );

    public void handleNode ( @NotNull ExtensionsContext context, @NotNull ASTNodeName nodeName ) throws TextReaderException {

        // now positioned right after the node name

        PXMLReader reader = context.getPXMLReader();
        TextErrorHandler errorHandler = context.getErrorHandler();
        String pathParameterName = PATH_PARAMETER.getName();

        @SuppressWarnings ( {"rawtypes", "unchecked"} )
        Parameters parameters = ExtensionsHelper.parseAttributesNodeAndCreateParameters (
            reader, nodeName, pathParameterName, FORMAL_PARAMETERS, errorHandler );

        @Nullable Path path = parameters.getPathOrNull ( pathParameterName );
        if ( path == null ) return; // an error has been handled already

        // TODO PARSE_AS_TEXT_PARAMETER

        File rootDirectory = ExtensionsHelper.getRootDirectoryFromResource ( reader.currentResource() );
        File file = FileUtilities.filePathToExistingFile ( path, rootDirectory, errorHandler, nodeName.getToken() );
        if ( file == null ) return;

        reader.insertFileToRead ( file, nodeName.getToken() );
    }
}
