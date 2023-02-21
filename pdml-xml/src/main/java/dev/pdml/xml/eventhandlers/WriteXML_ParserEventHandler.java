package dev.pdml.xml.eventhandlers;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.parser.eventhandler.*;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.parameters.parameter.Parameter;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class WriteXML_ParserEventHandler implements PdmlParserEventHandler<NodeName, String> {

    private final XMLStreamWriter XMLWriter;
    private MutableNodeNamespaces allNamespaces = new MutableNodeNamespaces();


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
        XMLWriter.writeCharacters ( StringConstants.OS_NEW_LINE );
    }

    public void onEnd () throws XMLStreamException {

        XMLWriter.writeEndDocument();
        XMLWriter.writeCharacters ( StringConstants.OS_NEW_LINE );
    }

    public @NotNull NodeName onRootNodeStart ( @NotNull NodeStartEvent event ) throws Exception {
        return onNodeStart_ ( event );
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull NodeName node ) throws XMLStreamException {
        onNodeEnd ( event, node );
    }

    public @NotNull NodeName onNodeStart ( @NotNull NodeStartEvent event, @Nullable NodeName parentNode ) throws Exception {
        return onNodeStart_ ( event );
    }

    private @NotNull NodeName onNodeStart_ ( @NotNull NodeStartEvent event ) throws Exception {

        addNamespaces ( event );

        NodeName name = event.name();
        String localName = name.localName();

        String namespacePrefix = name.namespacePrefix();
        if ( namespacePrefix == null ) {
            if ( event.isEmptyNode() ) {
                XMLWriter.writeEmptyElement ( localName );
            } else {
                XMLWriter.writeStartElement ( localName );
            }

        } else {
            String URI = allNamespaces.getByPrefix ( namespacePrefix ).URI();
            if ( event.isEmptyNode() ) {
                XMLWriter.writeEmptyElement ( namespacePrefix, localName, URI );
            } else {
                XMLWriter.writeStartElement ( namespacePrefix, localName, URI );
            }
        }

        writeDeclaredNamespaces ( event.declaredNamespaces() );
        writeAttributes ( event.attributes() );

        return name;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull NodeName name ) throws XMLStreamException {

        if ( ! event.isEmptyNode() ) {
            XMLWriter.writeEndElement();
        }
    }

    public void onText ( @NotNull NodeTextEvent text, @NotNull NodeName parentNode )
        throws XMLStreamException {

        XMLWriter.writeCharacters ( text.text() );
    }

    public void onComment ( @NotNull NodeCommentEvent comment, @NotNull NodeName parentNode )
        throws XMLStreamException {

        XMLWriter.writeComment ( PdmlParserHelper.stripStartAndEndFromComment ( comment.comment() ) );
    }

    public @NotNull String getResult() { return "foo"; }


    private void addNamespaces ( @NotNull NodeStartEvent event ) {

        MutableNodeNamespaces namespaces = event.declaredNamespaces();
        if ( namespaces == null ) return;
        Collection<NodeNamespace> list = namespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace namespace : list ) {
            allNamespaces.add ( namespace );
        }
    }

    private void writeDeclaredNamespaces ( MutableNodeNamespaces namespaces )
        throws XMLStreamException {

        if ( namespaces == null ) return;
        Collection<NodeNamespace> list = namespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace namespace : list ) {
            allNamespaces.add ( namespace );
            String value = namespace.URI();
            // TODO?
            // if ( namespace.isDefaultNamespace() ) {
            if ( false ) {
                XMLWriter.writeAttribute (
                    XMLConstants.XMLNS_ATTRIBUTE,
                    value );
            } else {
                XMLWriter.writeAttribute (
                    XMLConstants.XMLNS_ATTRIBUTE,
                    XMLConstants.XML_NS_URI,
                    namespace.namePrefix(),
                    value );
            }
        }
    }

    public void writeAttributes ( @Nullable MutableNodeAttributes attributes )
        throws XMLStreamException {

        if ( attributes == null ) return;
        Collection<Parameter<String>> list = attributes.list();
        if ( list == null ) return;

        for ( Parameter<String> attribute : list ) {
            /*
            if ( ! attribute.hasNamespace() ) {
                XMLWriter.writeAttribute ( attribute.getLocalNameText(), attribute.getValueText() );
            } else {
                XMLWriter.writeAttribute (
                    attribute.getNamespacePrefixText(),
                    attribute.getNamespaceURIText(),
                    attribute.getLocalNameText(),
                    attribute.getValueText() );
            }
             */
            XMLWriter.writeAttribute ( attribute.getName(), attribute.getValue() );
        }
    }
}
