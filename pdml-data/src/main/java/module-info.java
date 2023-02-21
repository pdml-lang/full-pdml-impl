module dev.pdml.data {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.parameters;

    requires dev.pdml.shared;

    exports dev.pdml.data.attribute;
    exports dev.pdml.data.namespace;
    exports dev.pdml.data.node;
    exports dev.pdml.data.node.branch;
    exports dev.pdml.data.node.leaf;
    exports dev.pdml.data.node.root;
}
