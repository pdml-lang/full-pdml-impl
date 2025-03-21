module dev.pp.pdml {

    requires dev.pp.core;
    requires dev.pp.pjse;
    requires java.xml;
    requires jdk.javadoc;
    requires com.fasterxml.jackson.databind;

/*
    requires dev.pdml.core;
    requires dev.pdml.data;
    requires dev.pdml.html;
    requires dev.pdml.json;
    requires dev.pdml.parser;
    requires dev.pdml.utils;
    requires dev.pdml.writer;
    requires dev.pdml.xml;
 */
    exports dev.pp.pdml.core.parser;
    exports dev.pp.pdml.core.reader;
    exports dev.pp.pdml.core.util;
    exports dev.pp.pdml.core.writer;

    exports dev.pp.pdml.data;
    exports dev.pp.pdml.data.attribute;
    exports dev.pp.pdml.data.exception;
    exports dev.pp.pdml.data.namespace;
    exports dev.pp.pdml.data.node;
    exports dev.pp.pdml.data.node.leaf;
    exports dev.pp.pdml.data.node.tagged;
    exports dev.pp.pdml.data.nodespec;
    exports dev.pp.pdml.data.util;
    exports dev.pp.pdml.data.validation;

    exports dev.pp.pdml.html.treeview;

    exports dev.pp.pdml.json;

    exports dev.pp.pdml.ext;
    exports dev.pp.pdml.ext.scripting.context;
    exports dev.pp.pdml.ext.types;
    exports dev.pp.pdml.ext.types.instances;
    exports dev.pp.pdml.parser;

    exports dev.pp.pdml.reader;

    exports dev.pp.pdml.utils;
    exports dev.pp.pdml.utils.lists;
    exports dev.pp.pdml.utils.networking.socket;
    exports dev.pp.pdml.utils.parser;
    exports dev.pp.pdml.utils.parameterizedtext;
    exports dev.pp.pdml.utils.scripting;
    exports dev.pp.pdml.utils.scriptingapidoc;
    exports dev.pp.pdml.utils.treewalker;
    exports dev.pp.pdml.utils.treewalker.handler;
    exports dev.pp.pdml.utils.treewalker.handler.impl;

    exports dev.pp.pdml.writer;
    exports dev.pp.pdml.writer.node;

    exports dev.pp.pdml.xml;
    exports dev.pp.pdml.xml.eventhandlers;

    exports dev.pp.pdml.companion.commands;
    exports dev.pp.pdml.companion.commands.xml;
    exports dev.pp.pdml.companion.commands.scripting;
    exports dev.pp.pdml.companion.commands.scriptingapidoc;
    exports dev.pp.pdml.companion.cli;
}
