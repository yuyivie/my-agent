package com.yuy.example;

import java.lang.instrument.Instrumentation;

/**
 * Created by @author yihui in 16:39 20/3/15.
 */
public class SimpleAgent {
    /**
     * jvm 参数形式启动，运行此方法
     *
     * manifest需要配置属性Premain-Class
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        customLogic(inst);
    }
    /**
     * 动态 attach 方式启动，运行此方法
     *
     * manifest需要配置属性Agent-Class
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("agentmain");
        customLogic(inst);
    }
    /**
     * 统计方法耗时
     *
     * @param inst
     */
    private static void customLogic(Instrumentation inst) {
        inst.addTransformer(new CostTransformer(), true);
    }
}
