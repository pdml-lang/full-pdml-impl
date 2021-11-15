module dev.pp.parameters {

    requires dev.pp.text;
    requires dev.pp.datatype;

    exports dev.pp.parameters.formalParameter;
    exports dev.pp.parameters.formalParameter.list;
    exports dev.pp.parameters.parameter;
    exports dev.pp.parameters.parameter.list;
    exports dev.pp.parameters.textTokenParameter;
}