package com.yenthefromghent.jlsp.core.lsp;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MethodHandler {

    private final Logger LOGGER = Logger.getLogger("main");

    private final Map<String, MethodBinding> methodsMap = new HashMap<>();

    /**
     * function to register a rpc method into the methodsMap
     * so that it can later be called with handleMessage()
     * @param method the method you want to register
     * @param instance the class object of that method
     */
    public void registerMethod(Method method, Object instance) {
        if (methodsMap.containsKey(method.getName())) {
            LOGGER.severe("Method " + method.getName() + " is already registered!");
            throw new IllegalArgumentException("Method " + method.getName() + " is already registered!");
        }

        if (!method.isAnnotationPresent(RPCMethod.class)) {
            LOGGER.severe("Method " + method.getName() + " is not annotated with @RPCMethod!");
            throw new IllegalArgumentException("Method " + method.getName() + " is not annotated with @RPCMethod!");
        }

        methodsMap.put(method.getName(), new MethodBinding(method, instance));
    }

    /**
     * function to remove a method from the methodMap
     * @param methodName name of the method you want to remove
     */
    public void removeMethod(String methodName) {
        if (!methodsMap.containsKey(methodName)) {
            LOGGER.severe("Method " + methodName + " is not registered!");
            throw new IllegalArgumentException("Method " + methodName + " is not registered!");
        }
        methodsMap.remove(methodName);
    }



    public record MethodBinding(Method method, Object instance) {}
}
