package ru.hse.mnmalysheva.test2;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/** Depndebcy injector. */
public class Injector {
    private static Map<Class<?>, State> classStates = new HashMap<>();
    private static Map<Class<?>, Object> interfaceInstances = new HashMap<>();
    private static List<Class<?>> implementationClasses = new ArrayList<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     * @param rootClassName class to be instanced.
     * @param implementationClassNames list of classes that can be used to create class.
     * @return class instance.
     * @throws ImplementationNotFoundException no implementation of interface can be found.
     * @throws InjectionCycleException injections are cyclic.
     * @throws ClassNotFoundException one of classes does not exist.
     * @throws AmbiguousImplementationException interface have more than one implementation.
     * @throws IllegalAccessException constructor is private.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames)
            throws ImplementationNotFoundException, InjectionCycleException, ClassNotFoundException,
            InvocationTargetException, AmbiguousImplementationException, InstantiationException,
            IllegalAccessException {
        classStates.clear();
        interfaceInstances.clear();
        implementationClasses.clear();

        classStates.put(Class.forName(rootClassName), State.FRESH);
        implementationClasses.add(Class.forName(rootClassName));
        for (var name : implementationClassNames) {
            classStates.put(Class.forName(name), State.FRESH);
            implementationClasses.add(Class.forName(name));
        }
        return initialize(Class.forName(rootClassName));
    }

    private static Object initialize(Class<?> rootClass)
            throws ImplementationNotFoundException, InjectionCycleException,
            AmbiguousImplementationException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        if (rootClass.isInterface()) {
            Set<Class<?>> possibleImplementations = new HashSet<>();
            for (var c : implementationClasses) {
                if (rootClass.isAssignableFrom(c)) {
                    possibleImplementations.add(c);
                }
            }
            if (possibleImplementations.isEmpty()) {
                throw new ImplementationNotFoundException();
            }
            if (possibleImplementations.size() > 1) {
                throw new AmbiguousImplementationException();
            }
            var instance = initialize(possibleImplementations.iterator().next());
            interfaceInstances.put(rootClass, instance);
            classStates.put(rootClass, State.LOADED_INTERFACE);
            return instance;
        }

        State currentState = classStates.get(rootClass);
        if (currentState == null) {
            throw new ImplementationNotFoundException();
        }
        if (currentState == State.TOUCHED) {
            throw new InjectionCycleException();
        }
        if (currentState == State.LOADED_INTERFACE) {
            return interfaceInstances.get(rootClass);
        }

        classStates.put(rootClass, State.TOUCHED);

        var rootClassConstructors = rootClass.getConstructors();
        assert(rootClassConstructors.length == 1);
        var constructor = rootClassConstructors[0];

        var parameters = new ArrayList<>();
        for (var parameter : constructor.getParameterTypes()) {
            parameters.add(initialize(parameter));
        }
        classStates.put(rootClass, State.LOADED);
        return constructor.newInstance(parameters.toArray());
    }

    private enum State {
        FRESH, TOUCHED, LOADED, LOADED_INTERFACE
    }
}
