package ru.hse.mnmalysheva.test2.testclasses;

public class ClassWithOneClassDependency {

    public final ClassWithoutDependencies dependency;

    public ClassWithOneClassDependency(ClassWithoutDependencies dependency) {
        this.dependency = dependency;
    }
}
