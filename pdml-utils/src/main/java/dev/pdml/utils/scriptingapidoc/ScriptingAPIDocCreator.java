package dev.pdml.utils.scriptingapidoc;

import dev.pdml.extscripting.bindings.DocBinding;
import dev.pdml.reader.PdmlReaderImpl;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.directory.DirectoryContentUtils;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.scripting.bindings.builder.BindingsBuilder;
import dev.pp.scripting.docletParser.JavaDocletParser;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ScriptingAPIDocCreator {

    public static final @NotNull String BINDING_NAME_METHOD_NAME = "bindingName";


    public static void createCoreDoc (
        @NotNull Path sourceCodeDirectory,
        @NotNull Path targetFile ) throws IOException {

        Map<String, Object> bindings = new BindingsBuilder()
            .addCoreBindings()
            .getResult();

        createDoc ( bindings, sourceCodeDirectory, targetFile );
    }

    public static void createExtensionsDoc (
        @NotNull Path sourceCodeDirectory,
        @NotNull Path targetFile ) throws IOException {

        Map<String, Object> bindings = new BindingsBuilder()
            .add ( new DocBinding ( new PdmlReaderImpl ( new StringReader ( "dummy" ) ) ) )
            .getResult();

        createDoc ( bindings, sourceCodeDirectory, targetFile );
    }

    public static void createDoc (
        @NotNull Map<String, Object> bindings,
        @NotNull Path sourceCodeDirectory,
        @NotNull Path targetFile ) throws IOException {

        List<Path> files = DirectoryContentUtils.filesInDirectory ( sourceCodeDirectory );
        assert files != null;

        Writer targetWriter = TextFileIO.getUTF8FileWriter ( targetFile );

        createDoc ( bindings, files, targetWriter );

        targetWriter.close();
    }

    public static void createDoc (
        @NotNull Map<String, Object> scriptingBindings,
        @NotNull List<Path> JavaSourceCodeFiles,
        @NotNull Writer targetWriter ) {

        ScriptingBindingsDoclet.bindings_ = scriptingBindings;
        ScriptingBindingsDoclet.targetWriter_ = targetWriter;

        JavaDocletParser.parseFiles ( JavaSourceCodeFiles, ScriptingBindingsDoclet.class, null );
    }

/*
    public static void createDoc() throws IOException {

        Map<String, Object> bindings = BindingsCreator.createMap (
            new DefaultPXMLReader ( "dummy" ), new TextToken ( "dummy" ) );

        File coreBindingsDirectory = new File (
            "C:\\aa\\work\\PDML\\dev\\current\\dev.pp.scripting\\src\\main\\java\\dev\\pp\\scripting\\bindings\\core" );
        File extensionsBindingsDirectory = new File (
            "C:\\aa\\work\\PDML\\dev\\current\\dev.pdml.ext\\src\\main\\java\\dev\\pdml\\ext\\extensions\\node\\standard\\scripting\\bindings" );

        List<File> files = DirectoryContentUtils.filesInDirectory ( coreBindingsDirectory );
        assert files != null;
        List<File> extensionsBindingsFiles = DirectoryContentUtils.filesInDirectory ( extensionsBindingsDirectory );
        assert extensionsBindingsFiles != null;
        files.addAll ( extensionsBindingsFiles );

        File targetFile = new File ( "C:\\aa\\work\\PDML\\docs\\Extensions\\Reference_Manual\\Scripting_API_Doc\\Scripting_API_doc.pml" );

        Writer targetWriter = TextFileUtils.getUTF8FileWriter ( targetFile );

        createDoc ( bindings, files, targetWriter );

        targetWriter.close();
    }
*/
}
