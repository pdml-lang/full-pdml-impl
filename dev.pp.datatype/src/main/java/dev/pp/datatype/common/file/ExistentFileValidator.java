package dev.pp.datatype.common.file;

import dev.pp.datatype.validator.DataValidator;
import dev.pp.datatype.validator.DataValidatorException;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;
import dev.pp.text.utilities.FileUtilities;

import java.io.File;
import java.util.function.Function;

public class ExistentFileValidator implements DataValidator<File> {


    public static String DEFAULT_ERROR_ID = "FILE_DOES_NOT_EXIST";

    public static Function<File, String> DEFAULT_ERROR_MESSAGE_SUPPLIER = file ->
        "File '" + FileUtilities.getAbsoluteOSPath ( file ) + "' does not exist.";;


    private final @NotNull String errorId;
    private final @NotNull Function<File, String> errorMessageSupplier;


    public ExistentFileValidator ( @NotNull String errorId, @NotNull Function<File, String> errorMessageSupplier ) {

        this.errorId = errorId;
        this.errorMessageSupplier = errorMessageSupplier;
    }

    public ExistentFileValidator() {
        this ( DEFAULT_ERROR_ID, DEFAULT_ERROR_MESSAGE_SUPPLIER );
    }


    public @NotNull String getErrorId () { return errorId; }

    public @NotNull Function<File, String> getErrorMessageSupplier () { return errorMessageSupplier; }


    public void validate ( @NotNull File file, @Nullable TextToken token ) throws DataValidatorException {

        if ( ! file.exists() || file.isDirectory() ) throw new DataValidatorException (
            errorId,
            errorMessageSupplier.apply ( file ),
            token, null );
    }
}
