package dev.pp.pdml.data.node.leaf;

import dev.pp.pdml.data.exception.InvalidPdmlDataException;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;
import dev.pp.core.basics.utilities.character.CharChecks;
import dev.pp.core.text.inspection.InvalidDataException;
import dev.pp.core.datatype.nonunion.scalar.impls.booleantype.BooleanDataType;
import dev.pp.core.datatype.nonunion.scalar.impls.enumtype.EnumDataType;
import dev.pp.core.datatype.nonunion.scalar.impls.integer.IntDataType;
import dev.pp.core.text.location.TextLocation;

public class TextLeaf extends UntaggedLeafNode {


    private interface FailableSupplier <T> {
        T get() throws InvalidDataException;
    }


    public TextLeaf (
        @NotNull String text,
        @Nullable TextLocation location ) {

        super ( text, location );
        assert ! text.isEmpty();
    }

    /*
    public TextLeaf ( @NotNull String text ) {
        this ( text, null );
    }
     */


    public boolean isTextLeaf() { return true; }

    public boolean isCommentLeaf() { return false; }


    // Whitespace Helpers

    public boolean isWhitespace() {

        for ( int i = 0; i < text.length(); i++){
            char c = text.charAt ( i );
            if ( ! CharChecks.isSpaceOrTabOrLineBreak ( c ) ) {
                return false;
            }
        }
        return true;
    }

    /*
    // TODO write test
    public boolean isWhitespaceBetweenBranchNodes() {

        if ( ! isWhitespace () ) {
            return false;
        }
        return previousSibling() instanceof BranchNode ||
            nextSibling() instanceof BranchNode;
    }
     */


    // Parse text to Java objects

    public int toInt() throws InvalidPdmlDataException {
        return parse ( () -> IntDataType.parse_ ( text, startLocation ) );
    }

    public boolean toBoolean (
        boolean caseInsensitive,
        boolean allowYesNoStrings ) throws InvalidPdmlDataException {

        return parse ( () -> BooleanDataType.parse ( text, caseInsensitive, allowYesNoStrings, startLocation ) );
    }

    public boolean toBoolean() throws InvalidPdmlDataException {
        return toBoolean ( true, true );
    }

    public <E extends Enum<E>> @NotNull E toEnum (
        @NotNull Class<E> clazz,
        boolean convertTextToUppercase ) throws InvalidPdmlDataException {

        return parse ( () -> EnumDataType.parse ( text, clazz, convertTextToUppercase, startLocation ) );
    }

    /*
    public @Nullable List<String> toStringListOrNull() throws InvalidPdmlDataException {
        return null;
    }
     */

    private <T> @NotNull T parse ( FailableSupplier<T> supplier ) throws InvalidPdmlDataException {

        try {
            return supplier.get();
        } catch ( InvalidDataException e ) {
            throw new InvalidPdmlDataException ( e );
        }
    }

    /* TODO

    byte short, long, float, double, BigInteger, BigDecimal
    List<String>, Set<String>, Map<String,String>
    bytes


    ? public <T> @NotNull T toBean ( Class<T> beanClass ) { // throws PdmlDataException
        // TODO
        return null;
    }

    ? public <T> @NotNull T toObject ( Function<String,T> parser ) { // throws PdmlDataException
        // TODO
        return null;
    }

    ? public <E> @NotNull List<E> toList ( Function<String,E> elementParser ) { // throws PdmlDataException
        // TODO
        return null;
    }

    ? public <E> @NotNull Set<E> toSet ( Function<String,E> elementParser ) { // throws PdmlDataException
        // TODO
        return null;
    }

    ? public <V> @NotNull Map<String,V> toMap ( Function<String,V> valueParser ) { // throws PdmlDataException
        // TODO
        return null;
    }
     */
}
