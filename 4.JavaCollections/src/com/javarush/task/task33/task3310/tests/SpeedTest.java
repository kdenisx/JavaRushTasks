package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {

    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        Date start = new Date();
        for (String string : strings) {
            ids.add(shortener.getId(string));
        }
        Date end = new Date();
        return end.getTime() - start.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        Date start = new Date();
        for (Long number : ids) {
            strings.add(shortener.getString(number));
        }
        Date end = new Date();
        return end.getTime() - start.getTime();
    }

    @Test
    public void testHashMapStorage() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());
        Set<String> origStrings = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }
        Set<Long> ids = new HashSet<>();
        Long timeToGetIds1 = getTimeToGetIds(shortener1, origStrings, ids);
        ids = new HashSet<>();
        Long timeToGetIds2 = getTimeToGetIds(shortener2, origStrings, ids);
        Assert.assertNotEquals(timeToGetIds1, timeToGetIds2);
        long timeToGetStrings1 = getTimeToGetStrings(shortener1, ids, origStrings);
        long timeToGetStrings2 = getTimeToGetStrings(shortener2, ids, origStrings);
        Assert.assertEquals(timeToGetStrings1, timeToGetStrings2, 30);

    }
}
