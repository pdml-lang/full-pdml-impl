// see https://picocli.info/#_java_9_jpms_modules
open module dev.pdml.cli {

    requires java.xml;

    requires dev.pp.text;
    requires dev.pp.datatype;
    requires dev.pp.parameters;

    requires dev.pdml.core;
    requires dev.pdml.ext;

    requires info.picocli;

    exports dev.pdml.cli;
    exports dev.pdml.cli.commands;
}