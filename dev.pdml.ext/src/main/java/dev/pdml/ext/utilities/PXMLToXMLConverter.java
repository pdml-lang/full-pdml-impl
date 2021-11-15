package dev.pdml.ext.utilities;

import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.utilities.FileUtilities;
import dev.pdml.core.reader.parser.EventStreamParserBuilder;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pdml.core.reader.parser.eventHandler.impls.CreateDOM_ParserEventHandler;
import dev.pdml.core.reader.parser.eventHandler.impls.WriteXML_ParserEventHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.ext.extensions.DefaultPXMLExtensionsHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.*;
import java.nio.file.Path;

public class PXMLToXMLConverter {

    public static void pXMLFileToXMLFile ( @NotNull File pXMLFile, @NotNull File XMLFile ) throws Exception {

        final FileReader PXMLFileReader = FileUtilities.getUTF8FileReader ( pXMLFile );
        final FileWriter XMLFileWriter = FileUtilities.getUTF8FileWriter ( XMLFile );
        pipePXMLReaderToXMLWriter ( PXMLFileReader, XMLFileWriter, new File_TextResource ( pXMLFile ) );
        PXMLFileReader.close();
        XMLFileWriter.close();
    }

    public static void pXMLFileToXMLFile ( @NotNull Path pXMLFilePath, @NotNull Path XMLFilePath ) throws Exception {
        pXMLFileToXMLFile ( pXMLFilePath.toFile(), XMLFilePath.toFile() );
    }

    public static void pXMLFileToXMLFile ( @NotNull String pXMLFilePath, @NotNull String XMLFilePath ) throws Exception {
        pXMLFileToXMLFile ( new File ( pXMLFilePath ), new File ( XMLFilePath ) );
    }

    // doesn't close reader nor writer
    public static void pipePXMLReaderToXMLWriter (
        @NotNull Reader pXMLReader, @NotNull Writer XMLWriter, @Nullable TextResource pXMLResource ) throws Exception {

        new EventStreamParserBuilder<> ( new WriteXML_ParserEventHandler ( XMLWriter ) )
            .setExtensionsHandler ( new DefaultPXMLExtensionsHandler() )
            .parseReader ( pXMLReader, pXMLResource );
    }

    public static @NotNull Document pXMLFileToXMLDocument ( @NotNull File pXMLFile ) throws Exception {

        return pXMLToXMLDocument ( FileUtilities.getUTF8FileReader ( pXMLFile ), new File_TextResource ( pXMLFile ) );
    }

    public static @NotNull Document pXMLToXMLDocument ( @NotNull Reader pXMLReader, @Nullable TextResource pXMLResource ) throws Exception {

        // long startTimeNanos = System.nanoTime();

        ParserEventHandler<Node, Document> eventHandler = new CreateDOM_ParserEventHandler();
        new EventStreamParserBuilder<> ( eventHandler )
            .parseReader ( pXMLReader, pXMLResource );

        /*
        long endTimeNanos = System.nanoTime();
        long time = endTimeNanos - startTimeNanos;
        long micros = TimeUnit.NANOSECONDS.toMicros ( time );
        System.out.println ( "PXMLToXMLDocument time: " + String.valueOf ( micros ) + " microseconds" );
        */

        return eventHandler.getResult();
    }
}
