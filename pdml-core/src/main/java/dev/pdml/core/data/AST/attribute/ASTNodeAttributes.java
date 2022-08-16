package dev.pdml.core.data.AST.attribute;

import dev.pp.parameters.formalParameter.FormalParameters;
import dev.pp.parameters.parameter.Parameters;
import dev.pp.parameters.parameter.ParametersCreator;
import dev.pp.parameters.textTokenParameter.TextTokenParameter;
import dev.pp.parameters.textTokenParameter.TextTokenParameters;
import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;
import dev.pp.text.error.TextErrorException;
import dev.pp.text.error.handler.TextErrorHandler;
import dev.pp.text.token.TextToken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ASTNodeAttributes {


    public static Parameters attributesToParameters (
        @Nullable ASTNodeAttributes attributes,
        @Nullable TextToken explicitStartToken,
        @Nullable FormalParameters formalParameters,
        @NotNull TextErrorHandler errorHandler ) throws TextErrorException {

        return ParametersCreator.createFromTextParameters (
            attributes != null ? attributes.toTextParameters() : null,
            explicitStartToken,
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

        return contains ( attribute.getName().qualifiedName () );
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

    public void add ( @NotNull ASTNodeAttribute attribute ) {

        if ( contains ( attribute ) ) throw new RuntimeException (
            "Attribute '" + attribute.getName().qualifiedName () + "' exists already." );

        addOrReplace ( attribute );
    }

    public void addOrReplace ( @NotNull ASTNodeAttribute attribute ) {

        map.put ( attribute.getName().qualifiedName (), attribute );
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
