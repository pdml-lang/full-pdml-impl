module dev.pdml.extutils {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.parameters;
    requires dev.pp.datatype;

/*
    // TODO remove when no more needed
    requires dev.pp.datatype;
    requires dev.pp.texttable;
    requires dev.pp.scripting;
*/

    requires dev.pdml.data;
    requires dev.pdml.reader;
    requires dev.pdml.parser;
    requires dev.pdml.extshared;

    exports dev.pdml.extutils;
}
