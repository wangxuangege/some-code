package com.wx.somecode.agent;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 *
 * @author xinquan.huangxq
 */
public class MonitorClassVisitor extends ClassVisitor {

    public MonitorClassVisitor(ClassWriter writer) {
        super(Opcodes.ASM5, writer);
    }

    @Override
    public MethodVisitor visitMethod(int methodAccess, String methodName, String methodDesc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(methodAccess, methodName, methodDesc, signature, exceptions);
        return new MonitorAdviceAdapter(Opcodes.ASM5, methodVisitor, methodAccess, methodName, methodDesc);
    }
}
