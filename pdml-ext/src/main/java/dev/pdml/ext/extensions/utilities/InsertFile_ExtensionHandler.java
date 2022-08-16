package dev.pdml.ext.extensions.utilities;

import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.formalNode.FormalPDMLNode;
import dev.pdml.core.data.node.name.QualifiedNodeName;
import dev.pdml.core.data.node.namespace.Namespace;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.extensions.PDMLExtensionsContext;
import dev.pdml.ext.extensions.ExtensionsHelper;
import dev.pdml.ext.extensions.ExtensionsNamespaces;
import dev.pdml.ext.extensions.node.PDMLExtensionNodeHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.datatype.nonUnion.scalar.impls.Boolean.BooleanDataType;
import dev.pp.datatype.nonUnion.scalar.impls.filesystempath.DirectoryOrFilePathDataType;
import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.FormalParameters;
import dev.pp.parameters.parameter.Parameters;
import dev.pp.text.documentation.SimpleDocumentation;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.TextErrorException;

import java.io.IOException;
import java.nio.file.Path;

public class InsertFile_ExtensionHandler implements PDMLExtensionNodeHandler {


    public static final @NotNull Namespace NAMESPACE = ExtensionsNamespaces.UTILITY_NAMESPACE;
    public static final @NotNull String LOCAL_NODE_NAME = "ins_file";
    public static final @NotNull QualifiedNodeName QUALIFIED_NODE_NAME = new QualifiedNodeName (
        LOCAL_NODE_NAME, NAMESPACE );
    public static final InsertFile_ExtensionHandler INSTANCE = new InsertFile_ExtensionHandler ();


    private InsertFile_ExtensionHandler (){}


    public static final FormalParameter<Path> PATH_PARAMETER = new FormalParameter.Builder<> (
        "path", DirectoryOrFilePathDataType.DEFAULT )
        .sortIndex ( 1 )
        .positionalParameterIndex ( 1 )
        .documentation ( "File Path",
            "Absolute or relative path of the file to be inserted. In case of a relative path, it is relative to the directory of the file in which the file is inserted.",
            "path = chapters/conclusion.pml")
        .build ();

    public static final FormalParameter<Boolean> PARSE_AS_TEXT_PARAMETER = new FormalParameter.Builder<> (
        "parse_as_text", BooleanDataType.INSTANCE )
        .defaultValue ( false )
        .sortIndex ( 2 )
        .documentation ( "Parse as Text",
            "By default, the file content is parsed as PDML code. To parse it as text, this parameter can be set to true.",
            "parse_as_text = yes")
        .build ();

    /* TODO
    public static final FormalParameter<String> INCLUDE_LINES_PARAMETER = new FormalParameter<> (
        "include_lines",
        null,
        new StringOrNull_ParameterType(),
        3,
        new SimpleDocumentation ( "", "", "" ) );
    */

    public static final FormalParameters FORMAL_PARAMETERS = new FormalParameters()
        .add ( PATH_PARAMETER )
        .add ( PARSE_AS_TEXT_PARAMETER );

    public static final FormalPDMLNode<Path> FORMAL_NODE = new FormalPDMLNode<> (
        QUALIFIED_NODE_NAME,
        null,
        FORMAL_PARAMETERS,
        false,
        false,
        () -> new SimpleDocumentation (
            "Insert File",
            "Insert the content of a markup or text file at the current position.",
            "[!insert-file ( path = chapters/conclusion.pml ) ]" ) );


    public @NotNull QualifiedNodeName getQualifiedNodeName() {
        return QUALIFIED_NODE_NAME;
    }

    public void handleNode ( @NotNull PDMLExtensionsContext context, @NotNull ASTNodeName nodeName )
        throws IOException, TextErrorException {

        // now positioned right after the node name

        @NotNull PDMLReader reader = context.getPDMLReader ();
        @NotNull TextErrorHandler errorHandler = context.getErrorHandler();

        reader.skipWhitespaceAndComments();

        @NotNull Parameters parameters = PDMLParserHelper.parseParametersWithOptionalParenthesis (
            reader, FORMAL_PARAMETERS, errorHandler, true );

        reader.skipWhitespaceAndComments();

        PDMLParserHelper.requireNodeEnd ( reader, nodeName, errorHandler );

        @Nullable Path existingFilePath = ExtensionsHelper.getExistingFilePath (
            parameters, reader, PATH_PARAMETER.getName(), errorHandler );
        if ( existingFilePath == null ) return;

        // TODO PARSE_AS_TEXT_PARAMETER

        reader.insertFileToRead ( existingFilePath );
    }
}
