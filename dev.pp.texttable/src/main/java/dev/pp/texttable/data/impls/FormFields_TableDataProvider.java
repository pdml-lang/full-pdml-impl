package dev.pp.texttable.data.impls;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.texttable.data.TableDataColumn;
import dev.pp.texttable.data.TableDataProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class FormFields_TableDataProvider<VALUE> implements TableDataProvider<FormField<VALUE>, String> {


    private final @NotNull List<FormField<VALUE>> rows;
    private final @NotNull List<TableDataColumn<FormField<VALUE>, String>> columns;


    public FormFields_TableDataProvider (
        @NotNull List<FormField<VALUE>> rows,
        @NotNull List<TableDataColumn<FormField<VALUE>, String>> columns ) {

        this.rows = rows;
        this.columns = columns;
    }

    public FormFields_TableDataProvider (
        @NotNull FormFields<VALUE> formFields,
        @NotNull List<TableDataColumn<FormField<VALUE>, String>> columns ) {

        this ( formFields.getList(), columns );
    }

    public FormFields_TableDataProvider (
        @NotNull FormFields<VALUE> formFields ) {

        this ( formFields.getList(), createColumns() );
    }

    public FormFields_TableDataProvider (
        @NotNull List<FormField<VALUE>> rows,
        @Nullable String labelTitle,
        @Nullable String valueTitle ) {

        this.rows = rows;
        this.columns = createColumns ( labelTitle, valueTitle );
    }

    public FormFields_TableDataProvider (
        @NotNull List<FormField<VALUE>> rows ) {

        this.rows = rows;
        this.columns = createColumns ( "Label", "Value" );
    }


    public @NotNull Iterator<FormField<VALUE>> getRows () { return rows.iterator(); };

    public @NotNull List<TableDataColumn<FormField<VALUE>, String>> getColumns () { return columns; }

    public @NotNull Long getRowCount() { return (long) rows.size(); }


    private static @NotNull <VALUE> List<TableDataColumn<FormField<VALUE>, String>> createColumns (
        @Nullable String labelTitle,
        @Nullable String valueTitle ) {

        return List.of (
            createLabelColumn ( labelTitle ),
            createValueColumn ( valueTitle ) );
    }

    private static @NotNull <VALUE> List<TableDataColumn<FormField<VALUE>, String>> createColumns() {

        return createColumns ( "Label", "Value" );
    }

    private static @NotNull <VALUE> TableDataColumn<FormField<VALUE>, String> createLabelColumn ( @Nullable String header ) {

        return new TableDataColumn<>() {

            @Nullable @Override public String getHeader () { return header; }

            @NotNull @Override public Function<FormField<VALUE>, String> rowToCellConverter() {
                return FormField::getLabel;
            }
        };
    }

    private static @NotNull <VALUE> TableDataColumn<FormField<VALUE>, String> createValueColumn ( @Nullable String header ) {

        return new TableDataColumn<>() {

            @Nullable @Override public String getHeader () { return header; }

            @NotNull @Override public Function<FormField<VALUE>, String> rowToCellConverter() {
                return FormField::getValueAsString;
            }
        };
    }
}
