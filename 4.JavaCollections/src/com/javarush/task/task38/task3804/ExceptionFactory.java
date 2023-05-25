package com.javarush.task.task38.task3804;

public class ExceptionFactory {
    public static Throwable getException(Enum name) {
        if (name == null) {
            return new IllegalArgumentException();
        }
        String message = name.toString().replaceAll("_", " ").toLowerCase().
                replaceFirst(name.toString().substring(0, 1).toLowerCase(), name.toString().substring(0, 1));
        if (name.getClass().getSimpleName().equals("ApplicationExceptionMessage")) {
            return new Exception(message);
        } else if (name.getClass().getSimpleName().equals("DatabaseExceptionMessage")) {
            return new RuntimeException(message);
        } else if (name.getClass().getSimpleName().equals("UserExceptionMessage")) {
            return new Error(message);
        } else {
            return new IllegalArgumentException();
        }
    }

}
