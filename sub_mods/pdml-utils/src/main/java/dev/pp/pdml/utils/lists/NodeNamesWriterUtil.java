package dev.pp.pdml.utils.lists;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.parser.PdmlParserUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.resource.reader.TextResourceReader;
import dev.pp.core.text.resource.writer.TextResourceWriter;

import java.io.IOException;

public class NodeNamesWriterUtil {

    public static void writeNames (
        @NotNull TextResourceReader pdmlCodeReader,
        @NotNull PdmlParserConfig parserConfig,
        @NotNull TextResourceWriter writer,
        @Nullable String separator,
        boolean sort,
        boolean distinct ) throws IOException, PdmlException {

        @NotNull TaggedNode rootNode = PdmlParserUtil.parseReader (
            pdmlCodeReader, parserConfig );
        writeNames ( rootNode, writer, separator, sort, distinct );
    }

    public static void writeNames (
        @NotNull TaggedNode rootNode,
        @NotNull TextResourceWriter writer,
        @Nullable String separator,
        boolean sort,
        boolean distinct ) throws IOException {

        TextLeavesWriterUtil.writeTextsOrNames ( true, rootNode, writer, separator, sort, distinct );
    }
}
