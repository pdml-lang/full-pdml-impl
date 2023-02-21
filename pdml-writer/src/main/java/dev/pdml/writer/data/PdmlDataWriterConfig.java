package dev.pdml.writer.data;

public record PdmlDataWriterConfig (
    boolean useIndents,
    // TODO PdmlNodeSpecs nodeSpecs
    boolean writeUnquotedAttributeValuesIfPossible ) {

    public PdmlDataWriterConfig() {
        this ( false, false );
    }
}
