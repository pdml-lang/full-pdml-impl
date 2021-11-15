package dev.pp.text.documentation;

import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;

import java.util.Objects;

public class SimpleDocumentation {


    // TODO use Supplier<String> for more flexibility
    final @NotNull String title;
    final @Nullable String description;
    final @Nullable String examples;


    public SimpleDocumentation ( @NotNull String title, @Nullable String description, @Nullable String examples ) {
        Objects.requireNonNull ( title );

        this.title = title;
        this.description = description;
        this.examples = examples;
    }


    public @NotNull String getTitle () { return title; }

    public @Nullable String getDescription () { return description; }

    public @Nullable String getExamples () { return examples; }


    @Override public @NotNull String toString() { return title; }
}
