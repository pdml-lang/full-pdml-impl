module dev.pdml.extscripting {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.parameters;
    requires dev.pp.scripting;

    requires dev.pdml.shared;
    requires dev.pdml.data;
    requires dev.pdml.reader;
    requires dev.pdml.parser;
    requires dev.pdml.extshared;
    requires dev.pdml.exttypes;

    exports dev.pdml.extscripting;
    exports dev.pdml.extscripting.bindings;
}
