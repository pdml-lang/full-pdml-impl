package dev.pp.pdml.ext.types.instances;

import dev.pp.pdml.ext.types.AbstractPdmlType;
import dev.pp.pdml.reader.PdmlReader;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

public class StringLiteralType extends AbstractPdmlType<String> {

    private static final @NotNull AbstractPdmlType.ObjectParser<String>
        OBJECT_PARSER = pdmlParser -> {

        PdmlReader pdmlReader = pdmlParser.getPdmlReader();
        pdmlReader.skipSpacesAndTabsAndLineBreaksAndComments();
        TextLocation startLocation = pdmlReader.currentLocation();
        @Nullable String string = pdmlParser.parseStringLiteralOrNull();
        pdmlReader.skipSpacesAndTabsAndLineBreaksAndComments();
        return new ObjectTokenPair<> (
            string, TextToken.createForNullable ( string, startLocation ) );
    };

    private static final @Nullable AbstractPdmlType.ObjectValidator<String>
        DEFAULT_OBJECT_VALIDATOR = null;

    private static final @Nullable AbstractPdmlType.ObjectHandler<String>
        OBJECT_HANDLER = ( objectTokenPair, parentNode, pdmlReader ) -> {
            String string = objectTokenPair.object();
            handleTextObject ( string, string, parentNode, pdmlReader, true );
        };


    public static final @NotNull StringLiteralType NON_NULL_INSTANCE = new StringLiteralType (
        "string", false, DEFAULT_OBJECT_VALIDATOR );

    public static final @NotNull StringLiteralType NULLABLE_INSTANCE = new StringLiteralType (
        "string_or_null", true, DEFAULT_OBJECT_VALIDATOR );


    public StringLiteralType (
        @NotNull String name,
        boolean isNullAllowed,
        @Nullable AbstractPdmlType.ObjectValidator<String> objectValidator ) {

        super ( name, isNullAllowed, OBJECT_PARSER, objectValidator, OBJECT_HANDLER );
    }
}

