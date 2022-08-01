package dev.pdml.ext.utilities.parser.eventhandlers;

import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.parser.PDMLParserHelper;
import dev.pdml.core.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.parser.eventHandler.PDMLParserEventHandler;
import dev.pdml.ext.utilities.xml.XMLUtilities;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.token.TextToken;
import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CreateDOM_ParserEventHandler implements PDMLParserEventHandler<Node, Document> {

    Document document;

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

        Element rootElement = createElement ( event.getName() );
        addDeclaredNamespaces ( rootElement, event.getDeclaredNamespaces() );
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
        Element element = createElement ( event.getName() );
        addDeclaredNamespaces ( element, event.getDeclaredNamespaces() );
        parentElement.appendChild ( element );
        return element;
    }

    private void addDeclaredNamespaces ( Element element, ASTNamespaces namespaces ) {

        if ( namespaces == null ) return;

        for ( ASTNamespace namespace : namespaces.getList() ) {
            String value = namespace.getURIToken ().toString();
            if ( namespace.isDefaultNamespace() ) {
                addAttributeWithoutNamespace ( element, XMLConstants.XMLNS_ATTRIBUTE, value );
            } else {
                String qualifiedName = XMLUtilities.getQualifiedNameForNamespaceAttribute ( namespace.getPrefixText() );
                addAttributeWithNamespace (
                    element, XMLConstants.XMLNS_ATTRIBUTE_NS_URI, qualifiedName, value );
            }

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
*/
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

    private void addAttributeWithoutNamespace (
        @NotNull Element element, @NotNull String localName, @NotNull String value ) {

        element.setAttribute ( localName, value );
    }

    private void addAttributeWithNamespace (
        @NotNull Element element, @NotNull String URI, @NotNull String fullName, @NotNull String value ) {

        element.setAttributeNS ( URI, fullName, value );
    }

    public void onText ( @NotNull TextToken text, @NotNull Node parentElement ) {

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
            Text textElement = document.createTextNode ( text.getText() );
            parentElement.appendChild ( textElement );
        } else {
            throw new RuntimeException ( "Unexpected parentElement " + parentElement );
        }
    }

    public void onComment ( @NotNull TextToken comment, @NotNull Node parentElement ) {

        String stripped = PDMLParserHelper.stripStartAndEndFromComment ( comment.getText() );
        Comment comment_ = document.createComment ( stripped );
        parentElement.appendChild ( comment_ );
    }

    @NotNull
    public Document getResult() {
        return document;
    }

    private Element createElement ( ASTNodeName name ) {

        if ( ! name.hasNamespace() ) {
            return document.createElement ( name.getLocalNameText() );
        } else {
            return document.createElementNS(
                name.getNamespaceURIText ().toString(), name.qualifiedName () );
        }
    }
}
