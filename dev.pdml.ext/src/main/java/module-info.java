module dev.pdml.ext {

    requires java.xml;

    requires org.graalvm.sdk;

    requires dev.pp.text;
    requires dev.pp.datatype;
    requires dev.pp.parameters;

    requires dev.pdml.core;

    exports dev.pdml.ext.extensions;
    exports dev.pdml.ext.extensions.node;
    exports dev.pdml.ext.extensions.node.standard;
    exports dev.pdml.ext.extensions.node.types;

    exports dev.pdml.ext.utilities;
    exports dev.pdml.ext.scripting;
}