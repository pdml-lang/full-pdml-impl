module dev.pdml.ext {

    requires java.xml;
    requires jdk.javadoc;

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.datatype;
    requires dev.pp.parameters;
    requires dev.pp.commands;
    requires dev.pp.texttable;
    requires dev.pp.scripting;

    requires dev.pdml.core;

    exports dev.pdml.ext.commands;
    exports dev.pdml.ext.extensions;
    exports dev.pdml.ext.extensions.node;
    exports dev.pdml.ext.extensions.scripting.bindings;
    exports dev.pdml.ext.extensions.types;
    exports dev.pdml.ext.extensions.types.handlers;
    exports dev.pdml.ext.extensions.utilities;
    exports dev.pdml.ext.utilities;
    exports dev.pdml.ext.utilities.parser;
    exports dev.pdml.ext.utilities.scriptingapidoc;
    exports dev.pdml.ext.utilities.writer;
    exports dev.pdml.ext.utilities.xml;
}
