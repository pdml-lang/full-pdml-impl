package dev.pdml.core.reader.parser.eventHandler.impls;

import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.FileUtilities;
import dev.pp.text.utilities.string.StringTruncator;
import dev.pp.text.utilities.string.StringUtils;
import dev.pdml.core.Constants;
import dev.pdml.core.data.AST.attribute.ASTNodeAttribute;
import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.parser.eventHandler.NodeEndEvent;
import dev.pdml.core.reader.parser.eventHandler.NodeStartEvent;
import dev.pdml.core.reader.parser.eventHandler.ParserEventHandler;
import dev.pp.text.location.TextLocation;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Logger_ParserEventHandler implements ParserEventHandler<ASTNodeName, String> {

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

    public Logger_ParserEventHandler ( @NotNull File file ) throws IOException {

        this ( FileUtilities.getUTF8FileWriter ( file ) );
    }

    public Logger_ParserEventHandler() {

        this ( new PrintWriter( System.out ) );
    }


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
    ASTNodeName onRootNodeStart ( @NotNull NodeStartEvent event ) throws Exception {

        ASTNodeName name = event.getName();
        if ( logRootNodeStart ) {
            writeEvent ( "Root start", name.getLocation(), name.fullName() );
        }
        return name;
    }

    public void onRootNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNodeName rootNode ) throws Exception {

        if ( logRootNodeEnd ) {
            writeEvent ( "Root end", event.getLocation() );
        }
    }

    public @NotNull
    ASTNodeName onNodeStart ( @NotNull NodeStartEvent event, @NotNull ASTNodeName parentNode ) throws Exception {

        ASTNodeName name = event.getName();
        if ( logNodeStart ) {
            writeEvent ( "Node start", name.getLocation(), name.fullName() );
        }
        return name;
    }

    public void onNodeEnd ( @NotNull NodeEndEvent event, @NotNull ASTNodeName node ) throws Exception {

        if ( logNodeEnd ) {
            writeEvent ( "Node end", event.getLocation() );
        }
    }

    public void onAttributes ( @NotNull ASTNodeAttributes attributes, @NotNull ASTNodeName parentNode ) throws Exception {

        if ( logAttributes ) {
            writeEvent ( "Attributes:" );
    
            for ( ASTNodeAttribute attribute : attributes.getList() ) {
                writeEvent ( " ", attribute.getName().getLocation(), attribute.toString() );
            }
        }
    }

    public void onText ( @NotNull TextToken text, @NotNull ASTNodeName parentNode ) throws Exception {

        if ( logText ) {
            writeEvent ( "Text", text.getLocation(), text.getText() );
        }
    }

    public void onComment ( @NotNull TextToken comment, @NotNull ASTNodeName parentNode ) throws Exception {

        if ( logComment ) {
            writeEvent ( "Comment", comment.getLocation(), comment.getText() );
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

        write ( Constants.NEW_LINE );

        writer.flush();
    }

    private void write ( String string ) throws Exception {

        writer.write ( string );
    }
}
