package com.yuy.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class CostTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        // 这里我们限制下，只针对目标包下进行耗时统计
//        if (!className.startsWith("com/yuy/example/")) {
//            return classfileBuffer;
//        }
        if (!className.startsWith("com/cps/csp/serverapi/controller/")) {
            return classfileBuffer;
        }
        CtClass cl = null;
        try {
            ClassPool classPool = ClassPool.getDefault();
            cl = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            for (CtMethod method : cl.getDeclaredMethods()) {
                // 所有方法，统计耗时；请注意，需要通过`addLocalVariable`来声明局部变量
                method.addLocalVariable("start", CtClass.longType);
                method.insertBefore("start = System.currentTimeMillis();");
                String methodName = method.getLongName();
                method.insertAfter("System.out.println(\"" + methodName + " 耗时: \" + (System" +
                        ".currentTimeMillis() - start));");
            }
            byte[] transformed = cl.toBytecode();
            return transformed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }
}
