package dev.pp.pdml.ext.types.instances;

import dev.pp.pdml.data.node.tagged.TaggedNode;
import dev.pp.pdml.ext.types.AbstractPdmlType;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.token.TextToken;

public class TrimmedTextOrStringLiteralType extends AbstractPdmlType<String> {


    private static final @NotNull AbstractPdmlType.ObjectParser<String>
        OBJECT_PARSER = pdmlParser -> {

        PdmlReader reader = pdmlParser.getPdmlReader();
        reader.skipWhitespaceAndComments ();
        @Nullable String string;
        TextToken textToken = reader.currentCharToken ();
        if ( reader.isAtChar ( '"' ) ||
            reader.isAtChar ( '~' ) ) {
            /*
                [code "i = 1"]
                [code """
                     i = 1
                     """ ]
                [code ~|i = 1|~]
             */
            // string = reader.readStringLiteralOrNull ( PdmlReader.ExtensionInitiatorKind.STRING_LITERAL );
            string = pdmlParser.parseStringLiteralOrNull();
            reader.skipWhitespaceAndComments ();
        } else {
            // [code i = 1]
            // string = reader.readText();
            string = pdmlParser.parseTextAndIgnoreComments();
            reader.skipWhitespaceAndComments ();
            if ( string != null ) {
                string = string.trim();
            }
        }
        return new ObjectTokenPair<> ( string, textToken );
    };

    private static final @Nullable AbstractPdmlType.ObjectValidator<String>
        DEFAULT_OBJECT_VALIDATOR = null;

    private static final @Nullable AbstractPdmlType.ObjectHandler<String>
        OBJECT_HANDLER = null;


    public static final @NotNull TrimmedTextOrStringLiteralType
        NON_NULL_INSTANCE = new TrimmedTextOrStringLiteralType (
            "text_or_string_literal", false, DEFAULT_OBJECT_VALIDATOR, true );

    public static final @NotNull TrimmedTextOrStringLiteralType
        NULLABLE_INSTANCE = new TrimmedTextOrStringLiteralType (
        "text_or_string_literal_or_null", true, DEFAULT_OBJECT_VALIDATOR, true );


    private final boolean escapeInsertedText;
    public boolean getEscapeInsertedText() { return escapeInsertedText; }


    public TrimmedTextOrStringLiteralType (
        @NotNull String name,
        boolean isNullAllowed,
        @Nullable ObjectValidator<String> objectValidator,
        boolean escapeInsertedText ) {

        super ( name, isNullAllowed, OBJECT_PARSER, objectValidator, OBJECT_HANDLER );

        this.escapeInsertedText = escapeInsertedText;
    }


    @Override
    public void handleObject (
        @NotNull ObjectTokenPair<String> objectTokenPair,
        @Nullable TaggedNode parentNode,
        @NotNull PdmlReader pdmlReader ) {

        String string = objectTokenPair.object();
        handleTextObject ( string, string, parentNode, pdmlReader, escapeInsertedText );
    }
}
