module dev.pdml.commands {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.parameters;
    requires dev.pp.commands;
    requires dev.pp.datatype;

    requires dev.pdml.utils;
    requires dev.pdml.xml;

    exports dev.pdml.commands;
    exports dev.pdml.commands.pdmltoxml;
    exports dev.pdml.commands.scriptingapidoc;
    exports dev.pdml.commands.standalone;
}
