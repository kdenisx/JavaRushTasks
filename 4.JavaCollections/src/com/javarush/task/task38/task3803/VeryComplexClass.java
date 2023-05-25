package com.javarush.task.task38.task3803;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* 
Runtime исключения (unchecked exception)
*/

public class VeryComplexClass {
    public void methodThrowsClassCastException() {
    Object obj = new String("hello");
    Integer result = (Integer) obj;
    }

    public void methodThrowsNullPointerException() {
    String result = null;
    String line = "string";
        System.out.println(result.equals(line));
    }

    public static void main(String[] args) {
    VeryComplexClass veryComplexClass = new VeryComplexClass();
    //veryComplexClass.methodThrowsClassCastException();
    veryComplexClass.methodThrowsNullPointerException();
    }
}
