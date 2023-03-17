package com.yuy.example1;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

public class ClazzTransform implements ClassFileTransformer {

    /** 增强类所在包名白名单 */
    private final String BASE_PACKAGE;

    public ClazzTransform(String basePackage) {
        this.BASE_PACKAGE = basePackage;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        className = className.replace("/", ".");
        if ( !className.startsWith(BASE_PACKAGE) ){
            return null;
        }
        try {
            CtClass ctKlass = ClassPool.getDefault().get(className);
            CtBehavior[] behaviors = ctKlass.getDeclaredBehaviors();
            //遍历方法进行增强
            for (CtBehavior m : behaviors) {
                enhanceMethod(m);
            }
            byte[] bytes = ctKlass.toBytecode();
            //输出修改后的class文件内容
            Files.write(Paths.get("D:\\A.class"), bytes);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classFileBuffer;
    }
    /** 方法增强，添加方法耗时统计 */
    private void enhanceMethod(CtBehavior method) throws CannotCompileException {
        if ( method.isEmpty() ){
            return;
        }
        String methodName = method.getName();
        method.addLocalVariable("start", CtClass.longType);
        method.insertBefore("start = System.currentTimeMillis();");
        method.insertAfter( String.format("System.out.println(\"%s cost: \" + (System.currentTimeMillis() - start) + \"ms\");", methodName) );
    }
}
