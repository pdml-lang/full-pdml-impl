package dev.pdml.ext.extensions.scripting.bindings;

import dev.pdml.core.exception.PDMLDocumentException;
import dev.pdml.core.exception.PDMLDocumentSemanticException;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.ext.extensions.utilities.PDMLEscaper;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.scripting.bindings.ScriptingBinding;
import dev.pp.text.token.TextToken;
import dev.pp.basics.utilities.file.TempFileUtils;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.basics.utilities.os.process.OSCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

// TODO: experimental.

public class PlantUMLBinding implements ScriptingBinding {

    private final @NotNull PDMLReader reader;
    private final @NotNull TextToken nodeNameToken;


    public PlantUMLBinding ( @NotNull PDMLReader reader, @NotNull TextToken nodeNameToken ) {
        this.reader = reader;
        this.nodeNameToken = nodeNameToken;
    }


    public @NotNull String bindingName () { return "plantUML"; }

    public void insertPlantUMLImage (
        // @NotNull File sourceFile,
        @NotNull String fileNameWithoutExtension,
        @NotNull String code,
        @Nullable Path inputDirectory,
        @Nullable Path outputDirectory,
        @Nullable Path relativeImagesDirectory,
        @Nullable String plantUMLCLIParams,
        @Nullable String targetCodeParams ) throws IOException, InterruptedException, PDMLDocumentException {

        // TODO: pipe from stdin by default, by using plantuml's '-pipe' CL argument

        String inputFileExtension = "txt";
        Path inputFile;
        if ( inputDirectory == null ) {
            inputFile = TempFileUtils.createEmptyTempFile ( fileNameWithoutExtension,inputFileExtension,true );
        } else {
            inputFile = Path.of ( inputDirectory.toString(), fileNameWithoutExtension + "." + inputFileExtension );
        }
        TextFileIO.writeTextToUTF8File ( code, inputFile );

        var tokens = new ArrayList<String> ();
        tokens.add ( "java" );
        tokens.add ( "-jar" );
        tokens.add ( "plantuml.jar" );
        if ( outputDirectory != null ) {
            tokens.add ( "-o" );
            // arguments.add ( outputDirectory.toString() );
            tokens.add ( outputDirectory.toAbsolutePath().toString() );
        }
        // TODO add plantUMLCLIParams
        tokens.add ( inputFile.toString() );

        // int exitCode = OSCommand.runAndWait ( "java", arguments.toArray( new String[0] ) );
        int exitCode = OSCommand.runAndWait ( tokens );
        if ( exitCode != 0 ) {
            throw new PDMLDocumentSemanticException ( "ID", "exit code " + exitCode, nodeNameToken ); // TODO error
        }

        String outputFileName = fileNameWithoutExtension + ".png";
        Path relativeFilePathOfImage;
        if ( relativeImagesDirectory != null ) {
            relativeFilePathOfImage = Path.of ( relativeImagesDirectory.toString(), outputFileName );
        } else {
            relativeFilePathOfImage = Path.of ( outputFileName );
        }
        // TODO add targetCodeParams
        String targetCode = "[image (source=\"" +
            PDMLEscaper.escapeDoubleQuotedAttributeValue ( relativeFilePathOfImage.toString() ) + "\")]";
        reader.insertStringToRead ( targetCode );
    }

    public void insertPlantUMLImage (
        @NotNull String fileNameWithoutExtension,
        @NotNull String code ) throws IOException, InterruptedException, PDMLDocumentException {

        insertPlantUMLImage (
            fileNameWithoutExtension,
            code,
            // null,
            Path.of ( "resources", "images" ),
            Path.of ( "resources", "images" ),
            Path.of ( "images" ),
            null,
            null );
    }
}
