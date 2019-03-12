package ru.hse.mnmalysheva.reflector;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class allows to print file structure (fields, methods, nested classes and interfaces)
 * as a compilable file and to show different fields and methods of two classes.
 */
class Reflector {
    private static final String CLASS_NAME = "SomeClass";
    private static final String OBJECT = "java.lang.Object";
    private static final String CLASS = "class ";
    private static final String EXTENDS = " extends ";
    private static final String IMPLEMENTS = " implements ";
    private static final String THROWS = " throws ";
    private static final String NOTHING = "";
    private static final String SPACE = " ";

    /**
     * Prints all {@code someClass} fields, methods, nested classes and interfaces into {@link PrintStream}.
     * {@code someClass} should not inner of nested class.
     * If any of printed fields or methods refers to outside package-private types
     * ar if any of printed classes extends class with no default constructor,
     * output will be generated, but it will not be compilable.
     * @throws IllegalArgumentException if given class is not supported.
     */
    public static void printStructure(@NotNull Class<?> someClass, @NotNull PrintStream out) {
        if (someClass.isMemberClass() || notSupportedClass(someClass)) {
            throw new IllegalArgumentException();
        }
        var builder = new StringBuilder();
        printStructure(someClass, true, 0, builder);
        var result = correctClassText(builder.toString(), someClass.getName());
        out.print(result);
    }

    /**
     * Prints all {@code someClass} fields, methods, nested classes and interfaces
     * into SomeClass.java file located in the current directory.
     * {@code someClass} should not inner of nested class.
     * If any of printed fields or methods refers to outside package-private types
     * ar if any of printed classes extends class with no default constructor,
     * output will be generated, but it will not be compilable.
     * @throws IOException if can not create SomeClass.java or has no access to write into it.
     * @throws IllegalArgumentException if given class is not supported.
     */
    public static void printStructure(@NotNull Class<?> someClass) throws IOException {
        var classFile = createClassFile();
        try (var out = new FileOutputStream(classFile)) {
            printStructure(someClass, new PrintStream(out));
        }
    }

    /**
     * Prints all different fields and methods of specified classes into {@link PrintStream}.
     * Fields are considered to be equal if they have equal modifiers, type and name.
     * Methods are considered to be equal if they have equal modifiers, type parameters,
     * return type, parameter types, exception types and name.
     * Note that if two different classes have inner class with the same name,
     * such inner classes are considered to have different types.
     */
    public static void diffClasses(@NotNull Class<?> firstClass,
                                   @NotNull Class<?> secondClass,
                                   @NotNull PrintStream out) {
        var firstMinusSecond = classDifference(firstClass, secondClass);
        var secondMinusFirst = classDifference(secondClass, firstClass);
        if (firstMinusSecond.isEmpty() && secondMinusFirst.isEmpty()) {
            out.println("Classes are identical");
        } else {
            out.print(firstMinusSecond);
            out.print(secondMinusFirst);
        }
    }

    /**
     * Prints all different fields and methods of specified classes into {@link PrintStream}.
     * Fields are considered to be equal if they have equal modifiers, type and name.
     * Methods are considered to be equal if they have equal modifiers, type parameters,
     * return type, parameter types, exception types and name.
     * Note that if two different classes have inner class with the same name,
     * such inner classes are considered to have different types.
     */
    public static void diffClasses(@NotNull Class<?> firstClass, @NotNull Class<?> secondClass) {
        diffClasses(firstClass, secondClass, System.out);
    }

    private static void printStructure(@NotNull Class<?> someClass,
                                       boolean isRootClass,
                                       int indent,
                                       @NotNull StringBuilder builder) {
        if (notSupportedClass(someClass)) {
            return;
        }
        printHeading(someClass, isRootClass, indent, builder);
        printFields(someClass, indent + 1, builder);
        printMethods(someClass, indent + 1, builder);
        printMemberClasses(someClass, indent + 1, builder);
        printEnding(indent, builder);
    }

    private static String correctClassText(@NotNull String classText, @NotNull String className) {
        var canonical = classText.replace("$", ".");
        var simplified = canonical.replaceAll(
                "(?<!\\.)java\\.lang\\.(?=\\p{Upper})", ""
        );
        return simplified.replaceAll(
                "(?<!\\.)" + className.replace(".", "\\.") + "(?!\\p{Alpha})",
                CLASS_NAME
        );
    }

    private static boolean notSupportedClass(@NotNull Class<?> someClass) {
        return someClass.isAnnotation() ||
                someClass.isArray() ||
                someClass.isEnum() ||
                someClass.isLocalClass() ||
                someClass.isPrimitive() ||
                someClass.isSynthetic();
    }

    private static @NotNull File createClassFile() throws IOException {
        var classFile = new File(CLASS_NAME + ".java");
        //noinspection ResultOfMethodCallIgnored
        classFile.createNewFile();
        return classFile;
    }

    private static @NotNull String indentString(int indent) {
        return "\t".repeat(indent);
    }

    private static @NotNull String modifiersString(int modifiers) {
        var modifiersString = Modifier.toString(modifiers);
        return modifiersString + (modifiersString.isEmpty() ? "" : " ");
    }

    private static @NotNull String typeVariablesString(TypeVariable<?>[] typeVariables) {
        if (typeVariables.length == 0) {
            return "";
        }
        var typeVariablesJoiner = new StringJoiner(", ");
        for (var typeVariable : typeVariables) {
            var boundsJoiner = new StringJoiner(" & ");
            for (var type : typeVariable.getBounds()) {
                if (!type.getTypeName().equals(OBJECT)) {
                    boundsJoiner.add(type.getTypeName());
                }
            }
            var bounds = boundsJoiner.toString();
            typeVariablesJoiner.add(typeVariable.getName() + (bounds.isEmpty() ? NOTHING : EXTENDS + bounds));
        }
        return "<" + typeVariablesJoiner.toString() + ">";
    }

    private static @NotNull String parametersString(@NotNull Parameter[] parameters) {
        var parametersString = Stream.of(parameters)
                .map(Parameter::toString)
                .collect(Collectors.joining(", "));
        return "(" + parametersString + ")";
    }

    private static void printHeading(@NotNull Class<?> someClass,
                                     boolean isRootClass,
                                     int indent,
                                     @NotNull StringBuilder builder) {
        builder.append(indentString(indent))
                .append(modifiersString(someClass.getModifiers() | (isRootClass ? Modifier.PUBLIC : 0)))
                .append(someClass.isInterface() ? NOTHING : CLASS)
                .append(isRootClass ? CLASS_NAME : someClass.getSimpleName())
                .append(typeVariablesString(someClass.getTypeParameters()));
        if (!someClass.isInterface()) {
            var superclassName = someClass.getGenericSuperclass().getTypeName();
            if (!superclassName.equals(OBJECT)) {
                builder.append(EXTENDS).append(superclassName);
            }
        }
        var interfaces = someClass.getGenericInterfaces();
        if (interfaces.length > 0) {
            builder.append(someClass.isInterface() ? EXTENDS : IMPLEMENTS)
                    .append(Stream.of(interfaces).map(Type::getTypeName).collect(Collectors.joining(", ")));
        }
        builder.append(" {\n");
    }

    private static void printFields(@NotNull Class<?> someClass,
                                    int indent,
                                    @NotNull StringBuilder builder) {
        var sortedFields =
                Stream.of(someClass.getDeclaredFields())
                        .sorted(Comparator.comparing(Field::getName))
                        .collect(Collectors.toList());
        for (var field : sortedFields) {
            if (field.isSynthetic()) {
                continue;
            }
            builder.append(indentString(indent))
                    .append(modifiersString(field.getModifiers()))
                    .append(field.getGenericType().getTypeName())
                    .append(SPACE)
                    .append(field.getName());
            if (Modifier.isFinal(field.getModifiers())) {
                builder.append(" = ").append(field.getType().isPrimitive() ? "0" : "null");
            }
            builder.append(";\n");
        }
    }

    private static void printMethods(@NotNull Class<?> someClass,
                                     int indent,
                                     @NotNull StringBuilder builder) {
        var sortedMethods =
                Stream.of(someClass.getDeclaredMethods())
                        .sorted(Comparator.comparing(Method::getName))
                        .collect(Collectors.toList());
        for (var method : sortedMethods) {
            if (method.isSynthetic()) {
                continue;
            }
            builder.append(indentString(indent));
            builder.append(modifiersString(method.getModifiers()));
            var typeVariables = typeVariablesString(method.getTypeParameters());
            if (!typeVariables.isEmpty()) {
                builder.append(typeVariables).append(SPACE);
            }
            builder.append(method.getGenericReturnType().getTypeName())
                    .append(SPACE).append(method.getName())
                    .append(parametersString(method.getParameters()));

            var exceptions = method.getGenericExceptionTypes();
            if (exceptions.length > 0) {
                builder.append(THROWS)
                        .append(Stream.of(exceptions)
                                .map(Type::getTypeName)
                                .sorted()
                                .collect(Collectors.joining(", ")));
            }

            if (Modifier.isAbstract(method.getModifiers())) {
                builder.append(";");
            } else {
                builder.append(" {\n")
                        .append(indentString(indent + 1))
                        .append("throw new UnsupportedOperationException();\n")
                        .append(indentString(indent))
                        .append("}");
            }
            builder.append("\n");
        }
    }

    private static void printMemberClasses(@NotNull Class<?> someClass,
                                           int indent,
                                           @NotNull StringBuilder builder) {
        var sortedClasses =
                Stream.of(someClass.getDeclaredClasses())
                        .sorted(Comparator.comparing(Class::getSimpleName))
                        .collect(Collectors.toList());
        for (var memberClass : sortedClasses) {
            printStructure(memberClass, false, indent, builder);
        }
    }

    private static void printEnding(int indent, @NotNull StringBuilder builder) {
        builder.append(indentString(indent)).append("}\n");
    }

    private static Set<ClassIndifferentField> classFields(@NotNull Class<?> someClass) {
        return Stream.of(someClass.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .map(ClassIndifferentField::new)
                .collect(Collectors.toSet());
    }

    private static Set<ClassIndifferentMethod> classMethods(@NotNull Class<?> someClass) {
        return Stream.of(someClass.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .map(ClassIndifferentMethod::new)
                .collect(Collectors.toSet());
    }

    private static String classDifference(@NotNull Class<?> firstClass, @NotNull Class<?> secondClass) {
        StringBuilder builder = new StringBuilder();

        var firstClassFields = classFields(firstClass);
        var secondClassFields = classFields(secondClass);
        var fieldDifference = new HashSet<>(firstClassFields);
        fieldDifference.removeAll(secondClassFields);
        if (!fieldDifference.isEmpty()) {
            builder.append(firstClass.getName()).append(" unique fields:\n");
            builder.append(fieldDifference.stream().map(f -> f.name).collect(Collectors.joining(", ")));
            builder.append("\n");
        }

        var firstClassMethods = classMethods(firstClass);
        var secondClassMethods = classMethods(secondClass);
        var methodDifference = new HashSet<>(firstClassMethods);
        methodDifference.removeAll(secondClassMethods);
        if (!methodDifference.isEmpty()) {
            builder.append(firstClass.getName()).append(" unique methods:\n");
            builder.append(methodDifference.stream().map(f ->
                    f.name + "(" +
                    f.parameterTypes.stream().map(t -> t.typeName).collect(Collectors.joining(", ")) + ")"
            ).collect(Collectors.joining(", ")));
            builder.append("\n");
        }

        return builder.toString();
    }

    private static class GenericType {
        private final String typeName;
        private final Set<String> bounds;
        GenericType(@NotNull Type type, @NotNull List<TypeVariable<?>> typeParameters) {
            typeName = type.getTypeName();
            var bounds = typeParameters.stream()
                    .filter(f -> f.getName().equals(typeName))
                    .map(TypeVariable::getBounds).findAny();
            this.bounds = bounds.map(Set::of).orElse(Collections.emptySet())
                    .stream().map(Type::getTypeName).collect(Collectors.toSet());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            var that = (GenericType) o;
            return typeName.equals(that.typeName) &&
                   bounds.equals(that.bounds);
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeName, bounds);
        }
    }

    private static class ClassIndifferentField {
        private final int modifiers;
        private final GenericType type;
        private final String name;

        private ClassIndifferentField(@NotNull Field field) {
            modifiers = field.getModifiers();
            type = new GenericType(field.getGenericType(), List.of(field.getDeclaringClass().getTypeParameters()));
            name = field.getName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            var that = (ClassIndifferentField) o;
            return modifiers == that.modifiers &&
                    type.equals(that.type) &&
                    name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(modifiers, type, name);
        }
    }

    private static class ClassIndifferentMethod {
        private final int modifiers;
        private final String typeParameters;
        private final GenericType returnType;
        private final String name;
        private final List<GenericType> parameterTypes;
        private final Set<GenericType> exceptionTypes;

        private ClassIndifferentMethod(@NotNull Method method) {
            var allTypeParameters = Stream.concat(
                    Stream.of(method.getTypeParameters()),
                    Modifier.isStatic(method.getModifiers()) ?
                            Stream.empty() :
                            Stream.of(method.getDeclaringClass().getTypeParameters())
            ).collect(Collectors.toList());

            modifiers = method.getModifiers();
            returnType = new GenericType(method.getGenericReturnType(), allTypeParameters);
            typeParameters = typeVariablesString(method.getTypeParameters());
            name = method.getName();
            parameterTypes =
                    Stream.of(method.getGenericParameterTypes())
                            .map(type -> new GenericType(type, allTypeParameters))
                            .collect(Collectors.toList());
            exceptionTypes =
                    Stream.of(method.getGenericExceptionTypes())
                            .map(type -> new GenericType(type, allTypeParameters))
                            .collect(Collectors.toSet());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            var that = (ClassIndifferentMethod) o;
            return modifiers == that.modifiers &&
                    typeParameters.equals(that.typeParameters) &&
                    returnType.equals(that.returnType) &&
                    name.equals(that.name) &&
                    parameterTypes.equals(that.parameterTypes) &&
                    exceptionTypes.equals(that.exceptionTypes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(modifiers, typeParameters, returnType, name, parameterTypes, exceptionTypes);
        }
    }
}
