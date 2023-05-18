package com.javarush.task.task36.task3606;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* 
Осваиваем ClassLoader и Reflection
*/

public class Solution {
    private List<Class> hiddenClasses = new ArrayList<>();
    private String packageName;

    public Solution(String packageName) {
        this.packageName = packageName;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Solution solution = new Solution(Solution.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "com/javarush/task/task36/task3606/data/second");
        solution.scanFileSystem();
        System.out.println(solution.getHiddenClassObjectByKey("secondhiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("firsthiddenclassimpl"));
        System.out.println(solution.getHiddenClassObjectByKey("packa"));
    }

    public void scanFileSystem() throws ClassNotFoundException {
        if (!packageName.endsWith("\\") && !packageName.endsWith("/")) {
            packageName += "/";
        }
        File dir = new File(packageName);
        String[] files = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".class");
            }
        });
        ClassLoader classLoader = Solution.class.getClassLoader();
        for (String fileName : files) {
            String className = packageName.replaceAll("[/\\\\]", ".").substring(packageName.lastIndexOf("ru/")) + fileName.substring(0, fileName.length() - 6);
            hiddenClasses.add(classLoader.loadClass(className));
        }
    }

    public HiddenClass getHiddenClassObjectByKey(String key) {
        for (Class<?> clazz : hiddenClasses) {
            if (clazz.getSimpleName().toLowerCase().startsWith(key.toLowerCase())) {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    if ((constructor.getParameterCount() == 0) && (HiddenClass.class.isAssignableFrom(clazz))) {
                        return (HiddenClass) constructor.newInstance();
                    }
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}

