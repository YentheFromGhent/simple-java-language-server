package com.yenthefromghent.sjls.core.state;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Class that enables listening to a certain state,
 * or checking if a state is active
 */
public class StatesRegistery {

    private static final Logger LOGGER = Logger.getLogger("main");

    private final Set<State> states = ConcurrentHashMap.newKeySet();
    private final Set<Class<? extends State>> stateClasses = ConcurrentHashMap.newKeySet();
    private final Map<Class<? extends State>, List<Runnable>> listeners = new ConcurrentHashMap<>();


    public StatesRegistery() {
        LOGGER.finest("initializing StatesRegistery");
    }

    public void add(State state) {
        Class<? extends State> stateClass = state.getClass();

        boolean isNew = stateClasses.add(stateClass);
        states.add(state);

        // If the state is new, we should notify the listeners
        if (isNew) {
            List<Runnable> listeners = this.listeners.getOrDefault(stateClass, List.of());
            listeners.forEach(Runnable::run);
        }
    }

    public boolean has(Class<? extends State> stateClass) {
        return states.stream().anyMatch(stateClass::isInstance);
    }

    // This will check if there is a object with the exact state in the set
    public boolean contains(State state) {
        return states.contains(state);
    }

    public void remove(State state) {
        states.remove(state);
    }

    // Register to the given state
    public void onState(Class<? extends State> stateClass, Runnable listener) {
        listeners.computeIfAbsent(stateClass, _ -> new CopyOnWriteArrayList<>()).add(listener);

        // If the state was already set at moment of invocation, run the listener immediately
        if (has(stateClass)) {
            listener.run();
        }
    }

}
