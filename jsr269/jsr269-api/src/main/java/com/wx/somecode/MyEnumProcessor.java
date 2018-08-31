package com.wx.somecode;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

/**
 * @author xinquan.huangxq
 */
@SupportedAnnotationTypes("com.wx.somecode.MyEnum")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyEnumProcessor extends AbstractProcessor {

    private Messager messager;

    private JavacTrees trees;

    private TreeMaker treeMaker;

    private Names names;

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();

        this.treeMaker = TreeMaker.instance(context);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        this.names = Names.instance(context);
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(MyEnum.class);
        elementsAnnotatedWith.forEach(element -> {
            if (element.getKind() != ElementKind.CLASS) {
                return;
            }

            String builderClassName = element.getSimpleName().toString();
            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(builderClassName)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);

            JCTree tree = trees.getTree(element);
            tree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    if (tree.getKind().equals(Tree.Kind.VARIABLE)) {

                    }
                }
            });

            try {
                JavaFile.builder("com.wx.somecode", typeBuilder.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
