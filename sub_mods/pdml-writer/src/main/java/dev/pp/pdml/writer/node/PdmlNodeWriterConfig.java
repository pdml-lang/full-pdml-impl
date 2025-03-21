package dev.pp.pdml.writer.node;

import dev.pp.pdml.core.writer.PdmlWriterConfig;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.text.writer.LineBreakKind;

public class PdmlNodeWriterConfig extends PdmlWriterConfig {


    public static boolean DEFAULT_USE_UNQUOTED_ATTRIBUTE_VALUES = false;
    public static @NotNull PdmlNodeWriterConfig DEFAULT_CONFIG = new PdmlNodeWriterConfig (
        DEFAULT_USE_UNQUOTED_ATTRIBUTE_VALUES, PdmlWriterConfig.DEFAULT_CONFIG );


    // TODO PdmlNodeSpecs nodeSpecs

    boolean writeUnquotedAttributeValuesIfPossible;
    public boolean isWriteUnquotedAttributeValuesIfPossible() {
        return writeUnquotedAttributeValuesIfPossible;
    }


    public PdmlNodeWriterConfig (
        int indentSize,
        boolean useTabIndent,
        @NotNull LineBreakKind lineBreakKind,
        boolean writeUnquotedAttributeValuesIfPossible ) {

        super ( indentSize, useTabIndent, lineBreakKind );
        this.writeUnquotedAttributeValuesIfPossible = writeUnquotedAttributeValuesIfPossible;
    }

    public PdmlNodeWriterConfig (
        boolean writeUnquotedAttributeValuesIfPossible,
        @NotNull PdmlWriterConfig writerConfig ) {

        this (
            writerConfig.getIndentSize(), writerConfig.useTabIndent(), writerConfig.getLineBreakKind(),
            writeUnquotedAttributeValuesIfPossible );
    }
}
