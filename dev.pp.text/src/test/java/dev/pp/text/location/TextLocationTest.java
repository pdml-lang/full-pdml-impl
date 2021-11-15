package dev.pp.text.location;

import dev.pp.text.resource.TextResource;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.string.StringConstants;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextLocationTest {

    @Test
    public void testToString() throws IOException {

        TextLocation l1 = new TextLocation ( new File ( "C:\\foo\\bar\\zar.txt" ), 12, 17, null );
        String expected = "zar.txt (C:\\foo\\bar\\zar.txt)" + StringConstants.OS_NEW_LINE + "Line 12, column 17";
        assertEquals ( expected, l1.toString() );
        // System.out.println ( l1.toString() );

        File tempFile = FileUtilities.createNonEmptyTempTextFile ( "line 1\n123456\nline3\n", true );
        TextLocation l2 = new TextLocation ( tempFile, 2, 5, l1 );
        // assertEquals ( "qwe", l2.toString() );
        System.out.println();
        System.out.println ( l2.toString() );

        URL url = new URL ( "http://www.example.org/foo#bar" );
        TextLocation l3 = new TextLocation ( url, 12345, 177, l2 );
        // assertEquals ( "qwe", l3.toString() );
        // System.out.println();
        // System.out.println ( l3.toString() );

        TextLocation l4 = new TextLocation ( (TextResource) null, 12, 17, null );
        assertEquals ( "Line 12, column 17", l4.toString() );
    }

    @Test
    public void testToLogString () throws IOException {

        TextLocation l1 = new TextLocation ( new File ( "C:\\foo\\bar\\zar.txt" ), 12, 17, null );
        assertEquals ( "\"C:\\foo\\bar\\zar.txt\",l 12,c 17", l1.toLogString () );
        // System.out.println ( l1.toLogString () );

        URL url = new URL ( "http://www.example.org/foo\"bar\"#zar" );
        TextLocation l3 = new TextLocation ( url, 12345, 177, l1 );
        assertEquals ( "\"http://www.example.org/foo\"\"bar\"\"#zar\",l 12345,c 177", l3.toLogString () );
        // System.out.println();
        // System.out.println ( l3.toLogString () );

        TextLocation l4 = new TextLocation ( (TextResource) null, 12, 17, null );
        assertEquals ( "\"\",l 12,c 17", l4.toLogString () );
        // System.out.println();
        // System.out.println ( l4.toLogString () );
    }
}
