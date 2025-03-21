package dev.pp.pdml.utils.networking.socket;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.core.basics.annotations.NotNull;

import java.io.IOException;

public interface PdmlClientInputHandler {

    /**
     *
     * @param inputRootNode
     * @param client
     * @return true if connection should be closed
     * @throws IOException
     */
    boolean handleInput (
        @NotNull TaggedNode inputRootNode,
        @NotNull PdmlClient client ) throws IOException;
}
