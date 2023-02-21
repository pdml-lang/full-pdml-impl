module dev.pdml.xml {

    requires java.xml;

    requires dev.pp.basics;
    requires dev.pp.parameters;
    requires dev.pp.text;

    requires dev.pdml.data;
    requires dev.pdml.parser;
    requires dev.pdml.utils;
    requires dev.pdml.shared;
    requires dev.pdml.writer;

    exports dev.pdml.xml;
    exports dev.pdml.xml.eventhandlers;
}
