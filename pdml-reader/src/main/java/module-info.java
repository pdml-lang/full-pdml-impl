module dev.pdml.reader {

    requires dev.pp.basics;
    requires dev.pp.text;
    requires dev.pp.scripting;
    requires dev.pp.texttable;

/*
    // TODO remove when no more needed
    requires dev.pp.parameters;
    requires dev.pp.datatype;
    requires dev.pp.datatype;
*/

    requires dev.pdml.shared;
//    requires dev.pdml.data;

    exports dev.pdml.reader;
    exports dev.pdml.reader.extensions;
}
