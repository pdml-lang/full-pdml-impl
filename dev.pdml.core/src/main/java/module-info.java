module dev.pdml.core {

    requires java.xml;

    requires dev.pp.text;
    requires dev.pp.datatype;
    requires dev.pp.parameters;

    exports dev.pdml.core;

    exports dev.pdml.core.data.AST;
    exports dev.pdml.core.data.AST.attribute;
    exports dev.pdml.core.data.AST.children;
    exports dev.pdml.core.data.AST.name;
    exports dev.pdml.core.data.AST.namespace;
    exports dev.pdml.core.data.formalNode;
    exports dev.pdml.core.data.formalNode.types;
    exports dev.pdml.core.data.formalNode.types.standard;
    exports dev.pdml.core.data.node.name;
    exports dev.pdml.core.data.node.namespace;

    exports dev.pdml.core.reader.exception;
    exports dev.pdml.core.reader.parser;
    exports dev.pdml.core.reader.parser.eventHandler;
    exports dev.pdml.core.reader.parser.eventHandler.impls;
    exports dev.pdml.core.reader.reader;
    exports dev.pdml.core.reader.reader.extensions;

    exports dev.pdml.core.utilities;

    exports dev.pdml.core.writer;
}