package dev.pdml.utils.parameterizedtext;

import dev.pdml.utils.parser.StringParametersPdmlParser;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.parameters.parameters.Parameters;
import dev.pp.parameters.utilities.parameterizedtext.parser.AbstractParameterizedTextParser;
import dev.pp.parameters.utilities.parameterizedtext.reader.ParameterizedTextReader;
import dev.pp.text.inspection.TextErrorException;
import dev.pp.text.location.TextLocation;
import dev.pp.text.token.TextToken;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Pdml_ParameterizedTextParser extends AbstractParameterizedTextParser {


    public Pdml_ParameterizedTextParser (
        @NotNull ParameterizedTextReader textReader,
        @NotNull String textParameterName ) {

        super ( textReader, textParameterName );
    }

    public Pdml_ParameterizedTextParser () {
        super();
    }


    public @Nullable Parameters<String> parseParameters ( @NotNull TextToken parameters )
        throws IOException, TextErrorException {

        TextLocation location = parameters.getLocation();
        try ( Reader reader = new StringReader ( parameters.getText() ) ) {
            return StringParametersPdmlParser.parseReader (
                reader,
                location.getResource(),
                false,
                (int) location.getLineNumber(),
                (int) location.getColumnNumber(),
                false );
        }
    }
}
