package dev.pp.pdml.xml;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.utils.treewalker.PdmlCodeWalker;
import dev.pp.pdml.utils.treewalker.handler.PdmlTreeWalkerEventHandler;
import dev.pp.pdml.xml.eventhandlers.CreateDOM_ParserEventHandler;
import dev.pp.pdml.xml.eventhandlers.WriteXMLParserEventHandler;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.resource.File_TextResource;
import dev.pp.core.text.resource.TextResource;
import dev.pp.core.text.utilities.file.TextFileReaderUtil;
import dev.pp.core.text.utilities.file.TextFileWriterUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public class PdmlToXMLConverter {

    public static void PDMLFileToXMLFile ( @NotNull Path PDMLFile, @NotNull Path XMLFile ) throws Exception {

        final FileReader PDMLFileReader = TextFileReaderUtil.createUTF8FileReader ( PDMLFile );
        final FileWriter XMLFileWriter = TextFileWriterUtil.createUTF8FileWriter ( XMLFile, true );
        pipePDMLReaderToXMLWriter ( PDMLFileReader, XMLFileWriter, new File_TextResource ( PDMLFile ) );
        PDMLFileReader.close();
        XMLFileWriter.close();
    }

    public static void PDMLFileToXMLFile ( @NotNull String PDMLFilePath, @NotNull String XMLFilePath ) throws Exception {
        PDMLFileToXMLFile ( Path.of ( PDMLFilePath ), Path.of ( XMLFilePath ) );
    }

    // doesn't close reader nor writer
    public static void pipePDMLReaderToXMLWriter (
        @NotNull Reader pdmlReader,
        @NotNull Writer XMLWriter,
        @Nullable TextResource pdmlTextResource ) throws Exception {

        PdmlParser parser = PdmlParser.create (
            pdmlReader, pdmlTextResource, PdmlParserConfig.defaultConfig() );
        PdmlCodeWalker<NodeTag, String> eventBasedPdmlParser =
            new PdmlCodeWalker<> ( parser, new WriteXMLParserEventHandler ( XMLWriter ) );
        eventBasedPdmlParser.walk ();
    }

    public static @NotNull Document PDMLFileToXMLDocument ( @NotNull Path PDMLFile ) throws Exception {

        return PDMLToXMLDocument ( TextFileReaderUtil.createUTF8FileReader ( PDMLFile ), new File_TextResource ( PDMLFile ) );
    }

    public static @NotNull Document PDMLToXMLDocument (
        @NotNull Reader pdmlReader,
        @Nullable TextResource pdmlTextResource ) throws Exception {

        // long startTimeNanos = System.nanoTime();

        PdmlParser parser = PdmlParser.create (
            pdmlReader, pdmlTextResource, PdmlParserConfig.defaultConfig() );
        PdmlTreeWalkerEventHandler<Node, Document> eventHandler = new CreateDOM_ParserEventHandler();
        PdmlCodeWalker<Node, Document> eventBasedPdmlParser =
            new PdmlCodeWalker<> ( parser, eventHandler );
        eventBasedPdmlParser.walk ();

        /*
        long endTimeNanos = System.nanoTime();
        long time = endTimeNanos - startTimeNanos;
        long micros = TimeUnit.NANOSECONDS.toMicros ( time );
        System.out.println ( "PXMLToXMLDocument time: " + String.valueOf ( micros ) + " microseconds" );
        */

        return eventHandler.getResult();
    }
}
