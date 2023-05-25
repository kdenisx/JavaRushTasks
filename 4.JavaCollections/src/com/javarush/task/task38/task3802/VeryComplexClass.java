package com.javarush.task.task38.task3802;

/* 
Проверяемые исключения (checked exception)
*/

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

public class VeryComplexClass {
    public void veryComplexMethod() throws Exception {
        String file = "c:/games.txt";
        InputStream is = new FileInputStream(file);
    }

    public static void main(String[] args) throws Exception {
    VeryComplexClass veryComplexClass = new VeryComplexClass();
    veryComplexClass.veryComplexMethod();
    }
}
