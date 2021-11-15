module dev.pp.datatype {

    requires dev.pp.text;

    exports dev.pp.datatype;
    exports dev.pp.datatype.common.Boolean;
    exports dev.pp.datatype.common.file;
    exports dev.pp.datatype.common.path;
    exports dev.pp.datatype.common.string;
    exports dev.pp.datatype.parser;
    exports dev.pp.datatype.validator;
    exports dev.pp.datatype.writer;
}