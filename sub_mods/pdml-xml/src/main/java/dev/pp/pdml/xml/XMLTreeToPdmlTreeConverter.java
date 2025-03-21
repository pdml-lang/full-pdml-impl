package dev.pp.pdml.xml;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.core.basics.annotations.NotNull;
import org.w3c.dom.*;

public class XMLTreeToPdmlTreeConverter {

    // TODO namespace declarations

    public XMLTreeToPdmlTreeConverter(){}


    public @NotNull TaggedNode convert ( @NotNull Document xmlDocument ) {

        return convertElement ( xmlDocument.getDocumentElement() );
    }

    private @NotNull TaggedNode convertElement ( @NotNull Element xmlElement ) {

        NodeTag nodeName = new NodeTag (
            xmlElement.getLocalName(), xmlElement.getPrefix() );
        TaggedNode taggedNode = new TaggedNode ( nodeName, null );

        addAttributes ( xmlElement, taggedNode );
        appendChildNodes ( xmlElement, taggedNode );

        return taggedNode;
    }

    private void addAttributes (
        @NotNull Element xmlElement,
        @NotNull TaggedNode taggedNode ) {

        if ( ! xmlElement.hasAttributes() ) {
            return;
        }

        NamedNodeMap namedNodeMap = xmlElement.getAttributes();
        for ( int i = 0; i < namedNodeMap.getLength(); i++ ) {
            Node xmlNode = namedNodeMap.item ( i );

            if ( xmlNode instanceof Attr xmlAttribute ) {
                taggedNode.addAttribute (
                    xmlAttribute.getName(), xmlAttribute.getValue() );
            } else {
                throw new IllegalStateException (
                    "XML nodes of type '" + xmlNode.getClass() + "' are not supported for attributes." );
            }
        }
    }

    private void appendChildNodes (
        @NotNull Element xmlElement,
        @NotNull TaggedNode taggedNode ) {

        if ( ! xmlElement.hasChildNodes() ) {
            return;
        }

        NodeList nodeList = xmlElement.getChildNodes();
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Node xmlNode = nodeList.item ( i );

            if ( xmlNode instanceof CharacterData characterData ) {
                if ( xmlNode instanceof Comment comment ) {
                    taggedNode.appendComment ( comment.getData() );
                } else {
                    // it's text or CDATA
                    taggedNode.appendText ( characterData.getData() );
                }

            } else if ( xmlNode instanceof Element childElement ) {
                taggedNode.appendChild ( convertElement ( childElement ) );

            } else {
                throw new IllegalStateException (
                    "XML nodes of type '" + xmlNode.getClass() + "' are not yet supported." );
            }
        }
    }
}
