package dev.pdml.ext.utilities.parser;

import dev.pdml.core.data.AST.PDMLNodeAST;
import dev.pdml.core.data.formalNode.FormalPDMLNodes;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.ext.commands.SharedDefaultOptions;
import dev.pdml.ext.utilities.parser.eventhandlers.CreateAST_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.CharReader;
import dev.pp.text.reader.CharReaderImpl;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public class PDMLParserUtils {

    public static @NotNull PDMLNodeAST parseFileToAST ( @NotNull Path file ) throws Exception {

        return parseFileToAST ( file, SharedDefaultOptions.createErrorHandler (), null );
    }

    public static @NotNull PDMLNodeAST parseFileToAST (
        @NotNull Path file,
        @NotNull TextErrorHandler errorHandler,
        @Nullable FormalPDMLNodes formalNodes ) throws Exception {

        PDMLParserEventHandler<PDMLNodeAST, PDMLNodeAST> eventHandler = new CreateAST_ParserEventHandler();

        try ( Reader reader = TextFileIO.getUTF8FileReader ( file ) ) {
            new PDMLParserBuilder<> ( eventHandler )
                .errorHandler ( errorHandler )
                .formalNodes ( formalNodes )
                .parseFile ( file );

            PDMLNodeAST result = eventHandler.getResult();
            assert result != null;
            return result;
        }
    }

    public static @NotNull CharReaderWithInserts createVirtualRootReader (
        @NotNull String virtualRootName,
        @NotNull CharReader rootContentReader ) throws IOException {

        CharReader rootNodeStartReader = CharReaderImpl.createForString ( "[" + virtualRootName + " " );
        CharReader rootNodeEndReader = CharReaderImpl.createForString ( "]" );

        CharReaderWithInserts result = new CharReaderWithInsertsImpl ( rootNodeEndReader );
        result.insert ( rootContentReader );
        result.insert ( rootNodeStartReader );

        return result;
    }

    public static @NotNull CharReaderWithInserts createVirtualRootReader (
        @NotNull String virtualRootName,
        @NotNull Reader rootContentReader,
        @Nullable TextResource rootContentTextResource ) throws IOException {

        CharReader rootNodeStartReader = CharReaderImpl.createForString ( "[" + virtualRootName + " " );
        CharReader rootNodeEndReader = CharReaderImpl.createForString ( "]" );
        CharReader rootNodeContentReader = new CharReaderImpl ( rootContentReader, rootContentTextResource );

        CharReaderWithInserts result = new CharReaderWithInsertsImpl ( rootNodeEndReader );
        result.insert ( rootNodeContentReader );
        result.insert ( rootNodeStartReader );

        return result;
    }
}
