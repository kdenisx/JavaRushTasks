package com.javarush.task.task34.task3402;

/* 
Факториал с помощью рекурсии
*/

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.factorial(9));     //362880
        System.out.println(solution.factorial(0));     //1
        System.out.println(solution.factorial(1));     //1
    }

    public int factorial(int n) {
        int result = 1;
        if (n == 1 || n == 0) {
            return result;
        }
        result = n * factorial(n-1);
        return result;
    }
}
