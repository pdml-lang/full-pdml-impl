package dev.pdml.utils.parser;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.reader.CharReader;
import dev.pp.text.reader.CharReaderImpl;
import dev.pp.text.reader.stack.CharReaderWithInserts;
import dev.pp.text.reader.stack.CharReaderWithInsertsImpl;
import dev.pp.text.resource.TextResource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class PdmlParserUtils {

    /*
    public static @NotNull CharReaderWithInserts createVirtualRootReader (
        @NotNull String virtualRootName,
        @NotNull CharReader rootContentReader ) throws IOException {

//        CharReader rootNodeStartReader = CharReaderImpl.createForString ( "[" + virtualRootName + " " );
        CharReader rootNodeStartReader = new CharReaderImpl (
            new StringReader ( "[" + virtualRootName + " " ), null, null, null );
//        CharReader rootNodeEndReader = CharReaderImpl.createForString ( "]" );
        CharReader rootNodeEndReader = new CharReaderImpl (
            new StringReader ( "]" ), null, null, null );

        CharReaderWithInserts result = new CharReaderWithInsertsImpl ( rootNodeEndReader );
        result.insert ( rootContentReader );
        result.insert ( rootNodeStartReader );

        return result;
    }

 */

    public static @NotNull CharReaderWithInserts createVirtualRootReader (
        @NotNull Reader rootContentReader,
        @Nullable TextResource rootContentTextResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset ) throws IOException {

        return createVirtualRootReader (
            "root", rootContentReader, rootContentTextResource, lineOffset, columnOffset );
    }

    public static @NotNull CharReaderWithInserts createVirtualRootReader (
        @NotNull String virtualRootName,
        @NotNull Reader rootContentReader,
        @Nullable TextResource rootContentTextResource,
        @Nullable Integer lineOffset,
        @Nullable Integer columnOffset ) throws IOException {

        // TODO close StringReaders
        CharReader rootNodeStartReader = CharReaderImpl.createAndAdvance (
            new StringReader ( "[" + virtualRootName + " " ), null, null, null );
        CharReader rootNodeEndReader = CharReaderImpl.createAndAdvance (
            new StringReader ( "]" ), null, null, null );
        CharReader rootNodeContentReader = CharReaderImpl.createAndAdvance (
            rootContentReader, rootContentTextResource, lineOffset, columnOffset );

        CharReaderWithInserts result = new CharReaderWithInsertsImpl ( rootNodeEndReader );
        result.insert ( rootNodeContentReader );
        result.insert ( rootNodeStartReader );

        return result;
    }
}
