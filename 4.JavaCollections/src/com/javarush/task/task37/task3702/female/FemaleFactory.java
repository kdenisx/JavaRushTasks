package com.javarush.task.task37.task3702.female;

import com.javarush.task.task37.task3702.AbstractFactory;
import com.javarush.task.task37.task3702.Human;

public class FemaleFactory implements AbstractFactory {
    public <T extends Human> T getPerson(int age) {
        if (age <= KidGirl.MAX_AGE) return (T) new KidGirl();
        else if (age <= TeenGirl.MAX_AGE) {
            return (T) new TeenGirl();
        } else return (T) new Woman();
    }
}
