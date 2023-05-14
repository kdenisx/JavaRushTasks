package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

/* 
Найти класс по описанию Ӏ Java Collections: 6 уровень, 6 лекция
*/

public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static <T extends Collections> Class<T> getExpectedClass() {
        Class<?>[] expected = Collections.class.getDeclaredClasses();
        for (Class<?> clazz : expected) {
            if (List.class.isAssignableFrom(clazz) && Modifier.isPrivate(clazz.getModifiers()) && Modifier.isStatic(clazz.getModifiers())) {
                Constructor<?> constructor;
                Method method;
                try {
                    constructor = clazz.getDeclaredConstructor();
                    if (constructor.getParameterCount() != 0) continue;
                    constructor.setAccessible(true);
                    method = clazz.getDeclaredMethod("get", int.class);
                    method.setAccessible(true);
                    try {
                        method.invoke(constructor.newInstance(), 5);
                    }catch (InvocationTargetException e) {
                        if (e.getCause().toString().contains("IndexOutOfBoundsException")) {
                            return (Class<T>) clazz;
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return null;
    }
}
