package dev.pdml.core.reader.parser.eventHandler.impls;

import dev.pdml.core.PDMLConstants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.reader.parser.ParserHelper;
import dev.pdml.core.reader.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.reader.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class WriteXML_ParserEventHandler implements ParserEventHandler<ASTNodeName, String> {

    private final XMLStreamWriter XMLWriter;


    public WriteXML_ParserEventHandler ( Writer writer ) throws XMLStreamException {

        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        // factory.setProperty ( XMLOutputFactory.IS_REPAIRING_NAMESPACES, true );
        this.XMLWriter = factory.createXMLStreamWriter ( writer );
    }

    public WriteXML_ParserEventHandler ( XMLStreamWriter XMLWriter ) {

        this.XMLWriter = XMLWriter;
    }


    public void onStart() throws XMLStreamException {

        XMLWriter.writeStartDocument ( StandardCharsets.UTF_8.name(), "1.0" );
        XMLWriter.writeCharacters ( PDMLConstants.NEW_LINE );
    }

    public void onEnd () throws XMLStreamException {

        XMLWriter.writeEndDocument();
        XMLWriter.writeCharacters ( PDMLConstants.NEW_LINE );
    }

    public @NotNull
    ASTNodeName onRootNodeStart ( @NotNull NodeStartEvent event ) throws Exception {

        return onNodeStart_ ( event );
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNodeName node ) throws XMLStreamException {

        onNodeEnd ( event, node );
    }

    public @NotNull
    ASTNodeName onNodeStart ( @NotNull NodeStartEvent event, @Nullable ASTNodeName parentNode ) throws Exception {

        return onNodeStart_ ( event );
    }

    private @NotNull
    ASTNodeName onNodeStart_ ( @NotNull NodeStartEvent event ) throws Exception {

        ASTNodeName name = event.getName();
        String localName = name.getLocalNameText();

        if ( ! name.hasNamespace() ) {
            if ( event.isEmptyNode() ) {
                XMLWriter.writeEmptyElement ( localName );
            } else {
                XMLWriter.writeStartElement ( localName );
            }

        } else {
            String namespacePrefix = name.getNamespacePrefixText ();
            String URI = name.getNamespaceURIText();
            if ( event.isEmptyNode() ) {
                XMLWriter.writeEmptyElement ( namespacePrefix, localName, URI );
            } else {
                XMLWriter.writeStartElement ( namespacePrefix, localName, URI );
            }
        }

        if ( event.getDeclaredNamespaces() != null ) {
            for ( ASTNamespace namespace : event.getDeclaredNamespaces().getList() ) {

                String value = namespace.getURIToken ().toString();
                if ( namespace.isDefaultNamespace() ) {
                    XMLWriter.writeAttribute (
                        XMLConstants.XMLNS_ATTRIBUTE,
                        value );
                } else {
                    XMLWriter.writeAttribute (
                        XMLConstants.XMLNS_ATTRIBUTE,
                        XMLConstants.XML_NS_URI,
                        namespace.getPrefixText(),
                        value );
                }
            }
        }

        return name;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNodeName name ) throws XMLStreamException {

        if ( ! event.isEmptyNode() ) {
            XMLWriter.writeEndElement();
        }
    }

    public void onAttributes ( @NotNull ASTNodeAttributes attributes, @NotNull ASTNodeName parentNode ) throws Exception {

        for ( ASTNodeAttribute attribute : attributes.getList() ) {
            if ( ! attribute.hasNamespace() ) {
                XMLWriter.writeAttribute ( attribute.getLocalNameText(), attribute.getValueText() );
            } else {
                XMLWriter.writeAttribute (
                    attribute.getNamespacePrefixText(),
                    attribute.getNamespaceURIText(),
                    attribute.getLocalNameText(),
                    attribute.getValueText() );
            }
        }
    }

    public void onText ( @NotNull TextToken text, @NotNull ASTNodeName parentNode )
        throws XMLStreamException {

        XMLWriter.writeCharacters ( text.getText() );
    }

    public void onComment ( @NotNull TextToken comment, @NotNull ASTNodeName parentNode )
        throws XMLStreamException {

        XMLWriter.writeComment ( ParserHelper.stripStartAndEndFromComment ( comment.getText() ) );
    }

    public @NotNull String getResult() { return "foo"; }
}
