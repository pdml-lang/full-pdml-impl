package dev.pp.pdml.utils.treewalker;

import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.data.nodespec.PdmlNodeSpec;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.data.nodespec.PdmlNodeSpecs;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.reader.CharReader;
import dev.pp.core.text.resource.TextResource;
import dev.pp.pdml.utils.treewalker.handler.*;

import java.io.IOException;
import java.io.Reader;


public class PdmlCodeWalker<N, R> {

    private final @NotNull PdmlParser parser;
    public @NotNull PdmlParser getPdmlParser() { return parser; }

    private final @NotNull PdmlReader reader;
    private final @NotNull PdmlTreeWalkerEventHandler<N, R> eventHandler;
    private final @Nullable PdmlNodeSpecs nodeSpecs;


    public PdmlCodeWalker (
        @NotNull PdmlParser parser,
        @NotNull PdmlTreeWalkerEventHandler<N, R> eventHandler ) {

        this.parser = parser;
        this.reader = parser.getPdmlReader();
        this.eventHandler = eventHandler;
        this.nodeSpecs = parser.getConfig().getNodeSpecs();

        this.parser.setOptimizeTypedNodes ( false );
    }

    public PdmlCodeWalker (
        @NotNull CharReader charReader,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N, R> eventHandler ) throws IOException {

        this ( PdmlParser.create ( charReader, parserConfig ), eventHandler );
    }

    public PdmlCodeWalker (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N, R> eventHandler ) throws IOException {

        this ( PdmlParser.create ( reader, textResource, lineOffset, columnOffset, parserConfig ), eventHandler );
    }

    public PdmlCodeWalker (
        @NotNull Reader reader,
        @Nullable TextResource textResource,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull PdmlTreeWalkerEventHandler<N, R> eventHandler ) throws IOException {

        this ( reader, textResource, null, null, parserConfig, eventHandler );
    }


    // TODO public boolean walk()
    public void walk() throws IOException, PdmlException {

        eventHandler.onStart();
        requireRootNode();
        eventHandler.onEnd();
    }

    private void requireRootNode() throws IOException, PdmlException {

        parser.skipWhitespaceBeforeRootNode();
        requireTaggedNode ( null, true );
        if ( ! parser.getConfig().getIgnoreTextAfterRootNodeEnd() ) {
            parser.requireDocumentEnd();
        }
    }

    private void requireTaggedNode ( N handlerParentNode, boolean isRootNode )
        throws IOException, PdmlException {

        @NotNull TaggedNode taggedNode = parser.requireTaggedNodeStartUntilAttributes ();
        // PdmlNodeSpec<?> nodeSpec = taggedNode.getSpec();
        PdmlNodeSpec nodeSpec = nodeSpecs == null ? null : nodeSpecs.getOrNull ( taggedNode.getTag () );
        boolean isEmptyNode = taggedNode.isEmpty() &&
            reader.isAtChar ( CorePdmlConstants.NODE_END_CHAR );

        TaggedNodeStartEvent startEvent = new TaggedNodeStartEvent (
            taggedNode.getStartLocation(),
            taggedNode.getTag (),
            taggedNode.getNamespaceDefinitions(),
            taggedNode.getStringAttributes(), isEmptyNode, nodeSpec );
        N handlerNode;
        if ( isRootNode ) {
            handlerNode = eventHandler.onRootNodeStart ( startEvent );
        } else {
            handlerNode = eventHandler.onTaggedNodeStart ( startEvent, handlerParentNode );
        }

        parseChildNodes ( startEvent, handlerNode );

        requireNodeEnd ( taggedNode, startEvent, isRootNode, handlerNode );
    }

    private void requireNodeEnd (
        @NotNull TaggedNode taggedNode,
        @NotNull TaggedNodeStartEvent startEvent,
        boolean isRootNode,
        N handlerParentNode ) throws IOException, PdmlException {

        parser.requireNodeEnd ( taggedNode );

        TaggedNodeEndEvent event = new TaggedNodeEndEvent ( taggedNode.getStartLocation (), startEvent );
        if ( isRootNode ) {
            eventHandler.onRootNodeEnd ( event, handlerParentNode );
        } else {
            eventHandler.onTaggedNodeEnd ( event, handlerParentNode );
        }
    }

    private void parseChildNodes ( @NotNull TaggedNodeStartEvent startEvent, N handlerParentNode )
        throws IOException, PdmlException {

        while ( reader.isNotAtEnd() ) {

            if ( reader.isAtNodeEnd() ) {
                return;

            } else if ( reader.isAtNodeStart() ) {
                requireTaggedNode ( handlerParentNode, false );

            } else {
                try {
                    parser.parseTextsCommentsAndExtensions (
                        ( text, location ) -> {
                            TextEvent event = new TextEvent ( text, location, startEvent );
                            try {
                                eventHandler.onText ( event, handlerParentNode );
                            } catch ( IOException | PdmlException e ) {
                                throw new RuntimeException ( e );
                            }
                        },
                        ( comment, location ) -> {
                            CommentEvent event = new CommentEvent ( comment, location, startEvent );
                            try {
                                eventHandler.onComment ( event, handlerParentNode );
                            } catch ( IOException | PdmlException e ) {
                                throw new RuntimeException ( e );
                            }
                        } );

                } catch ( RuntimeException re ) {
                    if ( re.getCause() instanceof IOException ioe ) {
                        throw ioe;
                    } else if ( re.getCause() instanceof PdmlException pe ) {
                        throw pe;
                    } else {
                        throw re;
                    }
                }
            }
        }
    }
}
