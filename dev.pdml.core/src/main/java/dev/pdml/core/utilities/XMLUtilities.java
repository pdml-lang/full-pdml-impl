package dev.pdml.core.utilities;

import dev.pp.text.utilities.FileUtilities;
import dev.pdml.core.PDMLConstants;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class XMLUtilities {

    public static @Nullable String getOfficialNamespaceURI ( @NotNull String namespacePrefix ) {

        if ( namespacePrefix.equals ( XMLConstants.XMLNS_ATTRIBUTE ) ) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else if ( namespacePrefix.equals ( XMLConstants.XML_NS_PREFIX ) ) {
            return XMLConstants.XML_NS_URI;
        } else {
            return null;
        }
    }

    public static @NotNull String getQualifiedNameForNamespaceAttribute ( @NotNull String namespacePrefix ) {

        return XMLConstants.XMLNS_ATTRIBUTE + PDMLConstants.NAMESPACE_PREFIX_NAME_SEPARATOR + namespacePrefix;
    }

    public static @NotNull Document XMLFileToXMLDocument ( @NotNull File XMLFile ) throws Exception {
        return readXMLDocument ( FileUtilities.getUTF8FileReader ( XMLFile ) );
    }

    public static @NotNull Document readXMLDocument ( @NotNull Reader reader ) throws Exception {

        // long startTimeNanos = System.nanoTime();

        // DocumentBuilderFactory factory = DocumentBuilderFactory.newNSInstance(); // Java version >= 13
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware ( true ); // important!

        DocumentBuilder builder = factory.newDocumentBuilder();

        /*
        Document result = builder.parse ( new InputSource( reader ) );
        long endTimeNanos = System.nanoTime();
        long time = endTimeNanos - startTimeNanos;
        long micros = TimeUnit.NANOSECONDS.toMicros ( time );
        System.out.println ( "readXMLDocument time " + String.valueOf ( micros ) + " microseconds" );
        return result;
        */

        return builder.parse ( new InputSource( reader ) );
    }

    public static void writeXMLDocumentToFile ( @NotNull Document document, @NotNull File XMLFile ) throws Exception {

        writeXMLDocument ( document, FileUtilities.getUTF8FileWriter ( XMLFile ) );
    }

    public static void writeXMLDocumentToOSOut ( @NotNull Document document ) throws Exception {

        writeXMLDocument ( document, new PrintWriter( System.out ) );
    }

    public static void writeXMLDocument ( @NotNull Document document, @NotNull Writer writer ) throws Exception {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource ( document );

        // transformer.setOutputProperty ( OutputKeys.INDENT, "yes" );
        // transformer.setOutputProperty ( "{http://xml.apache.org/xslt}indent-amount", "4" );
        // transformer.setOutputProperty ( OutputKeys.OMIT_XML_DECLARATION, "yes" );

        transformer.transform ( source, new StreamResult ( writer ) );
    }
}
