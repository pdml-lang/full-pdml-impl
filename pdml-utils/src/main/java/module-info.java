module dev.pdml.utils {

    requires jdk.javadoc;

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.datatype;
    requires dev.pp.parameters;
    requires dev.pp.texttable;
    requires dev.pp.scripting;

    requires dev.pdml.data;
    requires dev.pdml.reader;
    requires dev.pdml.parser;
    requires dev.pdml.writer;
    requires dev.pdml.ext;
    requires dev.pdml.extscripting;

    exports dev.pdml.utils;
    exports dev.pdml.utils.parser;
    exports dev.pdml.utils.parser.eventhandlers;
    exports dev.pdml.utils.parameterizedtext;
    exports dev.pdml.utils.scriptingapidoc;
}
