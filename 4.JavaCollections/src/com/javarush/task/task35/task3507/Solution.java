package com.javarush.task.task35.task3507;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/* 
ClassLoader - что это такое?
*/

public class Solution {
    public static void main(String[] args) {
        Set<? extends Animal> allAnimals = getAllAnimals(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + Solution.class.getPackage().getName().replaceAll("[.]", "/") + "/data");
        System.out.println(allAnimals);
    }

    public static Set<? extends Animal> getAllAnimals(String pathToAnimals) {
        Set<Animal> result = new HashSet<>();
        if (!pathToAnimals.endsWith("\\") && !pathToAnimals.endsWith("/")) {
            pathToAnimals += "/";
        }
        File dir = new File(pathToAnimals);
        String[] files = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".class");
            }
        });
        for (String file : files) {
            String fullName = pathToAnimals + file;
            ClassLoader classLoader = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    byte [] bytes;
                    try {
                        bytes = Files.readAllBytes(new File(fullName).toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return defineClass(null, bytes, 0, bytes.length );
                }
            };
            String name = file.substring(0, file.length() - 6);
            Class<?> clazz = null;
            try {
                clazz = classLoader.loadClass(name);
                Constructor<?> constructor = clazz.getConstructor();
                if ((constructor.getModifiers() == 1) && (constructor.getParameterCount() == 0) && (Animal.class.isAssignableFrom(clazz))) {
                    result.add((Animal) clazz.newInstance());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            }
        }
        return result;
    }
}
