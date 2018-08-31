package com.wx.somecode;

import com.sun.source.tree.Tree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author xinquan.huangxq
 */
@SupportedAnnotationTypes("com.wx.somecode.MyUtil")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyUtilProcessor extends AbstractProcessor {

    private Trees trees;

    private TreeMaker treeMaker;

    private Name.Table names;

    /**
     * 初始化，获取编译环境
     *
     * @param env
     */
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        trees = Trees.instance(env);
        Context context = ((JavacProcessingEnvironment) env).getContext();
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context).table;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(MyUtil.class)) {
            // 只处理作用在类上的注解
            if (element.getKind() == ElementKind.CLASS) {
                removeUnStatic(element);
                addPrivateConstructor(element);
                addFinalModifier(element);
            }
        }
        return true;
    }

    protected void removeUnStatic(Element element) {
        JCTree tree = (JCTree) trees.getTree(element);
        tree.accept(new TreeTranslator() {

            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                List<JCTree> availableDefs = List.nil();

                for (JCTree def : jcClassDecl.defs) {
                    if (def.getKind().equals(Tree.Kind.VARIABLE)) {
                        JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) def;
                        if (jcClassDecl.getModifiers().getFlags().contains(Modifier.STATIC)) {
                            availableDefs = availableDefs.append(jcVariableDecl);
                        }
                    } else if (def.getKind().equals(Tree.Kind.METHOD)) {
                        JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) def;
                        if (jcMethodDecl.getModifiers().getFlags().contains(Modifier.STATIC)) {
                            availableDefs = availableDefs.append(jcMethodDecl);
                        }
                    }
                }

                jcClassDecl.defs = availableDefs;
            }
        });
    }

    /**
     * 添加私有构造器
     *
     * @param element 拥有注解的元素
     */
    protected void addPrivateConstructor(Element element) {
        JCTree tree = (JCTree) trees.getTree(element);
        tree.accept(new TreeTranslator() {

            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                jcClassDecl.mods = (JCTree.JCModifiers) this.translate((JCTree) jcClassDecl.mods);
                jcClassDecl.typarams = this.translateTypeParams(jcClassDecl.typarams);
                jcClassDecl.extending = (JCTree.JCExpression) this.translate((JCTree) jcClassDecl.extending);
                jcClassDecl.implementing = this.translate(jcClassDecl.implementing);

                ListBuffer<JCTree> statements = new ListBuffer<>();

                List<JCTree> oldList = this.translate(jcClassDecl.defs);
                boolean hasPrivateConstructor = false;  //是否拥有私有构造器

                //1. 将原来的方法添加进来
                //2. 判断是否已经有默认私有构造器
                for (JCTree jcTree : oldList) {
                    if (isPublicDefaultConstructor(jcTree)) {
                        continue;   //不添加共有默认构造器
                    }
                    if (isPrivateDefaultConstructor(jcTree)) {
                        hasPrivateConstructor = true;
                    }
                    statements.append(jcTree);
                }

                if (!hasPrivateConstructor) {
                    JCTree.JCBlock block = treeMaker.Block(0L, List.<JCTree.JCStatement>nil()); //代码方法内容
                    JCTree.JCMethodDecl constructor = treeMaker.MethodDef(
                            treeMaker.Modifiers(Flags.PRIVATE, List.<JCTree.JCAnnotation>nil()),
                            names.fromString(JcTreesHelper.CONSTRUCTOR_NAME),
                            null,
                            List.<JCTree.JCTypeParameter>nil(),
                            List.<JCTree.JCVariableDecl>nil(),
                            List.<JCTree.JCExpression>nil(),
                            block,
                            null);
                    statements.append(constructor);
                    jcClassDecl.defs = statements.toList(); //更新
                }

                this.result = jcClassDecl;
            }
        });
    }

    /**
     * 添加 final 修饰符
     * 1. 将工具类的修饰符定义为: public final;
     *
     * @param element 拥有注解的元素
     */
    protected void addFinalModifier(Element element) {
        JCTree tree = (JCTree) trees.getTree(element);
        tree.accept(new TreeTranslator() {
            @Override
            public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                jcClassDecl.mods = treeMaker.Modifiers(Flags.PUBLIC | Flags.FINAL, List.<JCTree.JCAnnotation>nil());
            }
        });
    }


    /**
     * 是否为私有默认构造器
     *
     * @param jcTree
     * @return
     */
    protected boolean isPrivateDefaultConstructor(JCTree jcTree) {
        if (jcTree.getKind() == Tree.Kind.METHOD) {
            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) jcTree;
            if (JcTreesHelper.isConstructor(jcMethodDecl)
                    && JcTreesHelper.isNoArgsMethod(jcMethodDecl)
                    && JcTreesHelper.isPrivateMethod(jcMethodDecl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为共有默认构造器
     *
     * @param jcTree
     * @return
     */
    protected boolean isPublicDefaultConstructor(JCTree jcTree) {
        if (jcTree.getKind() == Tree.Kind.METHOD) {
            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) jcTree;
            if (JcTreesHelper.isConstructor(jcMethodDecl)
                    && JcTreesHelper.isNoArgsMethod(jcMethodDecl)
                    && JcTreesHelper.isPublicMethod(jcMethodDecl)) {
                return true;
            }
        }
        return false;
    }
}
