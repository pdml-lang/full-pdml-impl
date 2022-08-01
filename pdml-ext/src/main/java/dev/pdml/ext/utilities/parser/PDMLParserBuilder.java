package dev.pdml.ext.utilities.parser;

import dev.pdml.core.data.formalNode.FormalPDMLNodes;
import dev.pdml.core.parser.PDMLParser;
import dev.pdml.core.parser.PDMLParserOptions;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.core.reader.PDMLReader;
import dev.pdml.core.reader.PDMLReaderImpl;
import dev.pdml.core.reader.PDMLReaderOptions;
import dev.pdml.core.reader.extensions.PDMLExtensionsHandler;
import dev.pdml.ext.commands.SharedDefaultOptions;
import dev.pdml.ext.extensions.PDMLExtensionsHandlerImpl;
import dev.pdml.ext.extensions.scripting.bindings.DocBinding;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class PDMLParserBuilder<N, R> {

    private final @NotNull PDMLParserEventHandler<N, R> eventHandler;
    private @NotNull TextErrorHandler errorHandler = SharedDefaultOptions.createErrorHandler ();
    private @Nullable FormalPDMLNodes formalNodes = null;
    private @Nullable PDMLExtensionsHandler extensionsHandler = new PDMLExtensionsHandlerImpl();
    private @Nullable ScriptingEnvironment scriptingEnvironment = new ScriptingEnvironmentImpl ( true );
    private boolean ignoreTextAfterEndOfRootNode = false;


    public PDMLParserBuilder ( @NotNull PDMLParserEventHandler<N, R> eventHandler ) {

        this.eventHandler = eventHandler;
    }


    public PDMLParserBuilder<N, R> errorHandler ( @NotNull TextErrorHandler errorHandler ) {

        this.errorHandler = errorHandler;
        return this;
    }

    public PDMLParserBuilder<N, R> formalNodes ( @Nullable FormalPDMLNodes formalNodes ) {

        this.formalNodes = formalNodes;
        return this;
    }

    public PDMLParserBuilder<N, R> extensionsHandler ( @Nullable PDMLExtensionsHandler extensionsHandler ) {

        this.extensionsHandler = extensionsHandler;
        return this;
    }

    public PDMLParserBuilder<N, R> scriptingEnvironment ( @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.scriptingEnvironment = scriptingEnvironment;
        return this;
    }

    public PDMLParserBuilder<N, R> ignoreTextAfterEndOfRootNode ( boolean ignoreTextAfterEndOfRootNode ) {

        this.ignoreTextAfterEndOfRootNode = ignoreTextAfterEndOfRootNode;
        return this;
    }

    public @NotNull PDMLParserOptions<N, R> getOptions() {

        return new PDMLParserOptions<> (
            eventHandler, errorHandler, formalNodes, extensionsHandler, scriptingEnvironment, ignoreTextAfterEndOfRootNode );
    }

/*
    public PDMLParser<N, R> build() throws IOException, TextErrorException {

        PDMLParserOptions<N, R> parserOptions = getOptions();
        PDMLParser<N, R> parser = new PDMLParser<> ( parserOptions );

//        if ( scriptingEnvironment != null ) {
//            DebugUtils.writeNameValue ( "parser.getPDMLReader()", parser.getPDMLReader() );
//            DocBinding docBinding = new DocBinding ( parser.getPDMLReader() );
//            scriptingEnvironment.addBinding ( docBinding.bindingName(), docBinding );
//        }

        return parser;
    }
*/

    public void parsePDMLReader ( @NotNull PDMLReader PDMLReader ) throws IOException, TextErrorException {

        if ( scriptingEnvironment != null ) {
            // DebugUtils.writeNameValue ( "parser.getPDMLReader()", parser.getPDMLReader() );
            DocBinding docBinding = new DocBinding ( PDMLReader );
            scriptingEnvironment.addBinding ( docBinding.bindingName(), docBinding );
        }

        PDMLParser<N, R> parser = new PDMLParser<>();
        parser.parsePDMLReader ( PDMLReader, getOptions() );
    }

    public void parseCharReader ( @NotNull CharReaderWithInserts charReader )
        throws IOException, TextErrorException {

        parsePDMLReader ( new PDMLReaderImpl ( charReader, new PDMLReaderOptions ( getOptions() ) ) );
    }

    public void parseReader ( @NotNull Reader reader, @Nullable TextResource resource )
        throws IOException, TextErrorException {

        parsePDMLReader ( new PDMLReaderImpl ( reader, resource, new PDMLReaderOptions ( getOptions() ) ) );
    }

    public void parseFile ( @NotNull Path file ) throws IOException, TextErrorException {

        try ( FileReader fileReader = TextFileIO.getUTF8FileReader ( file ) ) {
            parseReader ( fileReader, new File_TextResource ( file ) );
        }
    }

    public void parseString ( @NotNull String string ) throws IOException, TextErrorException {

        try ( StringReader stringReader = new StringReader ( string ) ) {
            parseReader ( stringReader, new String_TextResource ( string ) );
        }
    }

    public void parseStdin() throws IOException, TextErrorException {

        parseReader ( OSIO.standardInputUTF8Reader(), null );
    }
}
