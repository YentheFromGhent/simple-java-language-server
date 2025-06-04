package com.yenthefromghent.sjls.core.lsp;

import com.fasterxml.jackson.databind.JsonNode;
import com.yenthefromghent.sjls.core.lsp.methods.Registerable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RPCMethodRegistery {

    private static final Logger LOGGER = Logger.getLogger("main");

    private static final Map<String, MethodBinding> methodsMap = new HashMap<>();

    /**
     * function to register a rpc method into the methodsMap so that it can later be called with handleMessage()
     * @param method the method you want to register
     * @param instance the Object of that method
     */
    public static void registerMethod(Method method, Object instance) {
        if (methodsMap.containsKey(method.getName())) {
            LOGGER.severe("Method " + method.getName() + " is already registered!");
            throw new IllegalArgumentException("Method " + method.getName() + " is already registered!");
        }

        if (!method.isAnnotationPresent(RPCMethod.class)) {
            LOGGER.severe("Method " + method.getName() + " is not annotated with @RPCMethod!");
            throw new IllegalArgumentException("Method " + method.getName() + " is not annotated with @RPCMethod!");
        }

        LOGGER.finest("Registering method " + method.getName());
        methodsMap.put(method.getName(), new MethodBinding(method, instance));
    }

    /**
     * function to remove a method from the methodMap
     * @param methodName name of the method you want to remove
     */
    public static void removeMethod(String methodName) {
        if (!methodsMap.containsKey(methodName)) {
            LOGGER.severe("Method " + methodName + " is not registered!");
            throw new IllegalArgumentException("Method " + methodName + " is not registered!");
        }
        methodsMap.remove(methodName);
    }

    public static List<MethodBinding> getEntries() {
        return new ArrayList<>(methodsMap.values());
    }

    public static void invokeMethod(String methodName, JsonNode message) {
        if (!methodsMap.containsKey(methodName)) {
            LOGGER.severe("Method " + methodName + " is not registered!");
            return;
        }

        MethodBinding methodBinding = methodsMap.get(methodName);

        try {
            LOGGER.finest("Invoking method " + methodName);
            methodBinding.method().invoke(methodBinding.instance, message);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error invoking method: " + methodName + " with error: " + e);
        }
    }

    /**
     * function that will register all methods that implement RPCInstance and are registerd
     * in the META-INF/services directory
     * @throws NoSuchMethodException when method declared does not exist.
     */
    public static void registerRPCMethodsToRegistery() throws NoSuchMethodException {
        ServiceLoader<Registerable> loader = ServiceLoader.load(Registerable.class);
        for (Registerable instance : loader) {
            instance.register();
        }
        LOGGER.info("Registered RPC Methods");
    }

    /**
     * record class that holds the method and its instance.
     * @param method
     * @param instance
     */
    public record MethodBinding(Method method, Object instance) {}
}
