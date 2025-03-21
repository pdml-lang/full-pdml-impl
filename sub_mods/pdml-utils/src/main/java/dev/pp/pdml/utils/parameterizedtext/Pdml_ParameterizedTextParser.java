package dev.pp.pdml.utils.parameterizedtext;

import dev.pp.pdml.data.exception.PdmlException;
import dev.pp.pdml.parser.PdmlParserConfig;
import dev.pp.pdml.utils.parser.StringParametersUtil;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.parameters.parameters.Parameters;
import dev.pp.core.parameters.utilities.parameterizedtext.parser.AbstractParameterizedTextParser;
import dev.pp.core.parameters.utilities.parameterizedtext.reader.ParameterizedTextReader;
import dev.pp.core.text.inspection.InvalidTextException;
import dev.pp.core.text.location.TextLocation;
import dev.pp.core.text.token.TextToken;

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
        throws IOException, InvalidTextException {

        TextLocation location = parameters.getLocation();
        try ( Reader reader = new StringReader ( parameters.getText() ) ) {
            try {
                /*
                return StringParametersPdmlParser_OLD.parseReader (
                    reader,
                    location.getResource (),
                    false,
                    (int) location.getLineNumber (),
                    (int) location.getColumnNumber (),
                    false );
                 */
                return StringParametersUtil.parseReader (
                    reader,
                    location == null ? null : location.getResource(),
                    location == null ? null : (int) location.getLineNumber(),
                    location == null ? null : (int) location.getColumnNumber(),
                    false,
                    false,
                    PdmlParserConfig.defaultConfig() );
            } catch ( PdmlException e ) {
                throw e.toInvalidTextException();
            }
        }
    }
}
