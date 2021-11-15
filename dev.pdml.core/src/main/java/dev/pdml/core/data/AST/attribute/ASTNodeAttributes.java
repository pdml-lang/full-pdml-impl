package dev.pdml.core.data.AST.attribute;

import dev.pp.parameters.formalParameter.list.FormalParameters;
import dev.pp.parameters.parameter.list.Parameters;
import dev.pp.parameters.parameter.list.ParametersCreator;
import dev.pp.parameters.textTokenParameter.TextTokenParameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.text.annotations.NotNull;
import dev.pp.text.annotations.Nullable;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.token.TextToken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ASTNodeAttributes {


    public static <T>  Parameters<T> attributesToParameters (
        @Nullable ASTNodeAttributes attributes,
        @Nullable TextToken textParametersStartToken,
        @Nullable FormalParameters<T> formalParameters,
        @NotNull TextErrorHandler errorHandler ) {

        return ParametersCreator.createFromTextParameters (
            attributes != null ? attributes.toTextParameters() : null,
            attributes != null ? attributes.getStartToken() : textParametersStartToken,
            formalParameters,
            errorHandler );
    }


    private final @NotNull Map<String, ASTNodeAttribute> map;
    private final @Nullable TextToken startToken;


    public ASTNodeAttributes ( @Nullable TextToken startToken ) {

        this.startToken = startToken;
        this.map = new LinkedHashMap<>();
    }

    public boolean isEmpty() {

        return map.isEmpty();
    }

    public boolean contains ( ASTNodeAttribute attribute ) {

        return contains ( attribute.getName().fullName() );
    }

    public boolean contains ( String qualifiedName ) {

        return map.containsKey ( qualifiedName );
    }

    public @Nullable ASTNodeAttribute getIfContained ( String qualifiedName ) {

        return map.get ( qualifiedName );
    }

    public @Nullable TextToken getStartToken () {

        return startToken;
    }

    public void add ( @NotNull ASTNodeAttribute attribute ) throws Exception {

        if ( contains ( attribute ) ) throw new Exception (
            "Attribute '" + attribute.getName().fullName() + "' exists already." );

        addOrReplace ( attribute );
    }

    public void addOrReplace ( @NotNull ASTNodeAttribute attribute ) {

        map.put ( attribute.getName().fullName(), attribute );
    }

    public @NotNull Collection<ASTNodeAttribute> getList() {

        return map.values();
    }

    public @NotNull TextTokenParameters toTextParameters() {

        TextTokenParameters parameters = new TextTokenParameters ( startToken );
        for ( ASTNodeAttribute attribute : getList() ) {
            TextTokenParameter parameter = attribute.toTextParameter();
            parameters.add ( parameter );
        }
        return parameters;
    }

    public @NotNull String toString() { return map.size() + "attribute(s)"; }
}
