package dev.pdml.ext.utilities.xml;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.basics.utilities.file.TextFileIO;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.Reader;
import java.nio.file.Path;

public class XMLSchemaValidator {

    public static void validateXMLDataWithXMLSchema (
        @NotNull Reader XMLDataReader, @NotNull Reader XMLSchemaReader ) throws Exception {

        validateDocumentWithSchemaDocument (
            XMLUtilities.readXMLDocument ( XMLDataReader ),
            XMLUtilities.readXMLDocument ( XMLSchemaReader ) );
    }

    public static void validateXMLDataWithPXMLSchema (
        @NotNull Reader XMLDataReader, @NotNull Reader pXMLSchemaReader, @Nullable TextResource pXMLSchemaResource ) throws Exception {

        validateDocumentWithSchemaDocument (
            XMLUtilities.readXMLDocument ( XMLDataReader ),
            PDMLToXMLConverter.PDMLToXMLDocument ( pXMLSchemaReader, pXMLSchemaResource ) );
    }

    public static void validatePXMLDataWithXMLSchema (
        @NotNull Reader pXMLDataReader,
        @Nullable TextResource pXMLDataResource,
        @NotNull Reader XMLSchemaReader ) throws Exception {

        validateDocumentWithSchemaDocument (
            PDMLToXMLConverter.PDMLToXMLDocument ( pXMLDataReader, pXMLDataResource ),
            XMLUtilities.readXMLDocument ( XMLSchemaReader ) );
    }

    public static void validatePXMLFileWithPXMLSchemaFile (
        @NotNull Path pXMLDataFile, @NotNull Path pXMLSchemaFile ) throws Exception {

        validatePXMLDataWithPXMLSchema (
            TextFileIO.getUTF8FileReader ( pXMLDataFile ), new File_TextResource ( pXMLDataFile ),
            TextFileIO.getUTF8FileReader ( pXMLSchemaFile ), new File_TextResource ( pXMLSchemaFile ) );
    }

    public static void validatePXMLDataWithPXMLSchema (
        @NotNull Reader pXMLDataReader,
        @Nullable TextResource pXMLDataResource,
        @NotNull Reader pXMLSchemaReader,
        @Nullable TextResource pXMLSchemaResource ) throws Exception {

        validateDocumentWithSchemaDocument (
            PDMLToXMLConverter.PDMLToXMLDocument ( pXMLDataReader, pXMLDataResource ),
            PDMLToXMLConverter.PDMLToXMLDocument ( pXMLSchemaReader, pXMLSchemaResource ) );
    }

    public static void validateDocumentWithSchemaDocument (
        @NotNull Document XMLDocument, @NotNull Document XMLSchemaDocument ) throws Exception {

        SchemaFactory factory = SchemaFactory.newInstance ( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        Schema schema = factory.newSchema ( new DOMSource( XMLSchemaDocument ) );
        javax.xml.validation.Validator validator = schema.newValidator();
        validator.validate ( new DOMSource ( XMLDocument ) );
    }
}
