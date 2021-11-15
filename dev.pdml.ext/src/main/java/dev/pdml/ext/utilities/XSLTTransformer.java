package dev.pdml.ext.utilities;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.utilities.FileUtilities;
import org.w3c.dom.Document;
import dev.pdml.core.utilities.XMLUtilities;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.Reader;
import java.io.Writer;

public class XSLTTransformer {

    public static void transformXMLWithXMLXSLT (
        @NotNull Reader XMLDataReader, @NotNull Reader XMLXSLTReader, @NotNull Writer outputWriter ) throws Exception {

        transformWithDocuments (
            XMLUtilities.readXMLDocument ( XMLDataReader ),
            XMLUtilities.readXMLDocument ( XMLXSLTReader ),
            outputWriter );
    }

    public static void transformXMLWithPXMLXSLT (
        @NotNull Reader XMLDataReader,
        @NotNull Reader pXMLXSLTReader, @Nullable TextResource pXMLXSLTResource,
        @NotNull Writer outputWriter ) throws Exception {

        transformWithDocuments (
            XMLUtilities.readXMLDocument ( XMLDataReader ),
            PXMLToXMLConverter.pXMLToXMLDocument ( pXMLXSLTReader, pXMLXSLTResource ),
            outputWriter );
    }

    public static void transformPXMLWithXMLXSLT (
        @NotNull Reader pXMLDataReader, @Nullable TextResource pXMLDataResource,
        @NotNull Reader XMLXSLTReader,
        @NotNull Writer outputWriter ) throws Exception {

        transformWithDocuments (
            PXMLToXMLConverter.pXMLToXMLDocument ( pXMLDataReader, pXMLDataResource ),
            XMLUtilities.readXMLDocument ( XMLXSLTReader ),
            outputWriter );
    }

    public static void transformPXMLFileWithPXMLXSLTFile (
        @NotNull File pXMLFile, @NotNull File pXMLXSLTFile, @NotNull File outputFile ) throws Exception {

        transformPXMLWithPXMLXSLT (
            FileUtilities.getUTF8FileReader ( pXMLFile ), new File_TextResource ( pXMLFile ),
            FileUtilities.getUTF8FileReader ( pXMLXSLTFile ), new File_TextResource ( pXMLXSLTFile ),
            FileUtilities.getUTF8FileWriter ( outputFile ) );
    }

    public static void transformPXMLWithPXMLXSLT (
        @NotNull Reader pXMLDataReader, @Nullable TextResource pXMLDataResource,
        @NotNull Reader pXMLXSLTReader, @Nullable TextResource pXMLXSLTResource,
        @NotNull Writer outputWriter ) throws Exception {

        transformWithDocuments (
            PXMLToXMLConverter.pXMLToXMLDocument ( pXMLDataReader, pXMLDataResource ),
            PXMLToXMLConverter.pXMLToXMLDocument ( pXMLXSLTReader, pXMLXSLTResource ),
            outputWriter );
    }

    public static void transformWithDocuments (
        @NotNull Document XMLDocument, @NotNull Document XSLTDocument, @NotNull Writer outputWriter )
        throws Exception {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer ( new DOMSource ( XSLTDocument ) );
        transformer.transform ( new DOMSource ( XMLDocument ), new StreamResult ( outputWriter ) );
    }
}
