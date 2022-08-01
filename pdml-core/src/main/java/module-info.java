module dev.pdml.core {

    requires java.xml;

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.texttable;
    requires dev.pp.datatype;
    requires dev.pp.parameters;
    requires dev.pp.scripting;

    exports dev.pdml.core;

    exports dev.pdml.core.data.AST;
    exports dev.pdml.core.data.AST.attribute;
    exports dev.pdml.core.data.AST.children;
    exports dev.pdml.core.data.AST.name;
    exports dev.pdml.core.data.AST.namespace;
    exports dev.pdml.core.data.formalNode;
    exports dev.pdml.core.data.node.name;
    exports dev.pdml.core.data.node.namespace;


    exports dev.pdml.core.exception;
    exports dev.pdml.core.parser;
    exports dev.pdml.core.parser.eventHandler;
    exports dev.pdml.core.reader;
    exports dev.pdml.core.reader.extensions;
}
