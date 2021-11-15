package dev.pp.texttable.data.impls;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FormFields<VALUE> {

    private List<FormField<VALUE>> list;


    public FormFields() {
        this.list = new ArrayList<>();
    }

    public FormFields<VALUE> add ( @NotNull FormField<VALUE> formField ) {
        list.add ( formField );
        return this;
    }

    public FormFields<VALUE> add ( @Nullable String label, VALUE value ) {
        return add ( new FormField<> ( label, value ) );
    }

    public List<FormField<VALUE>> getList() { return list; }
}
