package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Solution {

    public static void main(String[] args) {
    testStrategy(new HashMapStorageStrategy(), 10000);
    testStrategy(new OurHashMapStorageStrategy(), 10000);
    testStrategy(new FileStorageStrategy(), 100);
    testStrategy(new OurHashBiMapStorageStrategy(), 10000);
    testStrategy(new HashBiMapStorageStrategy(), 10000);
    testStrategy(new DualHashBidiMapStorageStrategy(), 10000);
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        Set<Long> result = new HashSet<>();
        for (String str : strings) {
            result.add(shortener.getId(str));
        }
        return result;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> result = new HashSet<>();
        for (Long id : keys) {
            result.add(shortener.getString(id));
        }
        return result;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName() + ":");
        Set<String> strings = new HashSet<>();
        for (int i = 0; i < elementsNumber; i++) {
            strings.add(Helper.generateRandomString());
        }
        Shortener shortener = new Shortener(strategy);
        Date start = new Date();
        Set<Long> testIds = getIds(shortener, strings);
        Date end = new Date();
        Helper.printMessage("Lead time getIds: " + String.valueOf(end.getTime() - start.getTime()) + " ms.");
        Date start1 = new Date();
        Set<String> testStrings = getStrings(shortener, testIds);
        Date end1 = new Date();
        Helper.printMessage("Lead time getStrings: " + String.valueOf(end1.getTime() - start1.getTime()) + " ms.");
        if (Objects.equals(strings, testStrings)) {
            Helper.printMessage("Тест пройден.");
        } else {
            Helper.printMessage("Тест не пройден.");
        }
    }
}
