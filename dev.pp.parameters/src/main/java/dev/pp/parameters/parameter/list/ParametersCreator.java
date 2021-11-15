package dev.pp.parameters.parameter.list;

import dev.pp.parameters.formalParameter.FormalParameter;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.Parameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.token.TextToken;

import java.util.Set;

public class ParametersCreator {

    /* TODO
    public static <T> Parameters<T> create (
        Map<String, String> stringMap,
        FormalParameters<T> formalParameters ) {
        // @Nullable errorHandler

        return null;
    }

    public static <T> Parameters<T> createFromCommandLineParameters (
        List<String> commandLineParameters,
        FormalParameters<T> formalParameters ) {

        return null;
    }
    */

    public static @Nullable <T> Parameters<T> createFromTextParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken textParametersStartToken,
        @Nullable FormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        checkConsistency ( textParameters, textParametersStartToken, formalParameters, errorHandler );

        if ( formalParameters == null ) return null;

        @Nullable Set<String> remainingNames = textParameters != null ? textParameters.getNames() : null;

        Parameters<T> parameters = new Parameters<>();

        for ( FormalParameter<T> formalParameter : formalParameters.getListSortedByOrder() ) {
            Parameter<T> parameter = getParameterForFormalParameter (
                formalParameter, textParameters, remainingNames, errorHandler, textParametersStartToken );
            if ( parameter != null ) parameters.add ( parameter );
        }

        checkInvalidParameters ( textParameters, remainingNames, formalParameters, errorHandler );

        return parameters;
    }

    public static boolean checkConsistency (
        @Nullable TextTokenParameters textParameters,
        @Nullable TextToken textParametersStartToken,
        @Nullable FormalParameters<?> formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        if ( formalParameters == null && textParameters != null ) {
            errorHandler.handleError (
                "NO_PARAMETERS_ALLOWED",
                "There are no parameters allowed in this context.",
                textParameters.getStartToken() );
            return false;
        } else if ( formalParameters != null && textParameters == null ) {
            if ( formalParameters.hasRequiredParameters() ) {
                errorHandler.handleError (
                    "PARAMETERS_REQUIRED",
                    "The following parameters are required: " +
                        formalParameters.sortedRequiredParameterNamesAsString() + ".",
                    textParametersStartToken );
                return false;
            }
        }
        return true;
    }

    public static <T> boolean checkInvalidParameters (
        @Nullable TextTokenParameters textParameters,
        @Nullable Set<String> remainingNames,
        @Nullable FormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) { // throws ObjectParserException {

        if ( remainingNames == null || remainingNames.isEmpty() ) return true;

        assert textParameters != null;
        for ( String name : remainingNames ) {
            TextTokenParameter parameter = textParameters.get ( name );
            assert formalParameters != null;
            errorHandler.handleError (
                "INVALID_PARAMETER",
                "Parameter '" + name + " 'does not exist. The following parameters are valid: " +
                    formalParameters.sortedParameterNamesAsString(),
                parameter.getNameToken() );
        }

        return false;
    }

    private static <T> Parameter<T> getParameterForFormalParameter (
        @NotNull FormalParameter<T> formalParameter,
        @Nullable TextTokenParameters textParameters,
        @Nullable Set<String> remainingNames,
        @NotNull TextErrorHandler errorHandler,
        @Nullable TextToken textParametersStartToken ) {

        String parameterName = formalParameter.getName();

        TextTokenParameter textParameter = getTextParameterForFormalParameter (
            textParameters, formalParameter, remainingNames );

        if ( textParameter != null ) {
            try {
                T value = formalParameter.stringToValidatedObject (
                    textParameter.getValue(), textParameter.getValueOrElseNameToken () );
                return new Parameter<> ( parameterName, value );
            } catch ( TextReaderException e ) {
                errorHandler.handleError (
                    e.getId(), e.getMessage(), textParameter.getValueToken() );
                return null;
            }

        } else {
            if ( formalParameter.getDefaultValueSupplier() != null ) {
                return new Parameter<> ( parameterName, formalParameter.getDefaultValue() );
            } else {
                errorHandler.handleError (
                    "MISSING_PARAMETER",
                    "Parameter '" + parameterName + "' is required.",
                    textParametersStartToken );
                return null;
            }
        }
    }

    private static TextTokenParameter getTextParameterForFormalParameter (
        @Nullable TextTokenParameters textParameters,
        @NotNull FormalParameter<?> formalParameter,
        @Nullable Set<String> remainingNames ) {

        if ( textParameters == null ) return null;

        for ( String name : formalParameter.getAllNames() ) {
            TextTokenParameter parameter = textParameters.getIfContained ( name );
            if ( parameter != null ) {
                assert remainingNames != null;
                remainingNames.remove ( name );
                return parameter;
            }
        }

        return null;
    }
}
