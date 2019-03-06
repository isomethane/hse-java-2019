package ru.hse.mnmalysheva.test2;

import ru.hse.mnmalysheva.test2.testclasses.ClassWithOneClassDependency;
import ru.hse.mnmalysheva.test2.testclasses.ClassWithOneInterfaceDependency;
import ru.hse.mnmalysheva.test2.testclasses.ClassWithoutDependencies;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.hse.mnmalysheva.test2.testclasses.InterfaceImpl;

import java.util.Collections;

public class InjectorTest {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("ru.hse.mnmalysheva.test2.testclasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.hse.mnmalysheva.test2.testclasses.ClassWithOneClassDependency",
                Collections.singletonList("ru.hse.mnmalysheva.test2.testclasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "ru.hse.mnmalysheva.test2.testclasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("ru.hse.mnmalysheva.test2.testclasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}