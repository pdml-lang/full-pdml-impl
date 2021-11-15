package dev.pp.parameters.textTokenParameter;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.string.StringConstants;
import dev.pp.text.utilities.string.StringTruncator;

public class TextTokenParameter {


    private final @NotNull TextToken nameToken;
    private final @Nullable TextToken valueToken;


    public TextTokenParameter ( @NotNull TextToken nameToken, @Nullable TextToken valueToken ) {

        this.nameToken = nameToken;
        this.valueToken = valueToken;
    }


    public @NotNull TextToken getNameToken() { return nameToken; }

    public @Nullable TextToken getValueToken() { return valueToken; }


    public @NotNull String getName() { return nameToken.getText(); }

    public @Nullable String getValue() { return valueToken != null ? valueToken.getText() : null; }

    // public @Nullable TextLocation getNameLocation() { return nameToken.getLocation(); }

    // public @Nullable TextLocation getValueLocation() { return valueToken != null ? valueToken.getLocation() : null; }

    public @Nullable TextToken getValueOrElseNameToken () {

        TextToken valueToken = getValueToken();
        if ( valueToken != null ) return valueToken;
        return getNameToken();
    }


    @Override
    public String toString() {

        String value = getValue() == null ? StringConstants.NULL_AS_STRING : StringTruncator.truncateWithEllipses ( getValue() );
        return getName() + ": " + value;
    }
}
