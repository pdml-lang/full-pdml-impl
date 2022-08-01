package dev.pdml.ext.utilities.xml;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pdml.ext.utilities.writer.PDMLWriterImpl;
import dev.pdml.ext.utilities.writer.PDMLWriter;
import dev.pp.basics.utilities.file.TextFileIO;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.util.Iterator;

public class XMLToPXMLConverter {

    public static void XMLFileToPXMLFile ( @NotNull File XMLFile, @NotNull File pXMLFile ) throws Exception {

        final FileReader XMLFileReader = TextFileIO.getUTF8FileReader ( XMLFile );
        final FileWriter pXMLFileWriter = TextFileIO.getUTF8FileWriter ( pXMLFile );
        pipeXMLReaderToPXMLWriter ( XMLFileReader, pXMLFileWriter );
        XMLFileReader.close();
        pXMLFileWriter.close();
    }

    // doesn't close reader nor writer
    public static void pipeXMLReaderToPXMLWriter ( @NotNull Reader XMLReader, @NotNull Writer pXMLWriter ) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLEventReader XMLEventReader = factory.createXMLEventReader ( XMLReader );
        XMLEventReaderToPXML ( XMLEventReader, pXMLWriter );
    }

    public static void XMLDocumentToPXML (
        @NotNull Document XMLDocument, @NotNull Writer pXMLWriter ) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLEventReader XMLEventReader = factory.createXMLEventReader ( new DOMSource ( XMLDocument ) );
        XMLEventReaderToPXML ( XMLEventReader, pXMLWriter );
    }

    private static void XMLEventReaderToPXML (
        @NotNull XMLEventReader XMLEventReader, @NotNull Writer pXMLWriter ) throws Exception {

        /*
            Using an XMLEventReader instead of SAXParser to read XML has advantages:
                provides better data about events
                optionally provides lineNumber and columnNumber
        */

        PDMLWriter pXMLWriter_ = new PDMLWriterImpl ( pXMLWriter );
        while ( XMLEventReader.hasNext() ) {
            XMLEvent XMLEvent = XMLEventReader.nextEvent();
            handleXMLEvent ( XMLEvent, pXMLWriter_, XMLEventReader );
        }
    }

    private static void handleXMLEvent (
        @NotNull XMLEvent XMLEvent,
        @NotNull PDMLWriter pXMLWriter,
        @NotNull XMLEventReader XMLEventReader ) throws IOException, XMLStreamException {

        if ( XMLEvent.isStartElement() ) {
            handleStartElement ( (StartElement) XMLEvent, pXMLWriter, XMLEventReader.peek().isEndElement() );

        } else if ( XMLEvent.isEndElement() ) {
            pXMLWriter.writeNodeEndSymbol();

        } else if ( XMLEvent.isCharacters() ) {
            pXMLWriter.writeText ( ((Characters) XMLEvent).getData() );

        } else if ( XMLEvent instanceof Comment ) {
            pXMLWriter.writeComment ( ((Comment)XMLEvent).getText() );

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
        @NotNull StartElement XMLEvent, @NotNull PDMLWriter pXMLWriter, boolean isEmptyElement ) throws IOException {

        QName name = XMLEvent.getName();
        if ( isEmptyElement ) {
            pXMLWriter.writeEmptyNode ( name.getPrefix(), name.getLocalPart() );
        } else {
            pXMLWriter.writeNonEmptyNodeStart ( name.getPrefix(), name.getLocalPart() );
            // namespaces are not available as attributes
            // handleNamespacesAndAttributes ( XMLEvent.getNamespaces(), XMLEvent.getAttributes(), pXMLWriter );
            handleNamespaces ( XMLEvent.getNamespaces(), pXMLWriter );
            handleAttributes ( XMLEvent.getAttributes(), pXMLWriter );
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
        @NotNull PDMLWriter pXMLWriter ) throws IOException {

        if ( namespaces == null || !namespaces.hasNext () ) return;

        pXMLWriter.writeNamespacesStart ();

        while ( namespaces.hasNext () ) {
            Namespace namespace = namespaces.next ();

            pXMLWriter.writeNamespace ( namespace.getPrefix (), namespace.getNamespaceURI () );
        }

        pXMLWriter.writeNamespacesEnd ();
    }

    private static void handleAttributes (
        @Nullable Iterator<Attribute> attributes,
        @NotNull PDMLWriter pXMLWriter ) throws IOException {

        if ( attributes == null || ! attributes.hasNext () ) return;

        pXMLWriter.writeAttributesStart();

        while ( attributes.hasNext() ) {
            Attribute attribute = attributes.next();

            QName name = attribute.getName();
            pXMLWriter.writeAttribute(
                name.getPrefix(), name.getLocalPart(), attribute.getValue() ); // , attributes.hasNext() );
        }

        pXMLWriter.writeAttributesEnd();
    }
}
