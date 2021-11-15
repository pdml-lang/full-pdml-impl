package dev.pdml.core.reader.parser.eventHandler.impls;

import dev.pdml.core.reader.parser.ParserHelper;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.FileUtilities;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.data.AST.namespace.ASTNamespace;
import dev.pdml.core.data.AST.namespace.ASTNamespaces;
import dev.pdml.core.reader.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.reader.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pdml.core.writer.DefaultPXMLWriter;
import dev.pdml.core.writer.PXMLWriter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class WriteStandalone_ParserEventHandler implements ParserEventHandler<ASTNodeName, ASTNodeName> {

    private final PXMLWriter writer;

    private ASTNodeName rootNodeName;


    public WriteStandalone_ParserEventHandler ( PXMLWriter writer ) {

        this.writer = writer;
        this.rootNodeName = null;
    }

    public WriteStandalone_ParserEventHandler ( Writer writer ) {

        this ( new DefaultPXMLWriter ( writer ) );
    }

    public WriteStandalone_ParserEventHandler ( @NotNull File file ) throws IOException {

        this ( FileUtilities.getUTF8FileWriter ( file ) );
    }


    public void onStart() throws IOException {

        writer.startDocument();
    }

    public void onEnd () throws IOException {

        writer.endDocument();
    }

    public @NotNull
    ASTNodeName onRootNodeStart ( @NotNull NodeStartEvent event ) throws IOException {

        rootNodeName = event.getName();
        return onNodeStart_ ( event );
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNodeName rootNode ) throws IOException {

        onNodeEnd ( event, rootNode );
    }

    public @NotNull
    ASTNodeName onNodeStart (
        @NotNull NodeStartEvent event, @NotNull ASTNodeName parentName ) throws IOException {

        return onNodeStart_ ( event );
    }

    private @NotNull
    ASTNodeName onNodeStart_ ( @NotNull NodeStartEvent event ) throws IOException {

        ASTNodeName name = event.getName();
        if ( event.isEmptyNode() ) {
            writer.writeEmptyNode ( name.getNamespacePrefixText (), name.getLocalNameText() );
        } else {
            writer.writeNonEmptyNodeStart ( name.getNamespacePrefixText (), name.getLocalNameText() );
            writeDeclaredNamespaces ( event.getDeclaredNamespaces() );
        }
        return name;
    }

    private void writeDeclaredNamespaces ( @Nullable ASTNamespaces namespaces ) throws IOException {

        if ( namespaces == null ) return;

        writer.writeNamespacesStart();
        for ( ASTNamespace namespace : namespaces.getList() ) {
            writer.writeNamespace ( namespace );
        }
        writer.writeNamespacesEnd();
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNodeName name ) throws IOException {

        if ( ! event.isEmptyNode() ) writer.writeNodeEndSymbol();
    }

    public void onAttributes ( @NotNull ASTNodeAttributes attributes, @NotNull ASTNodeName parentNode ) throws Exception {

        writer.writeAttributesStart();
        for ( ASTNodeAttribute attribute : attributes.getList() ) {
            writer.writeAttribute ( attribute );
        }
        writer.writeAttributesEnd();
    }

    public void onText ( @NotNull TextToken text, @NotNull ASTNodeName parentNode ) throws IOException {

        writer.escapeAndWriteText ( text.getText() );
    }

    public void onComment ( @NotNull TextToken comment, @NotNull ASTNodeName parentNode ) throws IOException {

        writer.writeComment ( ParserHelper.stripStartAndEndFromComment ( comment.getText() ) );
    }

    public @NotNull ASTNodeName getResult() {
        return rootNodeName;
    }
}
