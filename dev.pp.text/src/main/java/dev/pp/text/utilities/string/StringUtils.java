package dev.pp.text.utilities.string;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.util.regex.Pattern;

public class StringUtils {

    public static boolean stringMatchesRegex ( @NotNull String string, @NotNull Pattern pattern ) {

        return pattern.matcher ( string ).matches();
    }

    public static @NotNull String replaceQuoteWith2Quotes ( @NotNull String string ) {

        return string.replace ( "\"", "\"\"" );
    }

    public static @NotNull String replaceNewLinesWithUnixEscape ( @NotNull String string ) {

        return string
            .replace ( "\r", "" )
            .replace ( "\n", "\\n" );
    }

    /* not used
    public static @Nullable String removeLeadingNewLine ( @NotNull String string ) {

        if ( string.isEmpty() ) return null;

        char firstChar = string.charAt ( 0 );
        switch ( firstChar ) {
            case CharConstants.UNIX_NEW_LINE:
                return StringUtilities.emptyStringToNull ( string.substring ( 1 ) );
            case CharConstants.WINDOWS_NEW_LINE_START:
                return StringUtilities.emptyStringToNull ( string.substring ( 2 ) );
            default:
                return string;
        }
    }
    */

    public static @Nullable String emptyStringToNull ( @NotNull String string ) {

        return string.isEmpty() ? null : string;
    }
}
