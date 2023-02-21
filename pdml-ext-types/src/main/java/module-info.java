module dev.pdml.exttypes {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.datatype;

    requires dev.pdml.shared;
    requires dev.pdml.data;
    requires dev.pdml.reader;
    requires dev.pdml.parser;
    requires dev.pdml.extshared;

    exports dev.pdml.exttypes;
    exports dev.pdml.exttypes.handlers;
}
