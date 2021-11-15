package dev.pdml.cli.commands;

import dev.pdml.ext.utilities.PXMLToXMLConverter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command (
    name = "pxmltoxml",
    description = "Convert a pXML file to an XML file." )

public class PXMLToXML implements Callable<Integer> {

    @Option ( names = { "-i", "--input" }, description = "pXML input file" )
    private File pXMLinputFile;

    @Option ( names = { "-o", "--output" }, description = "XML output file" )
    private File XMLoutputFile;

    @Override
    public Integer call() {

        try {
            PXMLToXMLConverter.pXMLFileToXMLFile ( pXMLinputFile, XMLoutputFile );
            return 0;
        } catch ( Exception e ) {
            System.out.println ( e.toString () );
            return 1;
        }
    }
}
