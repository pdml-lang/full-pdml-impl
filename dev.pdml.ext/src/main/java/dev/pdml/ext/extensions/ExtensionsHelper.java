package dev.pdml.ext.extensions;

import dev.pdml.core.data.AST.attribute.ASTNodeAttributes;
import dev.pdml.core.data.AST.name.ASTNodeName;
import dev.pdml.core.reader.parser.ParserHelper;
import dev.pdml.core.reader.reader.PXMLReader;
import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.reader.exception.TextReaderException;
import dev.pp.text.resource.File_TextResource;
import dev.pp.text.resource.TextResource;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.DirectoryUtilities;

import java.io.File;

public class ExtensionsHelper {

    public static <T> Parameters<T> parseAttributesNodeAndCreateParameters (
        @NotNull PXMLReader reader,
        @NotNull ASTNodeName nodeName,
        @Nullable String defaultAttributeName,
        @Nullable FormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) throws TextReaderException {

        reader.skipWhitespaceAndComments();

        Parameters<T> parameters = parseAttributesWithOptionalParenthesisAndCreateParameters (
            reader, defaultAttributeName, formalParameters, errorHandler );

        reader.skipWhitespaceAndComments();

        ParserHelper.requireNodeEnd ( reader, nodeName, errorHandler );

        return parameters;
    }

    public static <T> Parameters<T> parseAttributesWithOptionalParenthesisAndCreateParameters (
        @NotNull PXMLReader reader,
        @Nullable String defaultAttributeName,
        @Nullable FormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) throws TextReaderException {

        TextToken startToken = new TextToken ( String.valueOf ( reader.currentChar() ), reader.currentLocation() );

        @Nullable ASTNodeAttributes attributes = ParserHelper.parseAttributesWithOptionalParenthesis (
            reader, defaultAttributeName, errorHandler );

        return ASTNodeAttributes.attributesToParameters ( attributes, startToken, formalParameters, errorHandler );
    }

    public static @NotNull File getRootDirectoryFromResource ( @Nullable TextResource resource, @NotNull File defaultValue ) {

        if ( resource instanceof File_TextResource ) {
            File file = ((File_TextResource) resource ).getResource();
            return file.getParentFile();
        } else {
            return defaultValue;
        }
    }

    public static File getRootDirectoryFromResource ( @Nullable TextResource resource ) {

        return getRootDirectoryFromResource ( resource, DirectoryUtilities.currentWorkingDirectory() );
    }

/*
    public static @NotNull File parseAttributeValueAsFile (
        @NotNull ExtensionsContext context, @NotNull File rootDirectoryForRelativeFile ) throws StringReaderException {

        PXMLReader reader = context.getPXMLReader();

        String filePath = reader.readAttributeValue();
        if ( filePath == null ) {
            throw ParserHelper.cancelingErrorAtCurrentLocation(
                "INVALID_FILE_PATH",
                "Expecting an absolute or relative file path, defined as an attribute value. The value cannot start with '" + reader.currentChar () + "'.",
                reader );
        }

        return filePathToFile ( filePath, rootDirectoryForRelativeFile );
    }

    public static @NotNull TextReader parseFileAttributeValueToTextReader (
        @NotNull ExtensionsContext context, @NotNull File rootDirectoryForRelativeFile ) throws PXMLReaderException {

        PXMLReader reader = context.getPXMLReader();
        TextLocation errorLocation = reader.currentLocation();

        String FilePath = ParserHelper.parseAttributeValue ( reader );
        if ( FilePath == null ) {
            throw ParserHelper.cancelingErrorAtCurrentLocation(
                "INVALID_FILE_PATH",
                "Expecting ab absolute or relative file path, defined like an attribute value. The value cannot start with '" + reader.currentChar() + "'.",
                reader );
        }

        return filePathToTextReader ( FilePath, context, rootDirectoryForRelativeFile, errorLocation );
    }

    public static @NotNull TextReader parseFileOrURLToTextReader (
        @NotNull ExtensionsContext context ) throws PXMLReaderException {

        PXMLReader reader = context.getPXMLReader();

        reader.skipWhiteSpace();

        boolean isFile;
        if ( reader.acceptString ( StandardExtensionsConstants.FILE ) ) {
            isFile = true;
        } else if ( reader.acceptString ( StandardExtensionsConstants.URL ) ) {
            isFile = false;
        } else {
            throw new MalformedPXMLDocumentException (
                "FILE_OR_URL_EXPECTED",
                "'" + StandardExtensionsConstants.FILE + "' or '" + StandardExtensionsConstants.URL + "' expected.",
                    reader.currentLocation() );
        }

        // use readAttributeValue?
        String pathOrURL = context.getPXMLReader().readText();
        if ( pathOrURL != null ) pathOrURL = pathOrURL.trim();
        if ( pathOrURL == null || pathOrURL.isEmpty() ) {
            if ( isFile ) {
                throw new MalformedPXMLDocumentException(
                    "FILE_PATH_REQUIRED", "Valid file path required.", reader.currentLocation() );
            } else {
                throw new MalformedPXMLDocumentException(
                    "URL_REQUIRED", "Valid URL required.", reader.currentLocation() );
            }
        }

        if ( isFile ) {
            File file = filePathStringToFile( pathOrURL, context );
            try {
                return new DefaultTextReader ( file );
            } catch ( IOException e ) {
                throw PXMLReaderErrorHandlerHelper.wrapIOException ( e, reader.currentLocation() );
            }

        } else {
            try {
                return new DefaultTextReader ( new URL ( pathOrURL ) );
            } catch ( MalformedURLException e ) {
                throw new MalformedPXMLDocumentException (
                    "INVALID_URL", "'" + pathOrURL + "' is an invalid URL. Reason: " + e.getMessage(), reader.currentLocation() );
            } catch ( IOException e ) {
                throw PXMLReaderErrorHandlerHelper.wrapIOException ( e, reader.currentLocation() );
            }
        }
    }

    public static @NotNull TextReader filePathToTextReader (
        @NotNull String filePath,
        @NotNull ExtensionsContext context,
        @NotNull File rootDirectoryForRelativeFile,
        TextLocation errorLocation ) throws PXMLResourceException {

        File file = filePathToFile ( filePath, rootDirectoryForRelativeFile );
        try {
            return new DefaultTextReader ( file );
        } catch ( IOException e ) {
            throw PXMLReaderErrorHandlerHelper.wrapIOException ( e, errorLocation );
        }
    }

    public static Reader fileToUTF8Reader ( File file ) throws Exception {
        return new FileReader ( file, StandardCharsets.UTF_8 );
    }

    public static Reader URLToUTF8Reader ( String URLString ) throws Exception {

        URL url = new URL ( URLString );
        InputStream in = url.openStream();
        return new InputStreamReader ( in, StandardCharsets.UTF_8 );
    }

    public static String readFileContent ( String filePath, ExtensionsContext context ) throws IOException {

        File file = stringToFile ( filePath, context );
        return readFileUTF8Content ( file );
    }

    public static String readFileUTF8Content ( File file ) throws IOException {

        return Files.readString ( file.toPath() );
    }

    public static String readURLUTF8Content ( String URLString ) throws Exception {

        URL url = new URL ( URLString );
        InputStream in = url.openStream();
        return new String ( in.readAllBytes(), StandardCharsets.UTF_8 );
    }
*/
}
