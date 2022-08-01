package dev.pdml.ext.utilities.xml;

import dev.pdml.ext.utilities.parser.PDMLParserBuilder;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.ext.utilities.parser.eventhandlers.CreateDOM_ParserEventHandler;
import dev.pdml.ext.utilities.parser.eventhandlers.WriteXML_ParserEventHandler;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.*;
import java.nio.file.Path;

public class PDMLToXMLConverter {

    public static void PDMLFileToXMLFile ( @NotNull Path PDMLFile, @NotNull Path XMLFile ) throws Exception {

        final FileReader PDMLFileReader = TextFileIO.getUTF8FileReader ( PDMLFile );
        final FileWriter XMLFileWriter = TextFileIO.getUTF8FileWriter ( XMLFile );
        pipePDMLReaderToXMLWriter ( PDMLFileReader, XMLFileWriter, new File_TextResource ( PDMLFile ) );
        PDMLFileReader.close();
        XMLFileWriter.close();
    }

    public static void PDMLFileToXMLFile ( @NotNull String PDMLFilePath, @NotNull String XMLFilePath ) throws Exception {
        PDMLFileToXMLFile ( Path.of ( PDMLFilePath ), Path.of ( XMLFilePath ) );
    }

    // doesn't close reader nor writer
    public static void pipePDMLReaderToXMLWriter (
        @NotNull Reader PDMLReader, @NotNull Writer XMLWriter, @Nullable TextResource PDMLResource ) throws Exception {

        new PDMLParserBuilder<> ( new WriteXML_ParserEventHandler ( XMLWriter ) )
            .parseReader ( PDMLReader, PDMLResource );
    }

    public static @NotNull Document PDMLFileToXMLDocument ( @NotNull Path PDMLFile ) throws Exception {

        return PDMLToXMLDocument ( TextFileIO.getUTF8FileReader ( PDMLFile ), new File_TextResource ( PDMLFile ) );
    }

    public static @NotNull Document PDMLToXMLDocument ( @NotNull Reader PDMLReader, @Nullable TextResource PDMLResource ) throws Exception {

        // long startTimeNanos = System.nanoTime();

        PDMLParserEventHandler<Node, Document> eventHandler = new CreateDOM_ParserEventHandler();
        new PDMLParserBuilder<> ( eventHandler )
            .parseReader ( PDMLReader, PDMLResource );

        /*
        long endTimeNanos = System.nanoTime();
        long time = endTimeNanos - startTimeNanos;
        long micros = TimeUnit.NANOSECONDS.toMicros ( time );
        System.out.println ( "PXMLToXMLDocument time: " + String.valueOf ( micros ) + " microseconds" );
        */

        return eventHandler.getResult();
    }
}
