// see https://picocli.info/#_java_9_jpms_modules
// open module dev.pdml.cla {
module dev.pdml.commands {

    // requires java.xml;

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.texttable;
    requires dev.pp.datatype;
    requires dev.pp.parameters;
    requires dev.pp.commands;
    // requires dev.pp.scripting;

    // requires dev.pdml.core;
    requires dev.pdml.ext;

    requires info.picocli;

    exports dev.pdml.commands;
    exports dev.pdml.commands.logparserevents;
    exports dev.pdml.commands.pdmltoxml;
    exports dev.pdml.commands.scriptingapidoc;
    exports dev.pdml.commands.standalone;
}
