package dev.pp.parameters.formalParameter.list;


// remove?

public class FormalParametersBuilder<T> {

/*
    private final Map<String, FormalParameter<T>> nameMap = new HashMap<>();
    private final Map<String, FormalParameter<T>> alternativeNameMap = new HashMap<>();


    public FormalParametersBuilder() {
    }


    public FormalParametersBuilder<T> add ( FormalParameter<T> formalParameter ) throws FormalParameterExistsAlreadyException {

        String name = formalParameter.getName();
        checkNameNotExists ( name );
        nameMap.put ( name, formalParameter );

        if ( formalParameter.getAlternativeNames() != null ) {
            for ( String alternativeName : formalParameter.getAlternativeNames() ) {
                checkNameNotExists ( alternativeName );
                alternativeNameMap.put ( alternativeName, formalParameter );
            }
        }

        return this;
    }

    private void checkNameNotExists ( String name ) throws FormalParameterExistsAlreadyException {

        // TODO? throw RuntimeException
        if ( nameMap.containsKey ( name ) || alternativeNameMap.containsKey ( name ) )
            throw new FormalParameterExistsAlreadyException ( name );
    }

    public FormalParametersBuilder<T> add (
        @NotNull String name,
        @Nullable Set<String> alternativeNames,
        @NotNull ParameterType<T> type,
        int order,
        @Nullable SimpleDocumentation documentation ) throws FormalParameterExistsAlreadyException {

        add ( new FormalParameter<> ( name, alternativeNames, type, order, documentation ) );
        return this;
    }

    public FormalParametersBuilder<T> add (
        @NotNull String name,
        @NotNull ParameterType<T> type,
        @NotNull String title,
        @Nullable String description,
        @Nullable String examples ) throws FormalParameterExistsAlreadyException {

        add ( name, null, type, nameMap.size() + 1,
            new SimpleDocumentation ( title, description, examples ) );
        return this;
    }
*/

//    public addNullableString

/*
    yes_no -> formal_parameter<yes_no>
    in ids ordered_set<string>
    in default_value_supplier object_supplier<yes_no> or null default:null
    in validator object_validator<object:yes_no, error:anticipated_error> or null default:null
    in title string or null default:null
    in description string or null default:null
    in examples string or null default:null

        return create<yes_no> (
    i_ids,
    i_default_value_supplier,
    value_parser = se_yes_no_parser.default_parser
        i_validator,
    i_title, i_description, i_examples )

 */
}
