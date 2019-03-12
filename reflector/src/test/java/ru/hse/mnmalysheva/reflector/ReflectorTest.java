package ru.hse.mnmalysheva.reflector;

import org.joor.Reflect;
import org.junit.jupiter.api.Test;
import ru.hse.mnmalysheva.reflector.testclasses.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractList;

import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {
    private static final String IDENTICAL_CLASSES_MESSAGE = "Classes are identical\n";

    private static String printStructureResult(Class<?> someClass) {
        var out = new ByteArrayOutputStream();
        Reflector.printStructure(someClass, new PrintStream(out));
        return out.toString();
    }

    private static String diffClassesResult(Class<?> firstClass, Class<?> secondClass) {
        var out = new ByteArrayOutputStream();
        Reflector.diffClasses(firstClass, secondClass, new PrintStream(out));
        return out.toString();
    }

    private static Class<?> compileClassStructure(Class<?> someClass) {
        return Reflect.compile("SomeClass", printStructureResult(someClass)).type();
    }

    @Test
    void testCompile() {
        assertDoesNotThrow(() -> compileClassStructure(SimpleClass.class));
        assertDoesNotThrow(() -> compileClassStructure(ReflectorTest.class));
        assertDoesNotThrow(() -> compileClassStructure(GenericClass.class));
        assertDoesNotThrow(() -> compileClassStructure(InheritanceClass.class));
        assertDoesNotThrow(() -> compileClassStructure(AbstractClass.class));
        assertDoesNotThrow(() -> compileClassStructure(ClassWithNested.class));
        assertDoesNotThrow(() -> compileClassStructure(ThrowingClass.class));
        assertDoesNotThrow(() -> compileClassStructure(MyList.class));
        assertDoesNotThrow(() -> compileClassStructure(AbstractList.class));
    }

    @Test
    void printStructureNotSupported() {
        assertThrows(IllegalArgumentException.class, () -> printStructureResult(int[].class));
        assertThrows(IllegalArgumentException.class, () -> printStructureResult(SimpleTestA.class));
    }

    @Test
    void printStructureSimple() throws IOException {
        var expected =
                "public class SomeClass {\n" +
                "\tpublic static final int a = 0;\n" +
                "\tprivate double b;\n" +
                "\tprotected String c;\n" +
                "\tjava.util.Collection<String> bar() {\n" +
                "\t\treturn null;\n" +
                "\t}\n" +
                "\tvoid foo(java.util.Set<Integer> arg0) {}\n" +
                "}\n";
        assertEquals(expected, printStructureResult(SimpleClass.class));

        assertDoesNotThrow(() -> Reflector.printStructure(SimpleClass.class));
        var actual = new String(Files.readAllBytes(Paths.get("SomeClass.java")));
        assertEquals(expected, actual);
        assertTrue(new File("SomeClass.java").delete());
    }

    @Test
    void printStructurePackagePrivate() {
        var expectedFirstString = "public class SomeClass {";
        assertEquals(expectedFirstString, printStructureResult(ReflectorTest.class).split("\n")[0]);
    }

    @Test
    void printStructureGeneric() {
        var expected =
                "public class SomeClass<K extends Number, V> {\n" +
                "\tK key;\n" +
                "\tV value;\n" +
                "\tpublic <M extends K> void bar(M arg0) {}\n" +
                "\tprivate static <K, V> V foo(K arg0) {\n" +
                "\t\treturn null;\n" +
                "\t}\n" +
                "\tpublic void fooBar(java.util.Collection<? super K> arg0, java.util.Set<? extends V> arg1) {}\n" +
                "}\n";

        assertEquals(expected, printStructureResult(GenericClass.class));
    }

    @Test
    void printStructureInheritance() {
        var expected =
                "public class SomeClass<E> " +
                "extends ru.hse.mnmalysheva.reflector.testclasses.SimpleClass " +
                "implements ru.hse.mnmalysheva.reflector.testclasses.Interface1, " +
                "ru.hse.mnmalysheva.reflector.testclasses.Interface2<E> {\n" +
                "}\n";

        assertEquals(expected, printStructureResult(InheritanceClass.class));
    }

    @Test
    void printStructureAbstract() {
        var expected =
                "public abstract class SomeClass {\n" +
                "\tpublic abstract void bar();\n" +
                "\tprotected abstract int foo(double arg0, char arg1);\n" +
                "\tprivate void fooBar() {}\n" +
                "}\n";

        assertEquals(expected, printStructureResult(AbstractClass.class));
    }

    @Test
    void printStructureNested() {
        var expected =
                "public class SomeClass<E> {\n" +
                "\tprivate SomeClass<E>.Inner inner;\n" +
                "\tprivate SomeClass.Nested<String> nested;\n" +
                "\tpublic java.util.Set<Integer> bar() {\n" +
                "\t\treturn null;\n" +
                "\t}\n" +
                "\tpublic void foo(java.util.Set<Integer> arg0) {}\n" +
                "\tprivate class Inner {\n" +
                "\t\tjava.util.Set<Integer> set;\n" +
                "\t\tE foo(SomeClass<E>.Inner arg0) {\n" +
                "\t\t\treturn null;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tprivate static class Nested<E> {\n" +
                "\t\tprivate String string;\n" +
                "\t\tint bar(String arg0) {\n" +
                "\t\t\treturn 0;\n" +
                "\t\t}\n" +
                "\t\t<T extends E> T foo(SomeClass<E> arg0) {\n" +
                "\t\t\treturn null;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tprivate abstract static interface NestedInterface<E> {\n" +
                "\t\tpublic abstract int bar(String arg0);\n" +
                "\t\tpublic abstract <T extends E> T foo();\n" +
                "\t}\n" +
                "}\n";

        assertEquals(expected, printStructureResult(ClassWithNested.class));
    }

    @Test
    void printStructureThrowing() {
        var expected =
                "public class SomeClass<T extends Exception> {\n" +
                "\tpublic void foo() throws T, java.io.IOException {}\n" +
                "}\n";

        assertEquals(expected, printStructureResult(ThrowingClass.class));
    }

    @Test
    void diffClassesSameClass() {
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(SimpleClass.class, SimpleClass.class));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(ReflectorTest.class, ReflectorTest.class));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(GenericClass.class, GenericClass.class));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(InheritanceClass.class, InheritanceClass.class));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(AbstractClass.class, AbstractClass.class));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(ClassWithNested.class, ClassWithNested.class));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(ThrowingClass.class, ThrowingClass.class));
    }

    @Test
    void diffClassesSimple() {
        var expected =
                "ru.hse.mnmalysheva.reflector.ReflectorTest$SimpleTestA unique fields:\n" +
                "a\n" +
                "ru.hse.mnmalysheva.reflector.ReflectorTest$SimpleTestA unique methods:\n" +
                "foo()\n" +
                "ru.hse.mnmalysheva.reflector.ReflectorTest$SimpleTestB unique fields:\n" +
                "b\n" +
                "ru.hse.mnmalysheva.reflector.ReflectorTest$SimpleTestB unique methods:\n" +
                "foo(java.lang.String, int)\n";
        assertEquals(expected, diffClassesResult(SimpleTestA.class, SimpleTestB.class));

        var actual = diffClassesResult(SimpleTestA.class, SimpleTestC.class);
        assertTrue(
                actual.equals(
                        "ru.hse.mnmalysheva.reflector.ReflectorTest$SimpleTestA unique fields:\na, c\n"
                ) || actual.equals(
                        "ru.hse.mnmalysheva.reflector.ReflectorTest$SimpleTestA unique fields:\nc, a\n"
                )
        );
    }

    @Test
    void diffClassesGeneric() {
        var expected =
                "ru.hse.mnmalysheva.reflector.ReflectorTest$GenericTestA unique fields:\n" +
                "t\n" +
                "ru.hse.mnmalysheva.reflector.ReflectorTest$GenericTestA unique methods:\n" +
                "bar()\n" +
                "ru.hse.mnmalysheva.reflector.ReflectorTest$GenericTestB unique fields:\n" +
                "t\n" +
                "ru.hse.mnmalysheva.reflector.ReflectorTest$GenericTestB unique methods:\n" +
                "bar()\n";
        assertEquals(expected, diffClassesResult(GenericTestA.class, GenericTestB.class));
    }

    @Test
    void diffClassStructureAndSource() {
        assertEquals(IDENTICAL_CLASSES_MESSAGE,
                diffClassesResult(SimpleClass.class, compileClassStructure(SimpleClass.class)));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(ReflectorTest.class,
                compileClassStructure(ReflectorTest.class)));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(GenericClass.class,
                compileClassStructure(GenericClass.class)));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(InheritanceClass.class,
                compileClassStructure(InheritanceClass.class)));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(AbstractClass.class,
                compileClassStructure(AbstractClass.class)));
        assertEquals(IDENTICAL_CLASSES_MESSAGE, diffClassesResult(ThrowingClass.class,
                compileClassStructure(ThrowingClass.class)));
    }

    class SimpleTestA {
        int a, c;
        void foo() {}
        void foo(double d) {}
    }
    class SimpleTestB {
        int b, c;
        void foo(double c) {}
        void foo(String s, int t) {}
    }
    class SimpleTestC {
        void foo() {}
        void foo(double d) {}
    }

    class GenericTestA<E, T extends Interface1 & Interface2> {
        E e;
        T t;
        <M extends E> M bar() {
            return null;
        }
    }
    class GenericTestB<E, T> {
        E e;
        T t;
        <M extends T> M bar() {
            return null;
        }
    }
}