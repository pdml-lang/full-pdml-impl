package dev.pp.pdml.ext.types;

import dev.pp.pdml.data.CorePdmlConstants;
import dev.pp.pdml.data.exception.InvalidPdmlDataException;
import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.namespace.NodeNamespace;
import dev.pp.pdml.data.node.NodeTag;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParser;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.token.TextToken;

import java.io.IOException;

public interface PdmlType<T> {


    record ObjectTokenPair<T> (
        @Nullable T object,
        @Nullable TextToken textToken
    ) {}


    NodeNamespace NAMESPACE = new NodeNamespace (
        "t", "https://www.pdml-lang.dev/extensions/types" );


    @NotNull String getName();

    boolean isNullAllowed();

    default @NotNull NodeTag nodeName() {
        return new NodeTag ( getName(), NAMESPACE.namePrefix() );
    }

    @NotNull ObjectTokenPair<T> parseObject (
        @NotNull PdmlParser pdmlParser ) throws IOException, PdmlException;

    void validateObject (
        @NotNull ObjectTokenPair<T> objectTokenPair ) throws InvalidPdmlDataException;

    void handleObject (
        @NotNull ObjectTokenPair<T> objectTokenPair,
        @Nullable TaggedNode parentNode,
        @NotNull PdmlReader pdmlReader ) throws IOException;

    default void parseValidateAndHandleObject (
        @NotNull PdmlParser pdmlParser,
        @Nullable TaggedNode parentNode,
        boolean consumeNodeEnd ) throws IOException, PdmlException {

        @Nullable ObjectTokenPair<T> objectTokenPair = parseObject ( pdmlParser );

        PdmlReader pdmlReader = pdmlParser.getPdmlReader();
        if ( ! pdmlReader.isAtNodeEnd() ) {
            throw new InvalidPdmlDataException (
                "End of node (" + CorePdmlConstants.NODE_END_CHAR + ") expected.",
                "NODE_END_REQUIRED",
                pdmlReader.currentCharToken () );
        }
        if ( consumeNodeEnd ) {
            // assert pdmlReader.skipChar ( CorePdmlConstants.NODE_END_CHAR );
            assert pdmlReader.readNodeEnd();
        }

        validateObject ( objectTokenPair );
        handleObject ( objectTokenPair, parentNode, pdmlParser.getPdmlReader() );
    }
}
