package dev.pp.parameters.parameter.list;

import dev.pp.parameters.parameter.Parameter;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;

public class Parameters<T> {


    private final @NotNull Map<String, Parameter<T>> map;


    public Parameters ( boolean maintainOrder ) {

        if ( maintainOrder ) {
            this.map = new LinkedHashMap<>();
        } else {
            this.map = new HashMap<> ();
        }
    }

    public Parameters() { this ( true ); }


    // lists

    public Collection<Parameter<T>> getList() { return map.values(); }

    // TODO public Collection<Parameter<T>> getListSortedByName() { return map.values(); }

    // TODO public Collection<Parameter<T>> getListSortedByFormalParameterOrder() { return map.values(); }

    public Set<String> getNames() { return map.keySet(); }

    // TODO public Set<String> getSortedNames() { return map.keySet(); }


    // contains

    public boolean containsName ( @NotNull String name ) { return map.containsKey ( name ); }

    public boolean containsParameter ( @NotNull Parameter<T> parameter ) { return containsName ( parameter.getName() ); }


    // getters

    public @Nullable T getNullableValue ( @NotNull String name ) {

        checkExists ( name );
        return map.get ( name ).getValue();
    }

    public @NotNull T getNonNullValue ( @NotNull String name ) {

        T value = getNullableValue ( name );
        if ( value == null )
            throw new IllegalArgumentException ( "'" + name + "' is null, but supposed to be noon-null.");
        return value;
    }

    public @Nullable T getValueOrNull ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            return map.get ( name ).getValue ();
        } else {
            return null;
        }
    }

    public @NotNull T getNonNullValueOrDefault ( @NotNull String name, @NotNull T defaultValue ) {

        if ( containsName ( name ) ) {
            return getNonNullValue ( name );
        } else {
            return defaultValue;
        }
    }

    // Typed getters

    public @NotNull Path getNonNullPath ( @NotNull String name ) { return (Path) getNonNullValue ( name ); }

    public @Nullable Path getNullablePath ( @NotNull String name ) { return (Path) getNullableValue ( name ); }

    public @Nullable Path getPathOrNull ( @NotNull String name ) { return (Path) getValueOrNull ( name ); }

    // add

    public Parameters<T> add ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        checkNotExists ( name );
        map.put ( name, parameter );

        return this;
    }

    public Parameters<T>  add ( @NotNull String name, @Nullable T value ) {

        add ( new Parameter<T> ( name, value ) );

        return this;
    }

    public void addOrReplace ( @NotNull Parameter<T> parameter ) {

        map.put ( parameter.getName(), parameter );
    }

    public Parameters<T> addOrReplace ( @NotNull String name, @Nullable T value ) {

        map.put ( name, new Parameter<> ( name, value ) );

        return this;
    }


    // replace

    public Parameters<T>  replace ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        checkExists ( name );
        map.put ( name, parameter );

        return this;
    }

    public Parameters<T>  replace ( @NotNull String name, T value ) {

        checkExists ( name );
        map.put ( name, new Parameter<> ( name, value ) );

        return this;
    }

    public boolean replaceIfExists ( @NotNull Parameter<T> parameter ) {

        String name = parameter.getName();
        if ( containsName ( name ) ) {
            map.put ( name, parameter );
            return true;
        } else {
            return false;
        }
    }


    // remove

    public void remove ( @NotNull String name ) {

        checkExists ( name );
        map.remove ( name );
    }

    public boolean removeIfExists ( @NotNull String name ) {

        if ( containsName ( name ) ) {
            map.remove ( name );
            return true;
        } else {
            return false;
        }
    }


    private void checkExists ( @NotNull String name ) {

        if ( ! containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' does not exist." );
    }

    private void checkNotExists ( @NotNull String name ) {

        if ( containsName ( name ) ) throw new IllegalArgumentException (
            "Parameter '" + name + "' exists already." );
    }
}
