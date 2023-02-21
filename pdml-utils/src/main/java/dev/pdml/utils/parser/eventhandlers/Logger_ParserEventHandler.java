package dev.pdml.utils.parser.eventhandlers;

import dev.pdml.data.node.NodeName;
import dev.pdml.parser.eventhandler.*;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.basics.utilities.string.StringConstants;
import dev.pp.basics.utilities.string.StringTruncator;
import dev.pp.basics.utilities.string.StringUtils;
import dev.pp.text.location.TextLocation;

import java.io.Writer;

public class Logger_ParserEventHandler implements PdmlParserEventHandler<NodeName, String> {

    private final @NotNull Writer writer;

    public boolean logStart = true;
    public boolean logEnd = true;
    public boolean logRootNodeStart = true;
    public boolean logRootNodeEnd = true;
    public boolean logNodeStart = true;
    public boolean logNodeEnd = true;
    public boolean logAttributes = true;
    public boolean logText = true;
    public boolean logComment = true;
    public boolean logGetResult = true;

    public @Nullable String separator = null;


    public Logger_ParserEventHandler ( @NotNull Writer writer ) {
        this.writer = writer;
    }

    /*
    public Logger_ParserEventHandler ( @NotNull Path file ) throws IOException {

        this ( TextFileUtils.getUTF8FileWriter ( file ) );
    }

    public Logger_ParserEventHandler() {

        this ( new PrintWriter( System.out ) );
    }
    */


    public void logAll() {

        logStart = true;
        logEnd = true;
        logRootNodeStart = true;
        logRootNodeEnd = true;
        logNodeStart = true;
        logNodeEnd = true;
        logAttributes = true;
        logText = true;
        logComment = true;
        logGetResult = true;
    }

    public void logNone() {

        logStart = false;
        logEnd = false;
        logRootNodeStart = false;
        logRootNodeEnd = false;
        logNodeStart = false;
        logNodeEnd = false;
        logAttributes = false;
        logText = false;
        logComment = false;
        logGetResult = false;
    }


    public void onStart() throws Exception {

        if ( logStart ) {
            writeEvent ( "Doc start" );
        }
    }

    public void onEnd () throws Exception {

        if ( logEnd ) {
            writeEvent ( "Doc end" );
        }
        writer.flush();
    }

    public @NotNull String getResult() throws Exception {

        if ( logGetResult ) {
            writeEvent ( "Get result" );
        }
        return "nothing";
    }

    public @NotNull
    NodeName onRootNodeStart ( @NotNull NodeStartEvent event ) throws Exception {

        NodeName name = event.name();
        if ( logRootNodeStart ) {
            writeEvent ( "Root start", name.localNameLocation(), name.qualifiedName () );
        }
        return name;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull NodeName rootNode ) throws Exception {

        if ( logRootNodeEnd ) {
            writeEvent ( "Root end", event.location() );
        }
    }

    public @NotNull
    NodeName onNodeStart ( @NotNull NodeStartEvent event, @NotNull NodeName parentNode ) throws Exception {

        NodeName name = event.name();
        if ( logNodeStart ) {
            writeEvent ( "Node start", name.localNameLocation(), name.qualifiedName () );
        }
        return name;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull NodeName node ) throws Exception {

        if ( logNodeEnd ) {
            writeEvent ( "Node end", event.location() );
        }
    }

    public void onText ( @NotNull NodeTextEvent event, @NotNull NodeName parentNode ) throws Exception {

        if ( logText ) {
            writeEvent ( "Text", event.location(), event.text() );
        }
    }

    public void onComment ( @NotNull NodeCommentEvent event, @NotNull NodeName parentNode ) throws Exception {

        if ( logComment ) {
            writeEvent ( "Comment", event.location(), event.comment() );
        }
    }


    // private helpers

    private void writeEvent ( @NotNull String eventName ) throws Exception {

        writeEvent ( eventName, null, null );
    }

    private void writeEvent ( @NotNull String eventName, @Nullable TextLocation location ) throws Exception {

        writeEvent ( eventName, location, null );
    }

    private void writeEvent (
        @NotNull String eventName, @Nullable TextLocation location, @Nullable String text ) throws Exception {

        if ( separator == null ) {
            write ( StringTruncator.rightPadOrTruncate ( eventName, 11 ) );
        } else {
            write ( eventName );
        }

        String lc;
        if ( location != null ) {
            lc = "[" + location.getLineNumber () + ", " + location.getColumnNumber () + "]";
        } else {
            lc = " ";
        }
        if ( separator == null ) {
            write ( " " );
            write ( StringTruncator.rightPadOrTruncate ( lc, 12 ) );
        } else {
            write ( separator );
            write ( lc );
        }

        if ( text != null ) {
            if ( separator == null ) {
                write ( " <" + StringUtils.replaceNewLinesWithUnixEscape ( text ) + ">" );
            } else {
                write ( separator + "<" + text + ">" );
            }
            // write ( " (" + text.length () + ")" );
            write ( " / " + text.length () );
        }

        write ( StringConstants.OS_NEW_LINE );

        writer.flush();
    }

    private void write ( String string ) throws Exception {

        writer.write ( string );
    }
}
