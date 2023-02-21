package dev.pdml.xml.eventhandlers;

import dev.pdml.data.attribute.MutableNodeAttributes;
import dev.pdml.data.namespace.MutableNodeNamespaces;
import dev.pdml.data.namespace.NodeNamespace;
import dev.pdml.data.node.NodeName;
import dev.pdml.parser.PdmlParserHelper;
import dev.pdml.parser.eventhandler.*;
import dev.pdml.xml.XMLUtilities;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameter.Parameter;
import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Collection;

public class CreateDOM_ParserEventHandler implements PdmlParserEventHandler<Node, Document> {

    private Document document;
    private MutableNodeNamespaces allNamespaces = new MutableNodeNamespaces();


    public CreateDOM_ParserEventHandler() {}

    public void onStart() throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        this.document = docBuilder.newDocument();
        // this.document.setXmlStandalone ( true );
    }

    public void onEnd () {
        // do nothing
    }

    public @NotNull Node onRootNodeStart ( @NotNull NodeStartEvent event ) {

        Element rootElement = createElement ( event.name() );
        addDeclaredNamespaces ( rootElement, event.declaredNamespaces() );
        addAttributes ( event.attributes(), rootElement );
        document.appendChild ( rootElement );
        return rootElement;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull Node rootElement ) {
        // do nothing
    }

    public @NotNull Node onNodeStart ( @NotNull NodeStartEvent event, @NotNull Node parentElement ) {

/*
        if ( ! rootNode.isAttribute() ) {
            Element element = createElement ( rootNode );
            parentElement.appendChild ( element );
            return element;

        } else {
            Attr attribute;
            Element element = (Element) parentElement;
            if ( ! rootNode.hasNamespace() ) {
                attribute = document.createAttribute ( rootNode.getLocalName() );
                element.setAttributeNode ( attribute );
            } else {
                attribute = document.createAttributeNS (
                    rootNode.getNamespaceURI().toString(), rootNode.getName().toString() );
                element.setAttributeNodeNS(attribute);
            }
            return attribute;
        }
*/
        Element element = createElement ( event.name() );
        addDeclaredNamespaces ( element, event.declaredNamespaces() );
        addAttributes ( event.attributes(), element );
        parentElement.appendChild ( element );
        return element;
    }

    private void addDeclaredNamespaces ( Element element, MutableNodeNamespaces namespaces ) {

        if ( namespaces == null ) return;
        Collection<NodeNamespace> list = namespaces.list();
        if ( list == null ) return;

        for ( NodeNamespace namespace : list ) {
            allNamespaces.add ( namespace );
            String value = namespace.URI();
            /* TODO?
            if ( namespace.isDefaultNamespace() ) {
                addAttributeWithoutNamespace ( element, XMLConstants.XMLNS_ATTRIBUTE, value );
            } else {
             */
                String qualifiedName = XMLUtilities.getQualifiedNameForNamespaceAttribute ( namespace.namePrefix() );
                addAttributeWithNamespace (
                    element, XMLConstants.XMLNS_ATTRIBUTE_NS_URI, qualifiedName, value );
            // }
        }
    }

    public void addAttributes ( @Nullable MutableNodeAttributes attributes, @NotNull Element element ) {

        if ( attributes == null ) return;
        Collection<Parameter<String>> list = attributes.list();
        if ( list == null ) return;

        for ( Parameter<String> attribute : list ) {

            String value = attribute.getValue();
/*
            if ( ! attribute.hasNamespace() ) {
                addAttributeWithoutNamespace ( element, attribute.getLocalNameText(), value );
            } else {
                addAttributeWithNamespace (
                    element, attribute.getNamespaceURI().toString(), attribute.getName().qualifiedName (), value );
            }
 */
            addAttributeWithoutNamespace ( element, attribute.getName(), value );
        }
    }


    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull Node element ) {
        // do nothing
    }

/*
    public void onAttributes ( @NotNull NodeAttributes attributes, @NotNull Node parentNode ) throws Exception {

        Element element = (Element) parentNode;

        for ( NodeAttribute attribute : attributes.getList() ) {

            if ( ! attribute.hasNamespace() ) {
                Attr DOMAttribute = document.createAttribute ( attribute.getLocalName() );
                element.setAttributeNode ( DOMAttribute );
            } else {
                Attr DOMAttribute = document.createAttributeNS (
                    attribute.getNamespaceURI().toString(), attribute.getName().fullName() );
                element.setAttributeNodeNS ( DOMAttribute );
            }
        }
    }

    public void onAttributes ( @Nullable ASTNodeAttributes attributes, @NotNull Node parentNode ) throws Exception {

        if ( attributes == null ) return;

        Element element = (Element) parentNode;

        for ( ASTNodeAttribute attribute : attributes.getList() ) {

            String value = attribute.getValueText();
            if ( ! attribute.hasNamespace() ) {
                addAttributeWithoutNamespace ( element, attribute.getLocalNameText(), value );
            } else {
                addAttributeWithNamespace (
                    element, attribute.getNamespaceURI().toString(), attribute.getName().qualifiedName (), value );
            }
        }
    }
 */

    private void addAttributeWithoutNamespace (
        @NotNull Element element, @NotNull String localName, @Nullable String value ) {

        element.setAttribute ( localName, value );
    }

    private void addAttributeWithNamespace (
        @NotNull Element element, @NotNull String URI, @NotNull String fullName, @NotNull String value ) {

        element.setAttributeNS ( URI, fullName, value );
    }

    public void onText ( @NotNull NodeTextEvent text, @NotNull Node parentElement ) {

/*
        if ( parentElement instanceof Element ) {
            Text textElement = document.createTextNode ( text.getText() );
            parentElement.appendChild ( textElement );

        } else if ( parentElement instanceof Attr ) {
            Attr attribute = (Attr) parentElement;
            attribute.setValue ( text.getText() );

        } else {
            throw new RuntimeException ( "Unexpected parentElement " + parentElement );
        }
*/
        if ( parentElement instanceof Element ) {
            Text textElement = document.createTextNode ( text.text() );
            parentElement.appendChild ( textElement );
        } else {
            throw new RuntimeException ( "Unexpected parentElement " + parentElement );
        }
    }

    public void onComment ( @NotNull NodeCommentEvent comment, @NotNull Node parentElement ) {

        String stripped = PdmlParserHelper.stripStartAndEndFromComment ( comment.comment() );
        Comment comment_ = document.createComment ( stripped );
        parentElement.appendChild ( comment_ );
    }


    public @NotNull Document getResult() { return document; }

    private Element createElement ( NodeName name ) {

        @Nullable String prefix = name.namespacePrefix();
        if ( prefix == null ) {
            return document.createElement ( name.localName() );
        } else {
            String URI = allNamespaces.getByPrefix ( prefix ).URI();
            return document.createElementNS(
                URI, name.qualifiedName () );
        }
    }
}
