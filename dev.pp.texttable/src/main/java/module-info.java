module dev.pp.texttable {

    requires dev.pp.text;

    exports dev.pp.texttable.data;
    exports dev.pp.texttable.data.impls;
    exports dev.pp.texttable.writer.pretty;
    exports dev.pp.texttable.writer.pretty.config;
    exports dev.pp.texttable.writer.pretty.utilities;
}