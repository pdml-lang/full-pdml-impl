package dev.pdml.utils.parser;

import dev.pdml.ext.PdmlExtensionsHandlerImpl;
import dev.pdml.extscripting.bindings.DocBinding;
import dev.pdml.parser.PdmlParser;
import dev.pdml.parser.PdmlParserOptions;
import dev.pdml.parser.eventhandler.PdmlParserEventHandler;
import dev.pdml.parser.nodespec.PdmlNodeSpecs;
import dev.pdml.reader.PdmlReader;
import dev.pdml.reader.PdmlReaderImpl;
import dev.pdml.reader.extensions.PdmlExtensionsHandler;
import dev.pdml.utils.SharedDefaultOptions;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.basics.utilities.os.OSIO;
import dev.pp.scripting.env.ScriptingEnvironment;
import dev.pp.scripting.env.ScriptingEnvironmentImpl;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.inspection.handler.TextInspectionMessageHandler;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.String_TextResource;
import dev.pp.text.resource.TextResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class PdmlParserBuilder<N, R> {

    private final @NotNull PdmlParserEventHandler<N, R> eventHandler;
    private @NotNull TextInspectionMessageHandler errorHandler = SharedDefaultOptions.createErrorHandler();
    private @Nullable PdmlNodeSpecs<?> nodeSpecs = null;
    private @Nullable PdmlExtensionsHandler extensionsHandler = new PdmlExtensionsHandlerImpl ();
    private @Nullable ScriptingEnvironment scriptingEnvironment = new ScriptingEnvironmentImpl ( true );
    private boolean ignoreTextAfterEndOfRootNode = false;
    private boolean allowStandardAttributesStartSyntax = true;
    private boolean allowAlternativeAttributesStartSyntax = false;


    public PdmlParserBuilder ( @NotNull PdmlParserEventHandler<N, R> eventHandler ) {

        this.eventHandler = eventHandler;
    }


    public PdmlParserBuilder<N, R> errorHandler ( @NotNull TextInspectionMessageHandler errorHandler ) {

        this.errorHandler = errorHandler;
        return this;
    }

    public PdmlParserBuilder<N, R> nodeSpecs ( @Nullable PdmlNodeSpecs<?> nodeSpecs ) {

        this.nodeSpecs = nodeSpecs;
        return this;
    }

    public PdmlParserBuilder<N, R> extensionsHandler ( @Nullable PdmlExtensionsHandler extensionsHandler ) {

        this.extensionsHandler = extensionsHandler;
        return this;
    }

    public PdmlParserBuilder<N, R> scriptingEnvironment ( @Nullable ScriptingEnvironment scriptingEnvironment ) {

        this.scriptingEnvironment = scriptingEnvironment;
        return this;
    }

    public PdmlParserBuilder<N, R> ignoreTextAfterEndOfRootNode ( boolean ignoreTextAfterEndOfRootNode ) {

        this.ignoreTextAfterEndOfRootNode = ignoreTextAfterEndOfRootNode;
        return this;
    }

    public PdmlParserBuilder<N, R> allowStandardAttributesStartSyntax ( boolean allowStandardAttributesStartSyntax ) {

        this.allowStandardAttributesStartSyntax = allowStandardAttributesStartSyntax;
        return this;
    }

    public PdmlParserBuilder<N, R> allowAlternativeAttributesStartSyntax ( boolean allowAlternativeAttributesStartSyntax ) {

        this.allowAlternativeAttributesStartSyntax = allowAlternativeAttributesStartSyntax;
        return this;
    }

    public @NotNull PdmlParserOptions<N, R> getOptions() {

        return new PdmlParserOptions<> (
            eventHandler, errorHandler, nodeSpecs, extensionsHandler, scriptingEnvironment,
            ignoreTextAfterEndOfRootNode, allowStandardAttributesStartSyntax, allowAlternativeAttributesStartSyntax );
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

    // TODO public boolean parsePDMLReader (
    // return false if an error occurred
    public void parsePDMLReader ( @NotNull PdmlReader pdmlReader ) throws IOException, TextErrorException {

        if ( scriptingEnvironment != null ) {
            // DebugUtils.writeNameValue ( "parser.getPDMLReader()", parser.getPDMLReader() );
            DocBinding docBinding = new DocBinding ( pdmlReader );
            scriptingEnvironment.addBinding ( docBinding.bindingName(), docBinding );
        }

        PdmlParser<N, R> parser = new PdmlParser<>();
        parser.parsePDMLReader ( pdmlReader, getOptions() );
    }

    public void parseCharReader ( @NotNull CharReaderWithInserts charReader )
        throws IOException, TextErrorException {

        parsePDMLReader ( new PdmlReaderImpl ( charReader, getOptions().toReaderOptions() ) );
    }

    public void parseReader (
        @NotNull Reader reader,
        @Nullable TextResource resource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset )
        throws IOException, TextErrorException {

        parsePDMLReader ( new PdmlReaderImpl (
            reader, resource, lineOffset, columnOffset, getOptions().toReaderOptions() ) );
    }

    public void parseFile ( @NotNull Path file ) throws IOException, TextErrorException {

        try ( FileReader fileReader = TextFileIO.getUTF8FileReader ( file ) ) {
            parseReader ( fileReader, new File_TextResource ( file ), null, null );
        }
    }

    public void parseString (
        @NotNull String string,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset ) throws IOException, TextErrorException {

        try ( StringReader stringReader = new StringReader ( string ) ) {
            parseReader ( stringReader, new String_TextResource ( string ), lineOffset, columnOffset );
        }
    }

    public void parseStdin() throws IOException, TextErrorException {

        parseReader ( OSIO.standardInputUTF8Reader(), null, null, null );
    }
}
