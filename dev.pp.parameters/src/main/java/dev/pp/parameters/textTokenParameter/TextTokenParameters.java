package dev.pp.parameters.textTokenParameter;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.token.TextToken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TextTokenParameters {


    private final @NotNull Map<String, TextTokenParameter> map;
    private final @Nullable TextToken startToken;


    public TextTokenParameters ( @Nullable TextToken startToken ) {

        this.startToken = startToken;
        this.map = new LinkedHashMap<>();
    }


    public @Nullable TextToken getStartToken() { return startToken; }


    public @NotNull Collection<TextTokenParameter> getList() { return map.values(); }

    public @NotNull Set<String> getNames() { return map.keySet(); }

    public boolean isEmpty() { return map.isEmpty(); }


    public boolean contains ( String name ) { return map.containsKey ( name ); }

    public boolean contains ( TextTokenParameter parameter ) { return contains ( parameter.getName() ); }


    public @NotNull TextTokenParameter get ( String name ) {

        if ( ! contains ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' doesn't exist." );

        return map.get ( name );
    }

    public @Nullable String getValue ( String name ) { return get ( name ).getValue(); }

    public @Nullable TextTokenParameter getIfContained ( String name ) { return map.get ( name ); }

    public @Nullable String getValueIfContained ( String name ) {

        TextTokenParameter parameter = getIfContained ( name );
        if ( parameter != null ) {
            return parameter.getValue ();
        } else {
            return null;
        }
    }


    public void add ( @NotNull TextTokenParameter parameter ) {

        if ( contains ( parameter ) ) throw new IllegalArgumentException (
            "Parameter '" + parameter.getName() + "' exists already." );

        addOrReplace ( parameter );
    }

    public void add ( @NotNull TextToken nameToken, @Nullable TextToken valueToken ) {

        add ( new TextTokenParameter ( nameToken, valueToken ) );
    }

    public void addOrReplace ( @NotNull TextTokenParameter parameter ) {

        map.put ( parameter.getName(), parameter );
    }


    public @NotNull String toString() { return map.size() + "parameter(s)"; }
}
