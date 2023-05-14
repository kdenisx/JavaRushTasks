package com.javarush.task.task36.task3605;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

/* 
Использование TreeSet
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            TreeSet<Character> result = new TreeSet<>();
            while (reader.ready()) {
                String line = reader.readLine().toLowerCase();
                for (char symbol : line.toCharArray()) {
                    if (Character.isLetter(symbol)) {
                        result.add(symbol);
                    }
                }
            }
            StringBuilder findLine = new StringBuilder();
            for (Character find : result) {
                findLine.append(find);
                if (findLine.length() == 5) break;
            }
            System.out.println(findLine.toString().trim());
        }
    }
}
