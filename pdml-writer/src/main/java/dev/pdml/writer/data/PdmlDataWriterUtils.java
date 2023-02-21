package dev.pdml.writer.data;

import dev.pdml.data.node.branch.MutableRootOrBranchNode;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.utilities.file.TextFileIO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;

public class PdmlDataWriterUtils {

    public static @NotNull String writeToString (
        @NotNull MutableRootOrBranchNode node,
        @NotNull PdmlDataWriterConfig config ) {

        try ( Writer stringWriter = new StringWriter() ) {
            write ( stringWriter, node, config );
            return stringWriter.toString();
        } catch ( IOException e ) {
            throw new RuntimeException ( "String writer should never throw " + e );
        }
    }

    public static void writeToFile (
        @NotNull Path filePath,
        @NotNull MutableRootOrBranchNode node,
        @NotNull PdmlDataWriterConfig config ) throws IOException {

        try ( FileWriter fileWriter = TextFileIO.getUTF8FileWriter ( filePath ) ) {
            write ( fileWriter, node, config );
        }
    }

    public static void write (
        @NotNull Writer writer,
        @NotNull MutableRootOrBranchNode node,
        @NotNull PdmlDataWriterConfig config ) throws IOException {

        PdmlDataWriter pdmlWriter = new PdmlDataWriter ( writer, config );
        pdmlWriter.writeRootOrBranchNode ( node );
    }
}
