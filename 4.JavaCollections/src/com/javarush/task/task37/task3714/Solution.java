package com.javarush.task.task37.task3714;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/* 
Древний Рим
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input a roman number to be converted to decimal: ");
        String romanString = bufferedReader.readLine();
        System.out.println("Conversion result equals " + romanToInteger(romanString));
    }

    public static int romanToInteger(String s) {
        return s.replace("CM", "DCD").replace("M", "DD")
                .replace("CD", "CCCC").replace("D", "CCCCC")
                .replace("XC", "LXL").replace("C", "LL")
                .replace("XL", "XXXX").replace("L", "XXXXX")
                .replace("IX", "VIV").replace("X", "VV")
                .replace("IV", "IIII").replace("V", "IIIII").length();
    }
}
