package dev.pp.pdml.parser;

import dev.pp.pdml.core.parser.CorePdmlParserConfig;
import dev.pp.pdml.data.nodespec.PdmlNodeSpecs;
import dev.pp.pdml.ext.types.PdmlTypes;
import dev.pp.core.basics.annotations.NotNull;
import dev.pp.core.basics.annotations.Nullable;

public class PdmlParserConfig extends CorePdmlParserConfig {


    public static boolean DEFAULT_IGNORE_TEXT_AFTER_ROOT_NODE_END = CorePdmlParserConfig.DEFAULT_IGNORE_TEXT_AFTER_ROOT_NODE_END;
    public static final @Nullable PdmlNodeSpecs DEFAULT_NODE_SPECS = null;
    public static final @Nullable PdmlTypes DEFAULT_TYPES = null;
    public static final boolean DEFAULT_ALLOW_ATTRIBUTES_WITHOUT_CARET = false;
    public static final boolean DEFAULT_IGNORE_COMMENTS = true;


    private final @Nullable PdmlNodeSpecs nodeSpecs;
    public @Nullable PdmlNodeSpecs getNodeSpecs() { return nodeSpecs; }

    private final @Nullable PdmlTypes types;
    public @Nullable PdmlTypes getTypes() { return types; }

    private final boolean ignoreComments;
    public boolean isIgnoreComments() { return ignoreComments; }

    private final boolean allowAttributesWithoutCaret;
    public boolean getAllowAttributesWithoutCaret() { return allowAttributesWithoutCaret; }


    public PdmlParserConfig (
        boolean ignoreTextAfterRootNodeEnd,
        @Nullable PdmlNodeSpecs nodeSpecs,
        @Nullable PdmlTypes types,
        boolean ignoreComments,
        boolean allowAttributesWithoutCaret ) {

        super ( ignoreTextAfterRootNodeEnd );

        this.nodeSpecs = nodeSpecs;
        this.types = types;
        this.ignoreComments = ignoreComments;
        this.allowAttributesWithoutCaret = allowAttributesWithoutCaret;
    }

    public static @NotNull PdmlParserConfig defaultConfig() {

        return new PdmlParserConfig (
            DEFAULT_IGNORE_TEXT_AFTER_ROOT_NODE_END,
            DEFAULT_NODE_SPECS,
            DEFAULT_TYPES,
            DEFAULT_IGNORE_COMMENTS,
            DEFAULT_ALLOW_ATTRIBUTES_WITHOUT_CARET );
    }
}


