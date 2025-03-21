package dev.pp.pdml.utils.treewalker.handler.impl;

import dev.pp.pdml.data.node.NodeTag;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.string.StringConstants;
import dev.pp.core.basics.utilities.string.StringTruncator;
import dev.pp.core.basics.utilities.string.StringUtils;
import dev.pp.core.text.location.TextLocation;
import dev.pp.pdml.utils.treewalker.handler.*;

import java.io.IOException;
import java.io.Writer;

public class LoggerParserEventHandler implements PdmlTreeWalkerEventHandler<NodeTag, String> {

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


    public LoggerParserEventHandler ( @NotNull Writer writer ) {
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


    @Override
    public void onStart() throws IOException {

        if ( logStart ) {
            writeEvent ( "Doc start" );
        }
    }

    @Override
    public void onEnd() throws IOException {

        if ( logEnd ) {
            writeEvent ( "Doc end" );
        }
        writer.flush();
    }

    @Override
    public @NotNull String getResult() throws IOException {

        if ( logGetResult ) {
            writeEvent ( "Get result" );
        }
        return "nothing";
    }

    @Override
    public @NotNull NodeTag onRootNodeStart ( @NotNull TaggedNodeStartEvent event ) throws IOException {

        NodeTag name = event.tag ();
        if ( logRootNodeStart ) {
            writeEvent ( "Root start", name.tagLocation (), name.qualifiedTag () );
        }
        return name;
    }

    @Override
    public void onRootNodeEnd ( @NotNull TaggedNodeEndEvent event, @NotNull NodeTag rootNode ) throws IOException {

        if ( logRootNodeEnd ) {
            writeEvent ( "Root end", event.location() );
        }
    }

    @Override
    public @NotNull NodeTag onTaggedNodeStart ( @NotNull TaggedNodeStartEvent event, @NotNull NodeTag parentNode ) throws IOException {

        NodeTag name = event.tag ();
        if ( logNodeStart ) {
            writeEvent ( "Node start", name.tagLocation (), name.qualifiedTag () );
        }
        return name;
    }

    @Override
    public void onTaggedNodeEnd ( @NotNull TaggedNodeEndEvent event, @NotNull NodeTag node ) throws IOException {

        if ( logNodeEnd ) {
            writeEvent ( "Node end", event.location() );
        }
    }

    @Override
    public void onText ( @NotNull TextEvent event, @NotNull NodeTag parentNode ) throws IOException {

        if ( logText ) {
            writeEvent ( "Text", event.location(), event.text() );
        }
    }

    @Override
    public void onComment ( @NotNull CommentEvent event, @NotNull NodeTag parentNode ) throws IOException {

        if ( logComment ) {
            writeEvent ( "Comment", event.location(), event.comment() );
        }
    }


    // private helpers

    private void writeEvent ( @NotNull String eventName ) throws IOException {
        writeEvent ( eventName, null, null );
    }

    private void writeEvent ( @NotNull String eventName, @Nullable TextLocation location ) throws IOException {

        writeEvent ( eventName, location, null );
    }

    private void writeEvent (
        @NotNull String eventName, @Nullable TextLocation location, @Nullable String text ) throws IOException {

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
                write ( " <" + StringUtils.replaceLineBreaksWithUnixEscape ( text ) + ">" );
            } else {
                write ( separator + "<" + text + ">" );
            }
            // write ( " (" + text.length () + ")" );
            write ( " / " + text.length () );
        }

        write ( StringConstants.OS_LINE_BREAK );

        writer.flush();
    }

    private void write ( String string ) throws IOException {
        writer.write ( string );
    }
}
