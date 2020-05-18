package com.wx.somecode.agent;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 *
 * @author xinquan.huangxq
 */
public class MonitorClassFileTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("java") || className.startsWith("jdk") || className.startsWith("javax") || className.startsWith("sun") || className.startsWith("com/sun") || className.startsWith("com/wx/somecode/agent")) {
            //return null或者执行异常会执行原来的字节码
            return null;
        }
        System.out.println("loaded class: " + className);
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        reader.accept(new MonitorClassVisitor(writer), ClassReader.EXPAND_FRAMES);
        return writer.toByteArray();
    }
}
