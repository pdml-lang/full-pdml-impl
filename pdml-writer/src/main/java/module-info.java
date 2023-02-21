module dev.pdml.writer {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.parameters;

/*
    // TODO remove when no more needed
    requires dev.pp.datatype;
    requires dev.pp.texttable;
    requires dev.pp.datatype;
    requires dev.pp.scripting;
*/

    requires dev.pdml.shared;
    requires dev.pdml.data;

    exports dev.pdml.writer;
    exports dev.pdml.writer.data;
}
