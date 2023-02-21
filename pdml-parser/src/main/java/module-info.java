module dev.pdml.parser {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.parameters;
    requires dev.pp.scripting;
    requires dev.pp.datatype;

    requires dev.pdml.shared;
    requires dev.pdml.data;
    requires dev.pdml.reader;

    exports dev.pdml.parser;
    exports dev.pdml.parser.eventhandler;
    exports dev.pdml.parser.nodespec;
}
