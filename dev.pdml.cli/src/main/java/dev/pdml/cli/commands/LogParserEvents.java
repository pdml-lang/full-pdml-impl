package dev.pdml.cli.commands;

import dev.pdml.ext.utilities.LogUtils;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command (
    name = "log",
    description = "Log parser events." )

public class LogParserEvents implements Callable<Integer> {

    @Option ( names = { "-i", "--input" }, description = "pXML input file" )
    private @NotNull File inputFile;

    @Option ( names = { "-o", "--output" }, description = "Log output file (default is OS out)" )
    private @Nullable File outputFile;

    @Override
    public Integer call() {

        try {
            LogUtils.logParserEvents ( inputFile, outputFile );
            return 0;
        } catch ( Exception e ) {
            System.out.println ( e );
            return 1;
        }
    }
}
