package dev.pp.pdml.ext.types.instances;

import dev.pp.pdml.ext.types.AbstractPdmlType;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.token.TextToken;

public class TextType extends AbstractPdmlType<String> {

    private static final @NotNull AbstractPdmlType.ObjectParser<String>
        OBJECT_PARSER = pdmlParser -> {

        // TODO? allow embedded comments
        PdmlReader reader = pdmlParser.getPdmlReader();
        TextToken textToken = reader.currentToken();
        // @Nullable String string = reader.readText();
        @Nullable String string = pdmlParser.parseTextAndIgnoreComments();
        return new ObjectTokenPair<> ( string, textToken );
    };

    private static final @Nullable AbstractPdmlType.ObjectValidator<String>
        DEFAULT_OBJECT_VALIDATOR = null;

    private static final @Nullable AbstractPdmlType.ObjectHandler<String>
        OBJECT_HANDLER = ( objectTokenPair, parentNode, pdmlReader ) -> {
            String text = objectTokenPair.object();
            handleTextObject ( text, text, parentNode, pdmlReader, true );
        };

    public static final @NotNull TextType NON_NULL_INSTANCE = new TextType (
        "text", false, DEFAULT_OBJECT_VALIDATOR );

    public static final @NotNull TextType NULLABLE_INSTANCE = new TextType (
        "text_or_null", true, DEFAULT_OBJECT_VALIDATOR );


    public TextType (
        @NotNull String name,
        boolean isNullAllowed,
        @Nullable AbstractPdmlType.ObjectValidator<String> objectValidator ) {

        super ( name, isNullAllowed, OBJECT_PARSER, objectValidator, OBJECT_HANDLER );
    }
}
