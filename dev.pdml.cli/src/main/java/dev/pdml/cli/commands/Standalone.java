package dev.pdml.cli.commands;

import dev.pdml.ext.utilities.StandaloneUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command (
    name = "standalone",
    description = "Create a pXML standalone file from a pXML input file." )

public class Standalone implements Callable<Integer> {

    @Option ( names = { "-i", "--input" }, description = "pXML input file" )
    private File inputFile;

    @Option ( names = { "-o", "--output" }, description = "Standalone pXML output file" )
    private File outputFile;

    @Override
    public Integer call() throws Exception {

        try {
            StandaloneUtils.createStandalone ( inputFile, outputFile );
            return 0;
        } catch ( Exception e ) {
            System.out.println ( e.toString () );
            return 1;
        }
    }
}
