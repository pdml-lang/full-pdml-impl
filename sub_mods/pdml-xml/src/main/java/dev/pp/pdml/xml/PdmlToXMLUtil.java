package dev.pp.pdml.xml;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.parser.PdmlParserUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class PdmlToXMLUtil {


    // Basic Methods

    public static @NotNull Document treeToTree (
        @NotNull TaggedNode pdmlRootNode ) throws ParserConfigurationException {

        return new PdmlTreeToXMLTreeConverter().convert ( pdmlRootNode );
    }

    public static @NotNull Document readerToTree (
        @NotNull TextResourceReader pdmlCodeReader,
        @NotNull PdmlParserConfig parserConfig )
            throws IOException, PdmlException, ParserConfigurationException {

        @NotNull TaggedNode pdmlRootNode = PdmlParserUtil.parseReader (
            pdmlCodeReader, parserConfig );
        return treeToTree ( pdmlRootNode );
    }

    public static void treeToWriter (
        @NotNull TaggedNode pdmlRootNode,
        @NotNull TextResourceWriter xmlCodeWriter )
            throws ParserConfigurationException, TransformerException {
        // boolean usePrettyPrinting

        Document xmlDocument = treeToTree ( pdmlRootNode );
        XMLUtilities.writeXMLDocument ( xmlDocument, xmlCodeWriter );
    }

    public static void readerToWriter (
        @NotNull TextResourceReader pdmlCodeReader,
        @NotNull TextResourceWriter xmlCodeWriter,
        @NotNull PdmlParserConfig parserConfig )
            throws IOException, PdmlException, ParserConfigurationException, TransformerException {
        // boolean removeWhitespaceNodes,
        // boolean usePrettyPrinting ) throws IOException, PdmlException,JsonProcessingException {

        @NotNull Document xmlDocument = readerToTree (
            pdmlCodeReader, parserConfig );
        XMLUtilities.writeXMLDocument ( xmlDocument, xmlCodeWriter );
    }

/*
    // Convenience Methods

    public static @NotNull String treeToCode (
        @NotNull TaggedNode pdmlRootNode,
        boolean usePrettyPrinting ) throws JsonProcessingException {

        try ( StringWriter stringWriter = new StringWriter();
            TextResourceWriter jsonCodeWriter = new TextResourceWriter ( stringWriter, null ) ) {
            treeToWriter ( pdmlRootNode, jsonCodeWriter, usePrettyPrinting );
            return stringWriter.toString();
        } catch ( IOException e ) {
            // should never happen
            throw new RuntimeException ( e );
        }
    }


    private static void writeJsonTree (
        @NotNull ObjectNode jsonObjectNode,
        @NotNull TextResourceWriter jsonCodeWriter,
        boolean usePrettyPrinting ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        if ( usePrettyPrinting ) {
            objectMapper.writerWithDefaultPrettyPrinter();
        }

        objectMapper.writeValue ( jsonCodeWriter.getWriter(), jsonObjectNode );
    }
 */
}
