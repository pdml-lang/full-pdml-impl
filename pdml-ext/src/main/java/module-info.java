module dev.pdml.ext {

    requires dev.pp.basics;
    requires dev.pp.text;

    requires dev.pdml.shared;
    requires dev.pdml.data;
    requires dev.pdml.reader;
    requires dev.pdml.parser;
    requires dev.pdml.extshared;
    requires dev.pdml.extutils;
    requires dev.pdml.exttypes;
    requires dev.pdml.extscripting;

    exports dev.pdml.ext;
}
