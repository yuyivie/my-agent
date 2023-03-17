package com.yuy.example1;

import com.yuy.example1.ClazzTransform;

import java.lang.instrument.Instrumentation;

public class AgentPremain {

    public static void premain(String ages, Instrumentation instrumentation){
        instrumentation.addTransformer( new ClazzTransform("com.lauor.agent") );
    }
    //如果没有实现上面的方法，JVM将尝试调用该方法
    public static void premain(String agentArgs) {
    }
}
