package dev.pdml.xml;

import dev.pdml.writer.PdmlWriter;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.file.TextFileIO;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;

public class XMLToPdmlConverter {

    public static void XMLFileToPdmlFile ( @NotNull Path XMLFile, @NotNull Path pXMLFile ) throws Exception {

        final FileReader XMLFileReader = TextFileIO.getUTF8FileReader ( XMLFile );
        final FileWriter pXMLFileWriter = TextFileIO.getUTF8FileWriter ( pXMLFile );
        pipeXMLReaderToPdmlWriter ( XMLFileReader, pXMLFileWriter );
        XMLFileReader.close();
        pXMLFileWriter.close();
    }

    // doesn't close reader nor writer
    public static void pipeXMLReaderToPdmlWriter ( @NotNull Reader XMLReader, @NotNull Writer pXMLWriter ) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLEventReader XMLEventReader = factory.createXMLEventReader ( XMLReader );
        XMLEventReaderToPdml ( XMLEventReader, pXMLWriter );
    }

    public static void XMLDocumentToPdml (
        @NotNull Document XMLDocument, @NotNull Writer pXMLWriter ) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLEventReader XMLEventReader = factory.createXMLEventReader ( new DOMSource ( XMLDocument ) );
        XMLEventReaderToPdml ( XMLEventReader, pXMLWriter );
    }

    private static void XMLEventReaderToPdml (
        @NotNull XMLEventReader XMLEventReader, @NotNull Writer writer ) throws Exception {

        /*
            Using an XMLEventReader instead of SAXParser to read XML has advantages:
                provides better data about events
                optionally provides lineNumber and columnNumber
        */

        PdmlWriter pXMLWriter_ = new PdmlWriter ( writer );
        while ( XMLEventReader.hasNext() ) {
            XMLEvent XMLEvent = XMLEventReader.nextEvent();
            handleXMLEvent ( XMLEvent, pXMLWriter_, XMLEventReader );
        }
    }

    private static void handleXMLEvent (
        @NotNull XMLEvent XMLEvent,
        @NotNull PdmlWriter pdmlWriter,
        @NotNull XMLEventReader XMLEventReader ) throws IOException, XMLStreamException {

        if ( XMLEvent.isStartElement() ) {
            handleStartElement ( (StartElement) XMLEvent, pdmlWriter, XMLEventReader.peek().isEndElement() );

        } else if ( XMLEvent.isEndElement() ) {
            pdmlWriter.writeNodeEndSymbol();

        } else if ( XMLEvent.isCharacters() ) {
            pdmlWriter.writeText ( ((Characters) XMLEvent).getData(), true );

        } else if ( XMLEvent instanceof Comment ) {
            pdmlWriter.writeComment ( ((Comment)XMLEvent).getText() );

        } else if ( XMLEvent.isStartDocument() ) {
            // pXMLWriter.startDocument();

        } else if ( XMLEvent.isEndDocument() ) {
            // pXMLWriter.endDocument();

        // TODO add other events (ProcessingInstruction, etc.)

        } else {
            throw new RuntimeException ( "Event '" + XMLEvent + "' is not yet supported." );
        }

    }

    private static void handleStartElement (
        @NotNull StartElement XMLEvent, @NotNull PdmlWriter pdmlWriter, boolean isEmptyElement ) throws IOException {

        QName name = XMLEvent.getName();
        if ( isEmptyElement ) {
            pdmlWriter.writeEmptyNode ( name.getPrefix(), name.getLocalPart() );
        } else {
            pdmlWriter.writeNonEmptyNodeStart ( name.getPrefix(), name.getLocalPart() );
            // namespaces are not available as attributes
            // handleNamespacesAndAttributes ( XMLEvent.getNamespaces(), XMLEvent.getAttributes(), pXMLWriter );
            handleNamespaces ( XMLEvent.getNamespaces(), pdmlWriter );
            handleAttributes ( XMLEvent.getAttributes(), pdmlWriter );
        }
    }

/*
    private static void handleNamespacesAndAttributes (
        @Nullable Iterator<Namespace> namespaces,
        @Nullable Iterator<Attribute> attributes,
        @NotNull PXMLWriter pXMLWriter ) throws IOException {

        boolean hasNamespaces = namespaces != null && namespaces.hasNext();
        boolean hasAttributes = attributes != null && attributes.hasNext();

        if ( ! hasNamespaces && ! hasAttributes ) return;

        pXMLWriter.writeAttributesStart();

        // write namespaces as attributes
        if ( hasNamespaces ) {
            while ( namespaces.hasNext() ) {
                Namespace namespace = namespaces.next();

                pXMLWriter.writeAttribute(
                    XMLConstants.XMLNS_ATTRIBUTE, namespace.getPrefix(), namespace.getNamespaceURI() );
                    // namespaces.hasNext() || ( attributes != null && attributes.hasNext() ) );
            }
        }

        if ( hasAttributes ) {
            while ( attributes.hasNext() ) {
                Attribute attribute = attributes.next();

                QName name = attribute.getName();
                pXMLWriter.writeAttribute(
                    name.getPrefix(), name.getLocalPart(), attribute.getValue() ); // , attributes.hasNext() );
            }
        }

        pXMLWriter.writeAttributesEnd();
    }
*/
    private static void handleNamespaces (
        @Nullable Iterator<Namespace> namespaces,
        @NotNull PdmlWriter pdmlWriter ) throws IOException {

        if ( namespaces == null || !namespaces.hasNext () ) return;

        pdmlWriter.writeNamespacesStart ();

        while ( namespaces.hasNext () ) {
            Namespace namespace = namespaces.next ();

            pdmlWriter.writeNamespace ( namespace.getPrefix (), namespace.getNamespaceURI () );
        }

        pdmlWriter.writeNamespacesEnd ();
    }

    private static void handleAttributes (
        @Nullable Iterator<Attribute> attributes,
        @NotNull PdmlWriter pdmlWriter ) throws IOException {

        if ( attributes == null || ! attributes.hasNext () ) return;

        pdmlWriter.writeAttributesStart();

        while ( attributes.hasNext() ) {
            Attribute attribute = attributes.next();

            QName name = attribute.getName();
            assert name.getPrefix() == null;
            pdmlWriter.writeAttribute(
                name.getLocalPart(), attribute.getValue(), false ); // , attributes.hasNext() );
        }

        pdmlWriter.writeAttributesEnd();
    }
}
