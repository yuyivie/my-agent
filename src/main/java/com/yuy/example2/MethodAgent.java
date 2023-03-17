package com.yuy.example2;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class MethodAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {

            String classPkgName = className.replace('/', '.');
            if (!classPkgName.startsWith("com.yuy")) {
                return classfileBuffer;
            }

            ClassPool pool = ClassPool.getDefault();
            try {
                CtClass ctClass = pool.get(classPkgName);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                    method.insertBefore("System.out.println(\"方法" + method.getName() + "开始\");");
                    method.insertAfter("System.out.println(\"方法" + method.getName() + "结束\");");
                }
                return ctClass.toBytecode();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
            return classfileBuffer;
        });
    }
}
