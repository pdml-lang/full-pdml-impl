module dev.pdml.shared {

    requires dev.pp.basics;
    requires dev.pp.text;

/*
    // TODO remove when no more needed
    requires dev.pp.parameters;
    requires dev.pp.datatype;
    requires dev.pp.texttable;
    requires dev.pp.datatype;
    requires dev.pp.scripting;
*/

    exports dev.pdml.shared.constants;
    exports dev.pdml.shared.exception;
    exports dev.pdml.shared.utilities;
}
