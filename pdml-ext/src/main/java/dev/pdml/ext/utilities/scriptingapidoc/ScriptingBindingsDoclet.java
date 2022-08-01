package dev.pdml.ext.utilities.scriptingapidoc;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.source.doctree.*;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import com.sun.source.util.DocTrees;

import dev.pp.basics.annotations.NotNull;
import dev.pp.basics.annotations.Nullable;

import dev.pdml.ext.utilities.writer.PDMLWriter;
import dev.pdml.ext.utilities.writer.PDMLWriterImpl;

public class ScriptingBindingsDoclet implements Doclet {

    // hack to pass arguments to Doclet from its non-argument constructor which is required (not thread-safe!)
    public static @NotNull Map<String, Object> bindings_;
    public static @NotNull Writer targetWriter_;

    private final @NotNull Map<String, Object> bindings;
    private final @NotNull PDMLWriter PDMLWriter;


    public ScriptingBindingsDoclet() throws IOException {
        this ( bindings_, targetWriter_ );
    }

    public ScriptingBindingsDoclet ( @NotNull Map<String, Object> bindings, @NotNull Writer targetWriter )
        throws IOException {

        this.bindings = bindings;
        this.PDMLWriter = new PDMLWriterImpl ( targetWriter );
    }


    @Override
    public void init ( Locale locale, Reporter reporter ) {}

    @Override
    public String getName() { return getClass().getSimpleName(); }

    @Override
    public Set<? extends Option> getSupportedOptions() { return Collections.emptySet(); }

    @Override
    public SourceVersion getSupportedSourceVersion() { return SourceVersion.latest(); }

    @Override
    public boolean run ( DocletEnvironment docletEnv ) {

        // writeDocStart();

        Elements elements = docletEnv.getElementUtils();
        DocTrees docTrees = docletEnv.getDocTrees();

        List<String> bindingNames = new ArrayList<> ( bindings.keySet() );
        bindingNames.sort ( String.CASE_INSENSITIVE_ORDER );
        for ( String bindingName : bindingNames ) {
            Object bindingObject = bindings.get ( bindingName );
            Class<?> bindingClass = bindingObject.getClass();
            Element element = elements.getTypeElement ( bindingClass.getName() );
            // System.out.println ( "element: " + bindingName + " / " + element );
            try {
                handleElement ( element, bindingName, docTrees );
            } catch ( IOException e ) {
                throw new RuntimeException ( e );
            }
        }

        // writeDocEnd();

        return true;
    }

/*
    private void writeDocStart() {
        try {
            // [doc [title Test]
            PDMLWriter.writeNonEmptyNodeStart ( "doc" )
                .writeTextNode ( "title", "Test" )
                .writeNewLine()
                .increaseIndent();
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }

    private void writeDocEnd() {
        try {
            PDMLWriter.writeIndentedBlockNodeEnd();
        } catch ( IOException e ) {
            throw new RuntimeException ( e );
        }
    }
*/

    private void handleElement ( @NotNull Element element, @NotNull String bindingName, @NotNull DocTrees docTrees )
        throws IOException {

        @Nullable DocCommentTree commentTree = docTrees.getDocCommentTree ( element );
        writeBindingStart ( bindingName, commentTree );

        for ( Element childElement : element.getEnclosedElements() ) {
            if ( childElement.getKind() == ElementKind.METHOD ) {
                ExecutableElement method = (ExecutableElement) childElement;

                @NotNull String methodName = method.getSimpleName().toString();

                // skip the method that returns the binding name
                if ( methodName.equals ( ScriptingAPIDocCreator.BINDING_NAME_METHOD_NAME ) )
                    continue;

                @Nullable DocCommentTree methodComment = docTrees.getDocCommentTree ( method );
                writeMethodStart ( bindingName, methodName, methodComment );

                @Nullable List<? extends VariableElement> inputArguments = method.getParameters(); // empty list if there are none
                @Nullable Map<String, List<? extends DocTree>> inputArgumentDocs = getInputArgumentDocs ( methodComment );
                writeInputArguments ( inputArguments, inputArgumentDocs );

                @Nullable TypeMirror returnType = method.getReturnType();
                @Nullable List<? extends DocTree> outputDoc = getOutputDoc ( methodComment );
                boolean isNullable = isNullable ( method );
                writeOutput ( returnType, isNullable, outputDoc );

                @Nullable List<? extends TypeMirror> thrownTypes = method.getThrownTypes();
                @Nullable Map<String, List<? extends DocTree>> throwablesDocs = getThrowablesDocs ( methodComment );
                writeThrowables ( thrownTypes, throwablesDocs );

                writeMethodEnd ();
            }
        }

        writeBindingEnd ();
    }



    private void writeBindingStart (
        @NotNull String bindingName,
        @Nullable DocCommentTree doc ) throws IOException {

        // [ch (id=timeUtils) [title Object [c timeUtils]]
        PDMLWriter.writeNewLine()
            .writeIndent()
            .writeNonEmptyNodeStart ( "ch" )
            .write ( "(id=" )
            .writeText ( bindingName )
            .write ( ") " )
            .writeNonEmptyNodeStart ( "title" )
            .write ( "Object " )
            .writeTextNode ( "c", bindingName )
            .writeNodeEndSymbol()
            .writeNewLine()
            .increaseIndent();

        writeDocTreeHTMLBody ( doc );
    }

    private void writeBindingEnd () throws IOException {

        PDMLWriter.writeIndentedBlockNodeEnd();
    }

    private void writeMethodStart (
        @NotNull String bindingName,
        @NotNull String methodName,
        @Nullable DocCommentTree doc ) throws IOException {

        // TODO? append method signature after name
        // [ch (id=timeUtils-formattedCurrentLocalDateTime) [title Function [c formattedCurrentLocalDateTime]]
        PDMLWriter.writeNewLine()
            .writeIndent()
            .writeNonEmptyNodeStart ( "ch" )
            .write ( "(id=" )
            .writeText ( bindingName )
            .write ( "-" )
            .writeText ( methodName )
            .write ( ") " )
            .writeNonEmptyNodeStart ( "title" )
            .write ( "Function " )
            .writeTextNode ( "c", methodName )
            .writeNodeEndSymbol()
            .writeNewLine()
            .increaseIndent();

        writeDocTreeHTMLBody ( doc );
    }

    private void writeMethodEnd () throws IOException {

        PDMLWriter.writeIndentedBlockNodeEnd();
    }

    private void writeInputArguments (
        @Nullable List<? extends VariableElement> inputArguments,
        @Nullable Map<String, List<? extends DocTree>> inputArgumentDocs ) throws IOException {

        if ( inputArguments == null || inputArguments.isEmpty () ) {
            // [p Input: none]
            PDMLWriter.writeIndent()
                .writeTextNode ( "p", "Input: none" )
                .writeNewLine();

        } else {
            // [p Input:]
            PDMLWriter.writeIndent()
                .writeTextNode ( "p", "Input:" )
                .writeNewLine();

            // [list
            PDMLWriter.writeIndentedBlockNodeStart ( "list" );

            for ( VariableElement inputArgument : inputArguments ) {

                String name = inputArgument.getSimpleName().toString();

                boolean isNullable = isNullable ( inputArgument );
                String type = typeToString ( inputArgument.asType(), isNullable );

                // [el [c format String]: [verbatim A string defining the format to use]]
                PDMLWriter.writeIndent()
                    .writeNonEmptyNodeStart ( "el" )
                    .writeTextNode ( "c", name + " " + type );
                List<? extends DocTree> doc = inputArgumentDocs == null ? null : inputArgumentDocs.get ( name );
                if ( doc != null ) {
                    PDMLWriter.write ( ": " );
                    writeInlineHTML ( doc.toString() );
                }
                PDMLWriter.writeNodeEndSymbol()
                    .writeNewLine();
            }

            PDMLWriter.writeIndentedBlockNodeEnd(); // list
        }
    }

    private void writeOutput (
        @Nullable TypeMirror typeMirror,
        boolean isNullable,
        @Nullable List<? extends DocTree> doc ) throws IOException {

        if ( typeMirror == null || typeMirror.getKind() == TypeKind.VOID ) {
            // [p Output: none]
            PDMLWriter.writeIndent()
                .writeTextNode ( "p", "Output: none" )
                .writeNewLine();

        } else {
            // [p Output:]
            PDMLWriter.writeIndent()
                .writeTextNode ( "p", "Output:" )
                .writeNewLine();

            // [list
            PDMLWriter.writeIndentedBlockNodeStart ( "list" );

            // [el [c format String]: [verbatim A string defining the format to use]]
            PDMLWriter.writeIndent()
                .writeNonEmptyNodeStart ( "el" )
                .writeTextNode ( "c", typeToString ( typeMirror, isNullable ) );
            if ( doc != null ) {
                PDMLWriter.write ( ": " );
                writeInlineHTML ( doc.toString() );
            }
            PDMLWriter.writeNodeEndSymbol()
                .writeNewLine()
                .writeIndentedBlockNodeEnd(); // list
        }
    }

    private void writeThrowables (
        @Nullable List<? extends TypeMirror> thrownTypes,
        @Nullable Map<String, List<? extends DocTree>> throwablesDocs ) throws IOException {

        if ( thrownTypes == null || thrownTypes.isEmpty() ) {
            return;

        } else {
            // [p Throws:]
            PDMLWriter.writeIndent()
                .writeTextNode ( "p", "Throws:" )
                .writeNewLine();

            // [list
            PDMLWriter.writeIndentedBlockNodeStart ( "list" );

            for ( TypeMirror thrownType : thrownTypes ) {

                String type = typeToString ( thrownType, false );

                // [el [c format String]: [verbatim A string defining the format to use]]
                PDMLWriter.writeIndent()
                    .writeNonEmptyNodeStart ( "el" )
                    .writeTextNode ( "c", type );
                List<? extends DocTree> doc = throwablesDocs == null ? null : throwablesDocs.get ( type );
                if ( doc != null ) {
                    PDMLWriter.write ( ": " );
                    writeInlineHTML ( doc.toString() );
                }
                PDMLWriter.writeNodeEndSymbol()
                    .writeNewLine();
            }

            PDMLWriter.writeIndentedBlockNodeEnd(); // list
        }
    }


    private void writeDocTreeHTMLBody ( @Nullable DocCommentTree docTree ) throws IOException {

        if ( docTree == null ) return;

        // [p HTML code]
        PDMLWriter.writeIndent()
            .writeNonEmptyNodeStart ( "p" );
        writeInlineHTML ( docTree.getFullBody().toString() );
        PDMLWriter.writeNodeEndSymbol()
            .writeNewLine();
    }

    private void writeInlineHTML ( @Nullable String HTMLCode ) throws IOException {

        // [verbatim Get the \[current\] local <b>date and time</b>]
        String bracketsEscaped = HTMLCode
            .replace ( "\\", "\\\\" )
            .replace ( "[", "\\[" )
            .replace ( "]", "\\]" );
        PDMLWriter.writeNonEmptyNodeStart ( "verbatim" )
            .write ( bracketsEscaped )
            .writeNodeEndSymbol();
    }

    private @Nullable Map<String, List<? extends DocTree>> getInputArgumentDocs ( @Nullable DocCommentTree methodComment ) {

        if ( methodComment == null ) return null;

        List<? extends DocTree> tags = methodComment.getBlockTags();
        if ( tags == null ) return null;

        Map<String, List<? extends DocTree>> inputArgumentDocs = new HashMap<>();
        for ( DocTree tag : tags ) {
            if ( tag instanceof ParamTree paramDoc ) {
                String name = paramDoc.getName().getName().toString();
                List<? extends DocTree> description = paramDoc.getDescription();
                inputArgumentDocs.put ( name, description );
            }
        }

        return inputArgumentDocs.isEmpty() ? null : inputArgumentDocs;
    }

    private @Nullable Map<String, List<? extends DocTree>> getThrowablesDocs ( @Nullable DocCommentTree methodComment ) {

        if ( methodComment == null ) return null;

        List<? extends DocTree> tags = methodComment.getBlockTags();
        if ( tags == null ) return null;

        Map<String, List<? extends DocTree>> throwablesDocs = new HashMap<>();
        for ( DocTree tag : tags ) {
            if ( tag instanceof ThrowsTree throwsDoc ) {
                String throwableName = throwsDoc.getExceptionName().toString();
                List<? extends DocTree> description = throwsDoc.getDescription();
                throwablesDocs.put ( throwableName, description );
            }
        }

        return throwablesDocs.isEmpty() ? null : throwablesDocs;
    }

    private @Nullable List<? extends DocTree> getOutputDoc ( @Nullable DocCommentTree methodComment ) {

        if ( methodComment == null ) return null;

        List<? extends DocTree> tags = methodComment.getBlockTags();
        if ( tags == null ) return null;

        for ( DocTree tag : tags ) {
            if ( tag instanceof ReturnTree returnDoc ) return returnDoc.getDescription();
        }

        return null;
    }

    private @NotNull String typeToString ( TypeMirror type, boolean isNullable ) {

        String r = removePackagePaths ( type.toString() );
        if ( isNullable ) r = r + " or null";
        return r;
    }

    public static @NotNull String removePackagePaths ( @NotNull String type ) {

        // e.g java.lang.String -> String
        Pattern pattern = Pattern.compile ( "([a-zA-Z]+\\.)+(?<typeName>[a-zA-Z]+)" );
        StringBuilder result = new StringBuilder();

        int lastMatchEndIndex = 0;

        Matcher matcher = pattern.matcher ( type );
        while ( matcher.find() ) {

            int matchStartIndex = matcher.start();
            int matchEndIndex = matcher.end();
            String typeName = matcher.group ( "typeName" );

            if ( matchStartIndex > lastMatchEndIndex )
                result.append ( type.substring ( lastMatchEndIndex, matchStartIndex ) );
            result.append ( typeName );
            lastMatchEndIndex = matchEndIndex;
        }
        result.append ( type.substring ( lastMatchEndIndex ) );
        return result.toString();
    }

    private boolean isNullable ( Element element ) {

        return element.getAnnotation ( Nullable.class ) != null;
    }
}
